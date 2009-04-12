package org.seasar.ymir.constraint.impl;

import static org.seasar.ymir.RequestProcessor.ATTR_NOTES;
import static org.seasar.ymir.constraint.Globals.APPKEY_CORE_CONSTRAINT_PERMISSIONDENIEDMETHOD_ENABLE;
import static org.seasar.ymir.constraint.Globals.APPKEY_CORE_CONSTRAINT_VALIDATIONFAILEDMETHOD_ENABLE;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.Request;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.constraint.ConfirmationDecider;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintBundle;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.Globals;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.constraint.annotation.SuppressConstraints;
import org.seasar.ymir.constraint.annotation.Validator;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.ContainerUtils;

/**
 * 制約チェックを行なうためのInterceptorです。
 */
public class ConstraintInterceptor extends AbstractYmirProcessInterceptor {
    public static final String ACTION_VALIDATIONFAILED = "_validationFailed";

    public static final String ACTION_PERMISSIONDENIED = "_permissionDenied";

    static final ConfirmationDecider DECIDER_DEPENDS_ON_SUPPRESSTYPE = new ConfirmationDecider() {
        public boolean isConfirmed(Object page, Request request,
                ConstraintType type, Set<ConstraintType> suppressTypeSet) {
            return !suppressTypeSet.contains(type);
        }
    };

    static final Set<ConstraintType> EMPTY_SUPPRESSTYPESET = EnumSet
            .noneOf(ConstraintType.class);

    private ActionManager actionManager_;

    private AnnotationHandler annotationHandler_;

    private ApplicationManager applicationManager_;

    private Map<Method, Set<ConstraintType>> suppressTypeSetMap_;

    private Map<Method, ConstraintBag<?>[]> bagsForActionMap_;

    private Map<Class<?>, ConstraintBag<?>[]> bagsForPageClassMap_;

    private Map<Key, Method[]> validatorMethodsMap_;

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setCacheManager(CacheManager cacheManager) {
        suppressTypeSetMap_ = cacheManager.newMap();
        bagsForActionMap_ = cacheManager.newMap();
        bagsForPageClassMap_ = cacheManager.newMap();
        validatorMethodsMap_ = cacheManager.newMap();
    }

    @Override
    public Action actionInvoking(Request request, Action action) {
        PageComponent pageComponent = request.getCurrentDispatch()
                .getPageComponent();

        Action finalAction = null;
        try {
            Notes notes = confirmConstraint(pageComponent, request);
            if (notes != null) {
                request.setAttribute(ATTR_NOTES, notes);

                if (isValidationFailedMethodEnabled()) {
                    // 互換性のため。

                    // バリデーションエラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
                    // メソッドが存在しなければ何もしない（元のアクションメソッドの呼び出しをスキップする）。
                    finalAction = pageComponent
                            .accept(new VisitorForFindingValidationFailedMethod(
                                    notes));
                    if (finalAction == null) {
                        finalAction = actionManager_
                                .newVoidAction(pageComponent.getPage());
                    }
                } else {
                    throw new WrappingRuntimeException(
                            new ValidationFailedException(notes));
                }
            } else {
                finalAction = action;
            }
        } catch (PermissionDeniedException ex) {
            if (isPermissionDeniedMethodEnabled()) {
                // 互換性のため。

                // 権限エラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
                // メソッドが存在しなければPermissionDeniedExceptionを上に再スローする。
                finalAction = (Action) pageComponent
                        .accept(new VisitorForFindingPermissionDeniedMethod(ex));
                if (finalAction == null) {
                    throw new WrappingRuntimeException(ex);
                }
            } else {
                throw new WrappingRuntimeException(ex);
            }
        }

        return finalAction;
    }

    boolean isValidationFailedMethodEnabled() {
        return PropertyUtils.valueOf(applicationManager_
                .findContextApplication().getProperty(
                        APPKEY_CORE_CONSTRAINT_VALIDATIONFAILEDMETHOD_ENABLE),
                true);
    }

    boolean isPermissionDeniedMethodEnabled() {
        return PropertyUtils.valueOf(applicationManager_
                .findContextApplication().getProperty(
                        APPKEY_CORE_CONSTRAINT_PERMISSIONDENIEDMETHOD_ENABLE),
                true);
    }

