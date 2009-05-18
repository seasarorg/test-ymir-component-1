package org.seasar.ymir.constraint.impl;

import java.util.Set;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintBundle;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.annotation.Fuga;

import com.example.web.ConstraintInterceptorTestPage;

@SuppressWarnings("deprecation")
@Fuga("bundle")
public class FugaConstraintBundle implements ConstraintBundle {
    public boolean isConfirmed(Object page, Request request,
            ConstraintType type, Set<ConstraintType> suppressTypeSet) {
        return (page instanceof ConstraintInterceptorTestPage);
    }
}
