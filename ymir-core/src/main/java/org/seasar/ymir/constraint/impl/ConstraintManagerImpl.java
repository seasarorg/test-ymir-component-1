package org.seasar.ymir.constraint.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.Request;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.cache.CacheManager;
import org.seasar.ymir.constraint.ConfirmationDecider;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintBag;
import org.seasar.ymir.constraint.ConstraintManager;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.constraint.annotation.ConstraintHolder;
import org.seasar.ymir.constraint.annotation.Validator;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.util.ClassUtils;

public class ConstraintManagerImpl implements ConstraintManager {
    private static final ConfirmationDecider DECIDER_ALWAYS = new ConfirmationDecider() {
        public boolean isConfirmed(Object page, Request request,
                ConstraintType type, Set<ConstraintType> suppressTypeSet) {
            return true;
        }
    };

    private static final ConfirmationDecider DECIDER_DEPENDS_ON_SUPPRESSTYPE = new ConfirmationDecider() {
        public boolean isConfirmed(Object page, Request request,
                ConstraintType type, Set<ConstraintType> suppressTypeSet) {
            return !suppressTypeSet.contains(type);
        }
    };

    private ActionManager actionManager_;

    private AnnotationHandler annotationHandler_;

    private ApplicationManager applicationManager_;

    private Map<AnnotatedElement, ConstraintBag<?>[]> bagsWithAlwaysDeciderMap_;