    Notes confirmConstraint(PageComponent pageComponent, Request request)
            throws PermissionDeniedException {
        Action action = request.getCurrentDispatch().getAction();
        MethodInvoker actionInvoker = null;
        Method actionMethod = null;
        if (action != null) {
            actionInvoker = action.getMethodInvoker();
            if (actionInvoker != null) {
                actionMethod = actionInvoker.getMethod();
            }
        }
        Set<ConstraintType> suppressTypeSet = getSuppressTypeSet(actionMethod);
        Notes notes = new Notes();

        // 共通の制約をチェックする。
        confirmConstraint(getConstraintBagsFromConstraintBundles(),
                pageComponent.getPage(), request, suppressTypeSet, notes);

        // アクションに関連付けられている制約をチェックする。
        confirmConstraint(getConstraintBagsForActionMethod(actionMethod),
                pageComponent.getPage(), request, suppressTypeSet, notes);

        // それ以外の制約をチェックする。
        try {
            VisitorForConfirmingConstraint visitor = new VisitorForConfirmingConstraint(
                    request, suppressTypeSet, notes);
            pageComponent.accept(visitor);

            return visitor.getNotes();
        } catch (WrappingRuntimeException ex) {
            if (ex.getCause() instanceof PermissionDeniedException) {
                throw (PermissionDeniedException) ex.getCause();
            } else {
                throw ex;
            }
        }
    }

    Set<ConstraintType> getSuppressTypeSet(Method actionMethod) {
        if (actionMethod == null) {
            return EnumSet.noneOf(ConstraintType.class);
        }

        Set<ConstraintType> suppressTypeSet = suppressTypeSetMap_
                .get(actionMethod);
        if (suppressTypeSet == null) {
            suppressTypeSet = EnumSet.noneOf(ConstraintType.class);
            ConstraintType[] types = getSuppressConstraintsValue(actionMethod);
            if (types != null) {
                for (int i = 0; i < types.length; i++) {
                    suppressTypeSet.add(types[i]);
                }
            }
            suppressTypeSetMap_.put(actionMethod, suppressTypeSet);
        }
        return suppressTypeSet;
    }

    @SuppressWarnings("deprecation")
    ConstraintType[] getSuppressConstraintsValue(Method actionMethod) {
        SuppressConstraints suppress = annotationHandler_.getAnnotation(
                actionMethod, SuppressConstraints.class);
        if (suppress != null) {
            return suppress.value();
        }

        return null;
    }

    void confirmConstraint(ConstraintBag<?>[] bags, Object page,
            Request request, Set<ConstraintType> suppressTypeSet, Notes notes)
            throws PermissionDeniedException {
        for (int i = 0; i < bags.length; i++) {
            try {
                bags[i].confirm(page, request, suppressTypeSet);
            } catch (PermissionDeniedException ex) {
                throw ex;
            } catch (ValidationFailedException ex) {
                notes.add(ex.getNotes());
            } catch (ConstraintViolatedException ex) {
                throw new RuntimeException("May logic error", ex);
            }
        }
    }

    ConstraintBag<?>[] getConstraintBagsFromConstraintBundles() {
        Application application = applicationManager_.findContextApplication();
        ConstraintBag<?>[] bags = application
                .getRelatedObject(ConstraintBag[].class);
        if (bags == null) {
            ComponentDef[] bundleCds = ContainerUtils.findAllComponentDefs(
                    application.getS2Container(), ConstraintBundle.class);
            List<ConstraintBag<?>> list = new ArrayList<ConstraintBag<?>>();
            for (int i = 0; i < bundleCds.length; i++) {
                createConstraintBags(bundleCds[i].getComponentClass(),
                        (ConstraintBundle) bundleCds[i].getComponent(), list);
            }
            bags = list.toArray(new ConstraintBag[0]);
            application.setRelatedObject(ConstraintBag[].class, bags);
        }
        return bags;
    }

