package org.seasar.ymir.constraint.impl;

import java.lang.reflect.Method;
import java.util.Set;

import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConfirmationDecider;
import org.seasar.ymir.constraint.ConstraintType;

public class SuppressTypeAndMethodConfirmationDecider implements
        ConfirmationDecider {
    private ActionManager actionManager_;

    private Class<?> pageClass_;

    private Method method_;

    public SuppressTypeAndMethodConfirmationDecider(
            ActionManager actionManager, MethodInvoker methodInvoker) {
    }

    public SuppressTypeAndMethodConfirmationDecider(
            ActionManager actionManager, Class<?> pageClass, Method method) {
        actionManager_ = actionManager;
        pageClass_ = pageClass;
        if (method.getReturnType() == Boolean.TYPE) {
            method_ = method;
        } else if (method.getReturnType() != Void.TYPE) {
            throw new IllegalClientCodeRuntimeException(
                    "Return type must be boolean or void: method=" + method);
        }
    }

    public boolean isConfirmed(Object page, Request request,
            ConstraintType type, Set<ConstraintType> suppressTypeSet) {
        if (suppressTypeSet.contains(type)) {
            return false;
        } else if (method_ != null) {
            return ((Boolean) actionManager_.newAction(page, pageClass_,
                    method_).invoke()).booleanValue();
        } else {
            return true;
        }
    }
}