    private Map<AnnotatedElement, ConstraintBag<?>[]> bagsWithDependsOnSuppressTypeDeciderMap_;

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
        bagsWithAlwaysDeciderMap_ = cacheManager.newMap();
        bagsWithDependsOnSuppressTypeDeciderMap_ = cacheManager.newMap();
        validatorMethodsMap_ = cacheManager.newMap();
    }

    public ConfirmationDecider getAlwaysDecider() {
        return DECIDER_ALWAYS;
    }

    public ConfirmationDecider getDependsOnSuppressTypeDecider() {
        return DECIDER_DEPENDS_ON_SUPPRESSTYPE;
    }

    public void confirmConstraint(ConstraintBag<?>[] bags,
            Set<ConstraintType> suppressTypeSet, Object bean, Request request,
            Notes notes) throws PermissionDeniedException {
        for (int i = 0; i < bags.length; i++) {
            try {
                bags[i].confirm(bean, request, suppressTypeSet);
            } catch (PermissionDeniedException ex) {
                throw ex;
            } catch (ValidationFailedException ex) {
                notes.add(ex.getNotes());
            } catch (ConstraintViolatedException ex) {
                throw new RuntimeException("May logic error", ex);
            }
        }
    }

    public void confirmConstraint(Class<?> beanClass,
            Set<ConstraintType> suppressTypeSet, Object bean, Request request,
            Notes notes) throws PermissionDeniedException {
        // クラスとプロパティとConstraintHolderが付与されたメソッドに関連付けられている制約をチェックする。
        confirmConstraint(getConstraintBagsForClass(beanClass,
                DECIDER_DEPENDS_ON_SUPPRESSTYPE), suppressTypeSet, bean,
                request, notes);

        // Validatorアノテーションがついているメソッドを実行する。
        if (!suppressTypeSet.contains(ConstraintType.VALIDATION)) {
            Action[] validators = getValidators(bean, beanClass, request
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

    @SuppressWarnings("unchecked")
    public void confirmConstraint(Object component, Request request,
            Annotation annotation, AnnotatedElement element)
            throws ConstraintViolatedException {
        for (Annotation expanded : annotationHandler_.getMarkedAnnotations(
                ConstraintAnnotation.class, annotation)) {
            Class<? extends Constraint<?>> constraintClass = expanded
                    .annotationType().getAnnotation(ConstraintAnnotation.class)
                    .component();
            Constraint constraint = (Constraint) getS2Container().getComponent(
                    constraintClass);
            constraint.confirm(component, request, expanded, element);
        }
    }

    public ConstraintBag<?>[] getConstraintBagsForClass(Class<?> beanClass,
            ConfirmationDecider decider) {
        if (beanClass == null) {
            return CONSTRAINTBAGS_EMPTY;
        }

        Map<AnnotatedElement, ConstraintBag<?>[]> bagsMap = null;
        ConstraintBag<?>[] bags = null;
        if (decider == DECIDER_ALWAYS) {
            bagsMap = bagsWithAlwaysDeciderMap_;
        } else if (decider == DECIDER_DEPENDS_ON_SUPPRESSTYPE) {
            bagsMap = bagsWithDependsOnSuppressTypeDeciderMap_;
        }
        if (bagsMap != null) {
            bags = bagsMap.get(beanClass);
        }
        if (bags == null) {
            List<ConstraintBag<?>> list = new ArrayList<ConstraintBag<?>>();
            createConstraintBagsForClass(beanClass, decider, list);
            bags = list.toArray(CONSTRAINTBAGS_EMPTY);
        }
        if (bagsMap != null) {
            bagsMap.put(beanClass, bags);
        }
        return bags;
    }

    public ConstraintBag<?>[] getConstraintBags(AnnotatedElement element,
            ConfirmationDecider decider) {
        if (element == null) {
            return CONSTRAINTBAGS_EMPTY;
        }

        Map<AnnotatedElement, ConstraintBag<?>[]> bagsMap = null;
        ConstraintBag<?>[] bags = null;
        if (decider == DECIDER_ALWAYS) {
            bagsMap = bagsWithAlwaysDeciderMap_;
        } else if (decider == DECIDER_DEPENDS_ON_SUPPRESSTYPE) {
            bagsMap = bagsWithDependsOnSuppressTypeDeciderMap_;
        }
        if (bagsMap != null) {
            bags = bagsMap.get(element);
        }
        if (bags == null) {
            List<ConstraintBag<?>> list = new ArrayList<ConstraintBag<?>>();
            createConstraintBags(element, decider, list);
            bags = list.toArray(CONSTRAINTBAGS_EMPTY);
        }
        if (bagsMap != null) {
            bagsMap.put(element, bags);
        }
        return bags;
    }

    public void getConstraintBags(AnnotatedElement element,
            ConfirmationDecider decider, List<ConstraintBag<?>> list) {
        if (element == null) {
            return;
        }

        Map<AnnotatedElement, ConstraintBag<?>[]> bagsMap = null;
        if (decider == DECIDER_ALWAYS) {
            bagsMap = bagsWithAlwaysDeciderMap_;
        } else if (decider == DECIDER_DEPENDS_ON_SUPPRESSTYPE) {
            bagsMap = bagsWithDependsOnSuppressTypeDeciderMap_;
        }
        if (bagsMap != null) {
            ConstraintBag<?>[] bags = bagsMap.get(element);
            if (bags == null) {
                List<ConstraintBag<?>> l = new ArrayList<ConstraintBag<?>>();
                createConstraintBags(element, decider, l);
                bags = l.toArray(CONSTRAINTBAGS_EMPTY);
                bagsMap.put(element, bags);
            }
            list.addAll(Arrays.asList(bags));
        } else {
            createConstraintBags(element, decider, list);
        }
    }

    protected Action[] getValidators(Object bean, Class<?> beanClass,
            Action action) {
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

        Key key = new Key(beanClass, actionName);
        Method[] validatorMethods = validatorMethodsMap_.get(key);
        if (validatorMethods == null) {
            List<Method> list = new ArrayList<Method>();
            for (Method method : ClassUtils.getMethods(beanClass)) {
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
            list.add(actionManager_.newAction(bean, beanClass,
                    validatorMethods[i], actionParameters));
        }

        return list.toArray(new Action[0]);
    }

    private String[] getValidatorValue(Method method) {
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

    private void createConstraintBagsForClass(Class<?> beanClass,
            ConfirmationDecider decider, List<ConstraintBag<?>> bags) {
        // クラス自体に付与されているアノテーションからConstraintBagを作成する。
        createConstraintBags(beanClass, decider, bags);

        // クラスが持つメンバに付与されているアノテーションからConstraintBagを作成する。
        createFromClassMembers(beanClass, decider, bags);

        // ConstraintHolderアノテーションが付与されているメソッドからConstraintBagを作成する。
        createFromConstraintHolders(beanClass, decider, bags);
    }

    protected void createFromClassMembers(Class<?> beanClass,
            ConfirmationDecider decider, List<ConstraintBag<?>> bags) {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(beanClass);
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }

        // クラスが持つプロパティのGetter/Setterに付与されているアノテーションからConstraintBagを作成する。
        // fieldは対象外。
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < pds.length; i++) {
            createConstraintBags(pds[i].getReadMethod(), decider, bags);
            createConstraintBags(pds[i].getWriteMethod(), decider, bags);
        }
    }

    protected void createFromConstraintHolders(Class<?> beanClass,
            ConfirmationDecider decider, List<ConstraintBag<?>> bags) {

        for (Method method : ClassUtils.getMethods(beanClass)) {
            if (annotationHandler_
                    .getAnnotation(method, ConstraintHolder.class) != null) {
                createConstraintBags(method, new MethodConfirmationDecider(
                        actionManager_, beanClass, method, decider), bags);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void createConstraintBags(AnnotatedElement element,
            ConfirmationDecider decider, List<ConstraintBag<?>> bags) {
        if (element == null) {
            return;
        }

        for (Annotation annotation : annotationHandler_.getMarkedAnnotations(
                element, ConstraintAnnotation.class)) {
            ConstraintAnnotation constraintAnnotation = annotation
                    .annotationType().getAnnotation(ConstraintAnnotation.class);
            bags.add(new ConstraintBag(((Constraint) getS2Container()
                    .getComponent(constraintAnnotation.component())),
                    annotation, element, decider));
        }
    }
}
