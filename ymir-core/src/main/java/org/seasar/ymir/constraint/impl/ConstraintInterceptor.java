package org.seasar.ymir.constraint.impl;

import static org.seasar.ymir.RequestProcessor.ATTR_NOTES;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.Notes;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.Request;
import org.seasar.ymir.TypeConversionManager;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.annotation.SuppressConstraints;
import org.seasar.ymir.annotation.Validator;
import org.seasar.ymir.constraint.ConfirmationDecider;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintBundle;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.constraint.annotation.ConstraintsAnnotation;
import org.seasar.ymir.impl.ActionImpl;
import org.seasar.ymir.impl.MethodInvokerImpl;
import org.seasar.ymir.impl.VoidMethodInvoker;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.MethodUtils;

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

    private ApplicationManager applicationManager_;

    private TypeConversionManager typeConversionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    @Override
    public Action actionInvoking(Request request, Action originalAction,
            Action action) throws PermissionDeniedException {
        PageComponent pageComponent = request.getCurrentDispatch()
                .getPageComponent();

        Action finalAction = null;
        PermissionDeniedException pde = null;
        try {
            Notes notes = confirmConstraint(pageComponent, request);
            if (notes != null) {
                request.setAttribute(ATTR_NOTES, notes);

                // バリデーションエラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
                // メソッドが存在しなければ何もしない（元のアクションメソッドの呼び出しをスキップする）。
                finalAction = (Action) pageComponent
                        .accept(new VisitorForFindingValidationFailedMethod(
                                notes));
                if (finalAction == null) {
                    finalAction = new ActionImpl(pageComponent.getPage(),
                            VoidMethodInvoker.INSTANCE);

                }
            } else {
                finalAction = action;
            }
        } catch (WrappingRuntimeException ex) {
            if (ex.getCause() instanceof PermissionDeniedException) {
                pde = (PermissionDeniedException) ex.getCause();
            } else {
                throw ex;
            }
        } catch (PermissionDeniedException ex) {
            pde = ex;
        }

        // 権限エラーが発生した場合は、エラー処理メソッドが存在すればそれを呼び出す。
        // メソッドが存在しなければPermissionDeniedExceptionを上に再スローする。
        if (pde != null) {
            finalAction = (Action) pageComponent
                    .accept(new VisitorForFindingPermissionDeniedMethod(pde));
            if (finalAction == null) {
                throw pde;
            }
        }

        return finalAction;
    }

    Notes confirmConstraint(PageComponent pageComponent, Request request)
            throws PermissionDeniedException {
        try {
            Action action = request.getCurrentDispatch().getAction();
            Method actionMethod = (action != null) ? action.getMethodInvoker()
                    .getMethod() : null;
            VisitorForConfirmingConstraint visitor = new VisitorForConfirmingConstraint(
                    request, getSuppressTypeSet(actionMethod));
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

    void confirmConstraint(Object page, Class<?> pageClass, Request request,
            Set<ConstraintType> suppressTypeSet, Notes notes)
            throws PermissionDeniedException {
        List<ConstraintBag<?>> list = new ArrayList<ConstraintBag<?>>();

        // 共通の制約を収集する。
        list.addAll(Arrays.asList(getConstraintBagsFromConstraintBundles()));

        // クラスとプロパティに関連付けられている制約を収集する。
        getConstraintBagsFromPageClass(pageClass, list);

        // アクションに関連付けられている制約を収集する。
        Action action = request.getCurrentDispatch().getAction();
        MethodInvoker actionMethodInvoker = null;
        if (action != null) {
            actionMethodInvoker = action.getMethodInvoker();
            if (actionMethodInvoker != null && page == action.getTarget()) {
                createConstraintBags(actionMethodInvoker.getMethod(),
                        ConstraintBag.DECIDER_ALWAYS, list);
            }
        }

        for (Iterator<ConstraintBag<?>> itr = list.iterator(); itr.hasNext();) {
            ConstraintBag<?> bag = itr.next();
            try {
                bag.confirm(page, request, suppressTypeSet);
            } catch (PermissionDeniedException ex) {
                throw ex;
            } catch (ValidationFailedException ex) {
                notes.add(ex.getNotes());
            } catch (ConstraintViolatedException ex) {
                throw new RuntimeException("May logic error", ex);
            }
        }

        // Validatorアノテーションがついているメソッドを実行する。
        MethodInvoker[] validators = gatherValidators(pageClass,
                actionMethodInvoker, suppressTypeSet);
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
                    notes.add(((ValidationFailedException) cause).getNotes());
                } else {
                    throw ex;
                }
            }
        }
    }

    Set<ConstraintType> getSuppressTypeSet(Method actionMethod) {
        Set<ConstraintType> suppressTypeSet = EnumSet
                .noneOf(ConstraintType.class);
        if (actionMethod != null) {
            SuppressConstraints suppress = actionMethod
                    .getAnnotation(SuppressConstraints.class);
            if (suppress != null) {
                ConstraintType[] types = suppress.value();
                for (int i = 0; i < types.length; i++) {
                    suppressTypeSet.add(types[i]);
                }
            }
        }
        return suppressTypeSet;
    }

    ConstraintBag<?>[] getConstraintBagsFromConstraintBundles() {
        // synchronizedしていないのは、たまたま同時に呼ばれて2回ConstraintBagが生成されてしまっても実害がないから。

        Application application = applicationManager_.findContextApplication();
        ConstraintBag<?>[] bags = application
                .getRelatedObject(ConstraintBag[].class);
        if (bags == null) {
            ComponentDef[] bundleCds = application.getS2Container()
                    .findAllComponentDefs(ConstraintBundle.class);
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

    // PropertyDescriptorのreadMethodは対象外。fieldも対象外。
    void getConstraintBagsFromPageClass(Class<?> pageClass,
            List<ConstraintBag<?>> list) {
        createConstraintBags(pageClass, DECIDER_DEPENDS_ON_SUPPRESSTYPE, list);
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(pageClass);
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < pds.length; i++) {
            createConstraintBags(pds[i].getWriteMethod(),
                    DECIDER_DEPENDS_ON_SUPPRESSTYPE, list);
        }
    }

    @SuppressWarnings("unchecked")
    void createConstraintBags(AnnotatedElement element,
            ConfirmationDecider decider, List<ConstraintBag<?>> list) {
        if (element == null) {
            return;
        }

        Annotation[] annotations = element.getAnnotations();
        List<Annotation> constraintAnnotationList = new ArrayList<Annotation>();
        for (int i = 0; i < annotations.length; i++) {
            Class<? extends Annotation> annotationType = annotations[i]
                    .annotationType();
            if (annotationType.isAnnotationPresent(ConstraintAnnotation.class)) {
                constraintAnnotationList.add(annotations[i]);
            } else if (annotationType
                    .isAnnotationPresent(ConstraintsAnnotation.class)) {
                Method method;
                try {
                    method = annotations[i].getClass().getMethod("value",
                            new Class[0]);
                } catch (SecurityException ex) {
                    throw new RuntimeException("Annotation " + annotationType
                            + " can be accessed to 'value' property", ex);
                } catch (NoSuchMethodException ex) {
                    throw new RuntimeException("Annotation " + annotationType
                            + " must have 'value' property", ex);
                }
                Annotation[] as;
                try {
                    as = (Annotation[]) method.invoke(annotations[i],
                            new Object[0]);
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException("Logic error", ex);
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException("Annotation " + annotationType
                            + " can be accessed to 'value' property", ex);
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
                for (int j = 0; j < as.length; j++) {
                    if (as[j].annotationType().isAnnotationPresent(
                            ConstraintAnnotation.class)) {
                        constraintAnnotationList.add(as[j]);
                    }
                }
            }
        }

        for (Iterator<Annotation> itr = constraintAnnotationList.iterator(); itr
                .hasNext();) {
            Annotation annotation = itr.next();

            ConstraintAnnotation constraintAnnotation = annotation
                    .annotationType().getAnnotation(ConstraintAnnotation.class);
            list.add(new ConstraintBag(((Constraint) getS2Container()
                    .getComponent(constraintAnnotation.component())),
                    annotation, element, decider));
        }
    }

    MethodInvoker[] gatherValidators(Class<?> pageClass,
            MethodInvoker actionMethodInvoker,
            Set<ConstraintType> suppressTypeSet) {
        List<MethodInvoker> validatorList = new ArrayList<MethodInvoker>();

        String actionName;
        Object[] actionParameters;
        if (actionMethodInvoker != null) {
            actionName = actionMethodInvoker.getMethod().getName();
            actionParameters = actionMethodInvoker.getParameters();
        } else {
            actionName = null;
            actionParameters = new Object[0];
        }

        // バリデーションを抑制するように指定されている場合はカスタムバリデータを収集しない。
        if (!suppressTypeSet.contains(ConstraintType.VALIDATION)) {
            Method[] methods = ClassUtils.getMethods(pageClass);
            for (int i = 0; i < methods.length; i++) {
                Validator validator = methods[i].getAnnotation(Validator.class);
                if (validator != null) {
                    String[] actionNames = validator.value();
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

                    validatorList.add(createValidatorMethodInvoker(methods[i],
                            actionParameters));
                }
            }
        }
        return validatorList.toArray(new MethodInvoker[0]);
    }

    MethodInvoker createValidatorMethodInvoker(Method method,
            Object[] actionParameters) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i < actionParameters.length) {
                parameters[i] = actionParameters[i];
            } else {
                parameters[i] = typeConversionManager_.convert(null,
                        parameterTypes[i]);
            }
        }
        return new MethodInvokerImpl(method, parameters);
    }

    protected S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    protected class VisitorForFindingValidationFailedMethod extends
            PageComponentVisitor {
        private Notes notes_;

        public VisitorForFindingValidationFailedMethod(Notes notes) {
            notes_ = notes;
        }

        public Object process(PageComponent pageComponent) {
            Object page = pageComponent.getPage();
            Method method = MethodUtils.getMethod(page,
                    ACTION_VALIDATIONFAILED, new Class[] { Notes.class });
            if (method != null) {
                return new ActionImpl(page, new MethodInvokerImpl(method,
                        new Object[] { notes_ }));
            } else {
                method = MethodUtils.getMethod(page, ACTION_VALIDATIONFAILED);
                if (method != null) {
                    return new ActionImpl(page, new MethodInvokerImpl(method,
                            new Object[0]));
                } else {
                    return null;
                }
            }
        }
    }

    protected class VisitorForFindingPermissionDeniedMethod extends
            PageComponentVisitor {
        private PermissionDeniedException ex_;

        public VisitorForFindingPermissionDeniedMethod(
                PermissionDeniedException ex) {
            ex_ = ex;
        }

        public Object process(PageComponent pageComponent) {
            Object page = pageComponent.getPage();
            Method method = MethodUtils.getMethod(page,
                    ACTION_PERMISSIONDENIED,
                    new Class[] { PermissionDeniedException.class });
            if (method != null) {
                return new ActionImpl(page, new MethodInvokerImpl(method,
                        new Object[] { ex_ }));
            } else {
                method = MethodUtils.getMethod(page, ACTION_PERMISSIONDENIED);
                if (method != null) {
                    return new ActionImpl(page, new MethodInvokerImpl(method,
                            new Object[0]));
                } else {
                    return null;
                }
            }
        }
    }

    protected class VisitorForConfirmingConstraint extends PageComponentVisitor {
        private Request request_;

        private Set<ConstraintType> suppressTypeSet_;

        private Notes notes_ = new Notes();

        public VisitorForConfirmingConstraint(Request request,
                Set<ConstraintType> supperssTypeSet) {
            request_ = request;
            suppressTypeSet_ = supperssTypeSet;
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
