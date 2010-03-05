package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.Set;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintBag;
import org.seasar.ymir.constraint.ConstraintManager;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.annotation.Satisfy;

/**
 * @since 1.0.7
 */
public class SatisfyConstraint implements Constraint<Satisfy> {
    private static final Set<ConstraintType> SUPPRESSTYPESET_EMPTY = Collections
            .emptySet();

    private ConstraintManager constraintManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setConstraintManager(ConstraintManager constraintManager) {
        constraintManager_ = constraintManager;
    }

    public void confirm(Object component, Request request, Satisfy annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        for (Class<?> beanClass : annotation.value()) {
            confirm(component, request, beanClass, element);
        }
    }

    private void confirm(Object component, Request request, Class<?> beanClass,
            AnnotatedElement element) throws ConstraintViolatedException {
        for (ConstraintBag<?> bag : constraintManager_
                .getConstraintBags(beanClass, constraintManager_
                        .getAlwaysDecider())) {
            bag.confirm(component, request, SUPPRESSTYPESET_EMPTY);
        }

    }
}
