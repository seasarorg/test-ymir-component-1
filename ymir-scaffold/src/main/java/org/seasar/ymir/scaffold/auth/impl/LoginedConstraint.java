package org.seasar.ymir.scaffold.auth.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.scaffold.auth.LoginUser;
import org.seasar.ymir.scaffold.auth.annotation.Logined;
import org.seasar.ymir.session.SessionManager;

public class LoginedConstraint implements Constraint<Logined> {
    public static final String KEY_LOGINUSER = "loginUser";

    @Binding(bindingType = BindingType.MUST)
    protected SessionManager sessionManager;

    public void confirm(Object component, Request request, Logined annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        LoginUser loginUser = (LoginUser) sessionManager
                .getAttribute(KEY_LOGINUSER);
        if (loginUser != null) {
            if (annotation.value().length == 0) {
                // ロールの指定がない場合は、ログインしてさえいればOKとする。
                return;
            } else {
                if (loginUser.isInRole(annotation.value())) {
                    return;
                }
            }
        }

        throw new PermissionDeniedException();
    }
}
