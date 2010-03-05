package org.seasar.ymir.constraint.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.constraint.ConfirmationDecider;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintBag;
import org.seasar.ymir.constraint.ConstraintBagCreator;
import org.seasar.ymir.constraint.ConstraintManager;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.constraint.annotation.ConstraintHolder;
import org.seasar.ymir.util.ClassUtils;

abstract public class AbstractConstraintBagCreator<T> implements
        ConstraintBagCreator<T> {
    protected ActionManager actionManager_;

    protected AnnotationHandler annotationHandler_;

    protected ApplicationManager applicationManager_;

    @Binding(bindingType = BindingType.MUST)
    public final void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public final void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST)
    public final void setApplicationManager(
            ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    public ConstraintBag<?>[] createConstraintBags(Class<T> beanClass,
            ConfirmationDecider decider) {
        if (beanClass == null) {
            return null;
        }

        List<ConstraintBag<?>> bags = new ArrayList<ConstraintBag<?>>();

        // クラス自体に付与されているアノテーションからConstraintBagを作成する。
        createFromClass(beanClass, decider, bags);

        // クラスが持つメンバに付与されているアノテーションからConstraintBagを作成する。
        createFromClassMembers(beanClass, decider, bags);

        // ConstraintHolderアノテーションが付与されているメソッドからConstraintBagを作成する。
        createFromConstraintHolders(beanClass, decider, bags);

        return bags.toArray(CONSTRAINTBAG_EMPTY);
    }

    protected void createFromClass(Class<T> beanClass,
            ConfirmationDecider decider, List<ConstraintBag<?>> bags) {
        createConstraintBags(beanClass, beanClass, decider, bags);
    }

    protected void createFromClassMembers(Class<T> beanClass,
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
            createConstraintBags(beanClass, pds[i].getReadMethod(), decider,
                    bags);
            createConstraintBags(beanClass, pds[i].getWriteMethod(), decider,
                    bags);
        }
    }

    protected void createFromConstraintHolders(Class<T> beanClass,
            ConfirmationDecider decider, List<ConstraintBag<?>> bags) {

        for (Method method : ClassUtils.getMethods(beanClass)) {
            if (annotationHandler_
                    .getAnnotation(method, ConstraintHolder.class) != null) {
                createConstraintBags(beanClass, method,
                        new MethodConfirmationDecider(actionManager_,
                                beanClass, method, decider), bags);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public boolean createConstraintBags(Class<T> beanClass,
            AnnotatedElement element, ConfirmationDecider decider,
            List<ConstraintBag<?>> bags) {
        if (element == null) {
            return false;
        }

        for (Annotation annotation : annotationHandler_.getMarkedAnnotations(
                element, ConstraintAnnotation.class)) {
            ConstraintAnnotation constraintAnnotation = annotation
                    .annotationType().getAnnotation(ConstraintAnnotation.class);
            bags.add(new ConstraintBag(((Constraint) getS2Container()
                    .getComponent(constraintAnnotation.component())),
                    annotation, element, decider));
        }

        return true;
    }

    protected S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }
}
