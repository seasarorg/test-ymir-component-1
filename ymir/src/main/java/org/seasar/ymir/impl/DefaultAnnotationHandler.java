package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.seasar.ymir.AnnotationHandler;
import org.seasar.ymir.ScopeAttribute;
import org.seasar.ymir.Constraint;

public class DefaultAnnotationHandler implements AnnotationHandler {

    public ScopeAttribute[] getInjectedScopeAttributes(Object component) {
        return new ScopeAttribute[0];
    }

    public ScopeAttribute[] getOutjectedScopeAttributes(Object component) {
        return new ScopeAttribute[0];
    }

    public Constraint[] getConstraints(Object component, Method action) {
        return new Constraint[0];
    }
}
