package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.NamedConstraint;
import org.seasar.ymir.constraint.annotation.Fufu;

public class FufuConstraint implements NamedConstraint<Fufu> {
    private String name_;

    public String getName() {
        return name_;
    }

    public void confirm(Object component, Request request, Fufu annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        name_ = annotation.value();
    }
}
