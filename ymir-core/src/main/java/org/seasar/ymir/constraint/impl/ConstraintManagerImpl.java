package org.seasar.ymir.constraint.impl;

import static org.seasar.ymir.constraint.ConstraintBagCreator.CONSTRAINTBAG_EMPTY;

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
import org.seasar.ymir.constraint.ConstraintBagCreator;
import org.seasar.ymir.constraint.ConstraintManager;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
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

    private ConstraintBagCreator<?>[] constraintBagCreators_;

    private Map<Class<?>, List<ConstraintBagCreator<?>>> constraintBagCreatorMap_;

    private Map<ElementKey, ConstraintBag<?>[]> bagsWithAlwaysDeciderMap_;

    private Map<ElementKey, ConstraintBag<?>[]> bagsWithDependsOnSuppressTypeDeciderMap_;

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
    public void setCacheManager(CacheManager cacheManager) {
        bagsWithAlwaysDeciderMap_ = cacheManager.newMap();
        bagsWithDependsOnSuppressTypeDeciderMap_ = cacheManager.newMap();
        validatorMethodsMap_ = cacheManager.newMap();

        constraintBagCreatorMap_ = cacheManager.newMap();
        initializeConstraintBagCreatorMap();
    }

    @Binding(bindingType = BindingType.MUST)
    public void setConstraintBagCreators(
            ConstraintBagCreator<?>[] constraintBagCreators) {
        constraintBagCreators_ = constraintBagCreators;
        initializeConstraintBagCreatorMap();
    }

    private void initializeConstraintBagCreatorMap() {
        if (constraintBagCreatorMap_ == null || constraintBagCreators_ == null) {
            return;
        }

        for (ConstraintBagCreator<?> creator : constraintBagCreators_) {
            Class<?> clazz = creator.getTargetClass();
            List<ConstraintBagCreator<?>> list = constraintBagCreatorMap_
                    .get(clazz);
            if (list == null) {
                list = new ArrayList<ConstraintBagCreator<?>>();
                constraintBagCreatorMap_.put(clazz, list);
            }
            list.add(creator);
        }
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
        confirmConstraint(getConstraintBags(beanClass,
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

    public ConstraintBag<?>[] getConstraintBags(Class<?> beanClass,
            ConfirmationDecider decider) {
        if (beanClass == null) {
            return CONSTRAINTBAG_EMPTY;
        }

        Map<ElementKey, ConstraintBag<?>[]> bagsMap = null;
        ConstraintBag<?>[] bags = null;
        if (decider == DECIDER_ALWAYS) {
            bagsMap = bagsWithAlwaysDeciderMap_;
        } else if (decider == DECIDER_DEPENDS_ON_SUPPRESSTYPE) {
            bagsMap = bagsWithDependsOnSuppressTypeDeciderMap_;
        }
        ElementKey key = null;
        if (bagsMap != null) {
            key = new ElementKey(beanClass, null);
            bags = bagsMap.get(key);
        }
        if (bags == null) {
            bags = createConstraintBagsForClass(beanClass, decider);
        }
        if (bagsMap != null) {
            bagsMap.put(key, bags);
        }
        return bags;
    }

    @SuppressWarnings("unchecked")
    ConstraintBag[] createConstraintBagsForClass(Class<?> beanClass,
            ConfirmationDecider decider) {
        for (Class<?> clazz : ClassUtils.getAssignableClasses(beanClass)) {
            List creators = constraintBagCreatorMap_.get(clazz);
            if (creators != null) {
                for (ConstraintBagCreator c : (List<ConstraintBagCreator>) creators) {
                    ConstraintBag[] bags = c.createConstraintBags(beanClass,
                            decider);
                    if (bags != null) {
                        return bags;
                    }
                }
            }
        }
        return CONSTRAINTBAG_EMPTY;
    }

    public ConstraintBag<?>[] getConstraintBags(Class<?> beanClass,
            AnnotatedElement element, ConfirmationDecider decider) {
        if (beanClass == null || element == null) {
            return CONSTRAINTBAG_EMPTY;
        }

        Map<ElementKey, ConstraintBag<?>[]> bagsMap = null;
        ConstraintBag<?>[] bags = null;
        if (decider == DECIDER_ALWAYS) {
            bagsMap = bagsWithAlwaysDeciderMap_;
        } else if (decider == DECIDER_DEPENDS_ON_SUPPRESSTYPE) {
            bagsMap = bagsWithDependsOnSuppressTypeDeciderMap_;
        }
        ElementKey key = null;
        if (bagsMap != null) {
            key = new ElementKey(beanClass, element);
            bags = bagsMap.get(key);
        }
        if (bags == null) {
            List<ConstraintBag<?>> list = new ArrayList<ConstraintBag<?>>();
            createConstraintBags(beanClass, element, decider, list);
            bags = list.toArray(CONSTRAINTBAG_EMPTY);
        }
        if (bagsMap != null) {
            bagsMap.put(key, bags);
        }
        return bags;
    }

    public void getConstraintBags(Class<?> beanClass, AnnotatedElement element,
            ConfirmationDecider decider, List<ConstraintBag<?>> list) {
        if (beanClass == null || element == null) {
            return;
        }

        Map<ElementKey, ConstraintBag<?>[]> bagsMap = null;
        if (decider == DECIDER_ALWAYS) {
            bagsMap = bagsWithAlwaysDeciderMap_;
        } else if (decider == DECIDER_DEPENDS_ON_SUPPRESSTYPE) {
            bagsMap = bagsWithDependsOnSuppressTypeDeciderMap_;
        }
        if (bagsMap != null) {
            ElementKey key = new ElementKey(beanClass, element);
            ConstraintBag<?>[] bags = bagsMap.get(key);
            if (bags == null) {
                List<ConstraintBag<?>> l = new ArrayList<ConstraintBag<?>>();
                createConstraintBags(beanClass, element, decider, l);
                bags = l.toArray(CONSTRAINTBAG_EMPTY);
                bagsMap.put(key, bags);
            }
            list.addAll(Arrays.asList(bags));
        } else {
            createConstraintBags(beanClass, element, decider, list);
        }
    }

    @SuppressWarnings("unchecked")
    private void createConstraintBags(Class<?> beanClass,
            AnnotatedElement element, ConfirmationDecider decider,
            List<ConstraintBag<?>> list) {
        for (Class<?> clazz : ClassUtils.getAssignableClasses(beanClass)) {
            List creators = constraintBagCreatorMap_.get(clazz);
            if (creators != null) {
                for (ConstraintBagCreator c : (List<ConstraintBagCreator>) creators) {
                    if (c.createConstraintBags(beanClass, element, decider,
                            list)) {
                        return;
                    }
                }
            }
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

    protected static class ElementKey {
        private Class<?> beanClass_;

        private AnnotatedElement element_;

        public ElementKey(Class<?> beanClass, AnnotatedElement element) {
            beanClass_ = beanClass;
            element_ = element;
        }

        @Override
        public int hashCode() {
            int code = beanClass_.hashCode();
            if (element_ != null) {
                code += element_.hashCode();
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
            ElementKey o = (ElementKey) obj;
            if (o.element_ == null) {
                if (element_ != null) {
                    return false;
                }
            } else {
                if (!o.element_.equals(element_)) {
                    return false;
                }
            }
            if (o.beanClass_ != beanClass_) {
                return false;
            }
            return true;
        }
    }
}
