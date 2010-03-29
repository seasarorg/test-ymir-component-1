package com.example.web;

import org.seasar.ymir.constraint.annotation.ConstraintHolder;
import org.seasar.ymir.constraint.annotation.Required;

public class ConstraintInterceptorTest3Page {
    @ConstraintHolder
    @Required("saru")
    public boolean isProblem() {
        return false;
    }
}
