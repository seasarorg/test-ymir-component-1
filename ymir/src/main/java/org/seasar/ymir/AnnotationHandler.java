package org.seasar.ymir;

import java.lang.reflect.Method;

import org.seasar.ymir.constraint.Constraint;

public interface AnnotationHandler {

    ScopeAttribute[] getInjectedScopeAttributes(Object component);

    ScopeAttribute[] getOutjectedScopeAttributes(Object component);

    Constraint[] getConstraints(Object component, Method action);
}