    ConstraintBag<?>[] getConstraintBagsForActionMethod(Method actionMethod) {
        if (actionMethod == null) {
            return new ConstraintBag[0];
        }

        ConstraintBag<?>[] bags = bagsForActionMap_.get(actionMethod);
        if (bags == null) {
            ArrayList<ConstraintBag<?>> list = new ArrayList<ConstraintBag<?>>();
            createConstraintBags(actionMethod, ConstraintBag.DECIDER_ALWAYS,
                    list);
            bags = list.toArray(new ConstraintBag[0]);
            bagsForActionMap_.put(actionMethod, bags);
        }
        return bags;
    }

    ConstraintBag<?>[] getConstraintBagsForPageClass(Class<?> pageClass) {
        ConstraintBag<?>[] bags = bagsForPageClassMap_.get(pageClass);
        if (bags == null) {
            ArrayList<ConstraintBag<?>> list = new ArrayList<ConstraintBag<?>>();

            createConstraintBags(pageClass, DECIDER_DEPENDS_ON_SUPPRESSTYPE,
                    list);

            // fieldは対象外。
            BeanInfo beanInfo;
            try {
                beanInfo = Introspector.getBeanInfo(pageClass);
            } catch (IntrospectionException ex) {
                throw new RuntimeException(ex);
            }
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < pds.length; i++) {
                createConstraintBags(pds[i].getReadMethod(),
                        DECIDER_DEPENDS_ON_SUPPRESSTYPE, list);
                createConstraintBags(pds[i].getWriteMethod(),
                        DECIDER_DEPENDS_ON_SUPPRESSTYPE, list);
            }

            bags = list.toArray(new ConstraintBag[0]);
            bagsForPageClassMap_.put(pageClass, bags);
        }
        return bags;
    }

    @SuppressWarnings("unchecked")
    void createConstraintBags(AnnotatedElement element,
            ConfirmationDecider decider, List<ConstraintBag<?>> list) {
        if (element == null) {
            return;
        }

        for (Annotation annotation : annotationHandler_.getMarkedAnnotations(
                element, ConstraintAnnotation.class)) {
            ConstraintAnnotation constraintAnnotation = annotation
                    .annotationType().getAnnotation(ConstraintAnnotation.class);
            list.add(new ConstraintBag(((Constraint) getS2Container()
                    .getComponent(constraintAnnotation.component())),
                    annotation, element, decider));
        }
    }

    void confirmConstraint(Object page, Class<?> pageClass, Request request,
            Set<ConstraintType> suppressTypeSet, Notes notes)
            throws PermissionDeniedException {
        // クラスとプロパティに関連付けられている制約をチェックする。
        confirmConstraint(getConstraintBagsForPageClass(pageClass), page,
                request, suppressTypeSet, notes);

        // Validatorアノテーションがついているメソッドを実行する。
        if (!suppressTypeSet.contains(ConstraintType.VALIDATION)) {
            Action[] validators = getValidators(page, pageClass, request
                    .getCurrentDispatch().getAction());
            for (int i = 0; i < validators.length; i++) {
                try {
                    Object invoked = validators[i].invoke();
                    if (invoked instanceof Notes) {
                        notes.add((Notes) invoked);
                    }
                } catch (WrappingRuntimeException ex) {
                    Throwable cause = ex.getCause();
                    if (cause instanceof ValidationFailedException) {
                        notes.add(((ValidationFailedException) cause)
                                .getNotes());
                    } else {
                        throw ex;
                    }
                }
            }
        }
    }

    Action[] getValidators(Object page, Class<?> pageClass, Action action) {
        String actionName = null;
        Object[] actionParameters = new Object[0];
        if (action != null) {
            MethodInvoker actionInvoker = action.getMethodInvoker();
            if (actionInvoker != null) {
                Method actionMethod = actionInvoker.getMethod();
                if (actionMethod != null) {
                    actionName = actionMethod.getName();
                }
                actionParameters = actionInvoker.getParameters();
            }
        }

        Key key = new Key(pageClass, actionName);
        Method[] validatorMethods = validatorMethodsMap_.get(key);
        if (validatorMethods == null) {
            List<Method> list = new ArrayList<Method>();
            for (Method method : ClassUtils.getMethods(pageClass)) {
                if (actionManager_.isMatched(actionName,
                        getValidatorValue(method))) {
                    list.add(method);
                }
            }
            validatorMethods = list.toArray(new Method[0]);
            validatorMethodsMap_.put(key, validatorMethods);
        }

        List<Action> list = new ArrayList<Action>();
        for (int i = 0; i < validatorMethods.length; i++) {
            list.add(actionManager_.newAction(page, pageClass,
                    validatorMethods[i], actionParameters));
        }

        return list.toArray(new Action[0]);
    }

    @SuppressWarnings("deprecation")
    String[] getValidatorValue(Method method) {
        Validator validator = annotationHandler_.getAnnotation(method,
                Validator.class);
        if (validator != null) {
            return validator.value();
        }

        return null;
    }

    protected S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    protected class VisitorForFindingValidationFailedMethod extends
            PageComponentVisitor<Action> {
        private Notes notes_;

        public VisitorForFindingValidationFailedMethod(Notes notes) {
            notes_ = notes;
        }

        public Action process(PageComponent pageComponent) {
            Object page = pageComponent.getPage();
            Class<?> pageClass = pageComponent.getPageClass();
            Method[] methods = ClassUtils.getMethods(pageClass,
                    ACTION_VALIDATIONFAILED);
            if (methods.length == 1) {
                return actionManager_.newAction(page, pageClass, methods[0],
                        new Object[] { notes_ });
            } else if (methods.length == 0) {
                return null;
            } else {
                throw new IllegalClientCodeRuntimeException("Method '"
                        + ACTION_VALIDATIONFAILED + "' must be single: class="
                        + pageClass + ", method=" + Arrays.asList(methods));
            }
        }
    }

    protected class VisitorForFindingPermissionDeniedMethod extends
            PageComponentVisitor<Action> {
        private PermissionDeniedException ex_;

        public VisitorForFindingPermissionDeniedMethod(
                PermissionDeniedException ex) {
            ex_ = ex;
        }

        public Action process(PageComponent pageComponent) {
            Object page = pageComponent.getPage();
            Class<?> pageClass = pageComponent.getPageClass();
            Method[] methods = ClassUtils.getMethods(pageClass,
                    ACTION_PERMISSIONDENIED);
            if (methods.length == 1) {
                return actionManager_.newAction(page, pageClass, methods[0],
                        new Object[] { ex_ });
            } else if (methods.length == 0) {
                return null;
            } else {
                throw new IllegalClientCodeRuntimeException("Method '"
                        + ACTION_PERMISSIONDENIED + "' must be single: class="
                        + pageClass + ", method=" + Arrays.asList(methods));
            }
        }
    }

    protected class VisitorForConfirmingConstraint extends
            PageComponentVisitor<Object> {
        private Request request_;

        private Set<ConstraintType> suppressTypeSet_;

        private Notes notes_;

        public VisitorForConfirmingConstraint(Request request,
                Set<ConstraintType> suppressTypeSet, Notes notes) {
            request_ = request;
            suppressTypeSet_ = suppressTypeSet;
            notes_ = notes;
        }

        public Object process(PageComponent pageComponent) {
            Object page = pageComponent.getPage();
            try {
                confirmConstraint(page, pageComponent.getPageClass(), request_,
                        suppressTypeSet_, notes_);
            } catch (PermissionDeniedException ex) {
                throw new WrappingRuntimeException(ex);
            }

            return null;
        }

        public Notes getNotes() {
            if (!notes_.isEmpty()) {
                return notes_;
            } else {
                return null;
            }
        }
    }

    protected static class Key {
        private Class<?> pageClass_;

        private String actionName_;

        public Key(Class<?> pageClass, String actionName) {
            pageClass_ = pageClass;
            actionName_ = actionName;
        }

        @Override
        public int hashCode() {
            int code = pageClass_.hashCode();
            if (actionName_ != null) {
                code += actionName_.hashCode();
            }
            return code;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            Key o = (Key) obj;
            if (o.actionName_ == null) {
                if (actionName_ != null) {
                    return false;
                }
            } else {
                if (!o.actionName_.equals(actionName_)) {
                    return false;
                }
            }
            if (o.pageClass_ != pageClass_) {
                return false;
            }
            return true;
        }
    }
}
