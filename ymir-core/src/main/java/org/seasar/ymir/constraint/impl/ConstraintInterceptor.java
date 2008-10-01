package org.seasar.ymir.constraint.impl;

import static org.seasar.ymir.RequestProcessor.ATTR_NOTES;

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
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.constraint.annotation.SuppressConstraints;
import org.seasar.ymir.constraint.annotation.Validator;
import org.seasar.ymir.impl.MethodInvokerImpl;
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

    private Map<Class<?>, Method[]> validatorMethodsMap_;

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
    public Action actionInvoking(Request request, Action originalAction,
            Action action) {
        PageComponent pageComponent = request.getCurrentDispatch()
                .getPageComponent();

        Action finalAction = null;
        try {
            Notes notes = confirmConstraint(pageComponent, request);
            if (notes != null) {
                request.setAttribute(ATTR_NOTES, notes);

                // バリデーションエラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
                // メソッドが存在しなければ何もしない（元のアクションメソッドの呼び出しをスキップする）。
                finalAction = pageComponent
                        .accept(new VisitorForFindingValidationFailedMethod(
                                notes));
                if (finalAction == null) {
                    finalAction = actionManager_.newVoidAction(pageComponent
                            .getPage());
                }
            } else {
                finalAction = action;
            }
        } catch (PermissionDeniedException ex) {
            // 権限エラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
            // メソッドが存在しなければPermissionDeniedExceptionを上に再スローする。
            finalAction = (Action) pageComponent
                    .accept(new VisitorForFindingPermissionDeniedMethod(ex));
            if (finalAction == null) {
                throw new WrappingRuntimeException(ex);
            }
        }

        return finalAction;
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

        // 後方互換性のため。
        org.seasar.ymir.annotation.SuppressConstraints suppress2 = annotationHandler_
                .getAnnotation(actionMethod,
                        org.seasar.ymir.annotation.SuppressConstraints.class);
        if (suppress2 != null) {
            return suppress2.value();
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
            MethodInvoker[] validators = getValidators(pageClass, request
                    .getCurrentDispatch().getAction());
            for (int i = 0; i < validators.length; i++) {
                try {
                    Object invoked = validators[i].invoke(page);
                    if (invoked instanceof Notes) {
                        notes.add((Notes) invoked);
                    }
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException("May logic error", ex);
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

    MethodInvoker[] getValidators(Class<?> pageClass, Action action) {
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

        Method[] validatorMethods = validatorMethodsMap_.get(pageClass);
        if (validatorMethods == null) {
            List<Method> list = new ArrayList<Method>();
            Method[] methods = ClassUtils.getMethods(pageClass);
            for (int i = 0; i < methods.length; i++) {
                String[] actionNames = getValidatorValue(methods[i]);
                if (actionNames != null) {
                    if (actionNames.length > 0) {
                        // 対象アクションが限定されているので、アクションが対象アクションでない
                        // 場合は無視するようにする。
                        boolean matched = false;
                        for (int j = 0; j < actionNames.length; j++) {
                            if (actionNames[j].equals(actionName)) {
                                matched = true;
                                break;
                            }
                        }
                        if (!matched) {
                            continue;
                        }
                    }
                    list.add(methods[i]);
                }
            }
            validatorMethods = list.toArray(new Method[0]);
            validatorMethodsMap_.put(pageClass, validatorMethods);
        }

        List<MethodInvoker> list = new ArrayList<MethodInvoker>();
        for (int i = 0; i < validatorMethods.length; i++) {
            list.add(createValidator(validatorMethods[i], actionParameters));
        }

        return list.toArray(new MethodInvoker[0]);
    }

    @SuppressWarnings("deprecation")
    String[] getValidatorValue(Method method) {
        Validator validator = annotationHandler_.getAnnotation(method,
                Validator.class);
        if (validator != null) {
            return validator.value();
        }

        // 後方互換性のため。
        org.seasar.ymir.annotation.Validator validator2 = annotationHandler_
                .getAnnotation(method,
                        org.seasar.ymir.annotation.Validator.class);
        if (validator2 != null) {
            return validator2.value();
        }

        return null;
    }

    MethodInvoker createValidator(Method method, Object[] actionParameters) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i < actionParameters.length) {
                parameters[i] = actionParameters[i];
            } else {
                parameters[i] = getDefaultValue(parameterTypes[i]);
            }
        }
        return new MethodInvokerImpl(method, parameters);
    }

    Object getDefaultValue(Class<?> type) {
        if (type == Byte.TYPE) {
            return Byte.valueOf((byte) 0);
        } else if (type == Short.TYPE) {
            return Short.valueOf((short) 0);
        } else if (type == Integer.TYPE) {
            return Integer.valueOf(0);
        } else if (type == Long.TYPE) {
            return Long.valueOf(0l);
        } else if (type == Float.TYPE) {
            return Float.valueOf(0f);
        } else if (type == Double.TYPE) {
            return Double.valueOf(0d);
        } else if (type == Character.TYPE) {
            return Character.valueOf('\0');
        } else if (type == Boolean.TYPE) {
            return Boolean.FALSE;
        } else {
            return null;
        }
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
            Method[] methods = ClassUtils.getMethods(page,
                    ACTION_VALIDATIONFAILED);
            if (methods.length == 1) {
                return actionManager_.newAction(page, pageComponent
                        .getPageClass(), methods[0], new Object[] { notes_ });
            } else if (methods.length == 0) {
                return null;
            } else {
                throw new IllegalClientCodeRuntimeException("Method '"
                        + ACTION_VALIDATIONFAILED + "' must be single: class="
                        + pageComponent.getPageClass() + ", method="
                        + Arrays.asList(methods));
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
            Method[] methods = ClassUtils.getMethods(page,
                    ACTION_PERMISSIONDENIED);
            if (methods.length == 1) {
                return actionManager_.newAction(page, pageComponent
                        .getPageClass(), methods[0], new Object[] { ex_ });
            } else if (methods.length == 0) {
                return null;
            } else {
                throw new IllegalClientCodeRuntimeException("Method '"
                        + ACTION_PERMISSIONDENIED + "' must be single: class="
                        + pageComponent.getPageClass() + ", method="
                        + Arrays.asList(methods));
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
}
