package com.example.web;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.annotation.ConstraintHolder;
import org.seasar.ymir.constraint.annotation.Fufu;
import org.seasar.ymir.constraint.annotation.Required;
import org.seasar.ymir.constraint.annotation.SuppressConstraints;
import org.seasar.ymir.scope.annotation.Inject;

public class ConstraintInterceptorTest2Page {
    @ConstraintHolder
    @Required("saru")
    @Fufu("tora")
    public void constraints() {
    }

    @Required("fufu")
    @ConstraintHolder
    public boolean constraints2(@Inject
    Request request) {
        return request.getActionName().equals("_get_button2");
    }

    public void _get() {
    }

    @SuppressConstraints(ConstraintType.PERMISSION)
    public void _get_button1() {
    }

    public void _get_button2() {
    }
}
