package org.seasar.ymir.constraint;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Set;

import org.seasar.ymir.Request;
import org.seasar.ymir.message.Notes;

public interface ConstraintManager {
    ConfirmationDecider getAlwaysDecider();

    ConfirmationDecider getDependsOnSuppressTypeDecider();

    void confirmConstraint(ConstraintBag<?>[] bags,
            Set<ConstraintType> suppressTypeSet, Object bean, Request request,
            Notes notes) throws PermissionDeniedException;

    void confirmConstraint(Class<?> beanClass,
            Set<ConstraintType> suppressTypeSet, Object bean, Request request,
            Notes notes) throws PermissionDeniedException;

    ConstraintBag<?>[] getConstraintBags(Class<?> beanClass,
            ConfirmationDecider decider);

    ConstraintBag<?>[] getConstraintBags(Class<?> beanClass,
            AnnotatedElement element, ConfirmationDecider decider);

    void getConstraintBags(Class<?> beanClass, AnnotatedElement element,
            ConfirmationDecider decider, List<ConstraintBag<?>> bags);
}
