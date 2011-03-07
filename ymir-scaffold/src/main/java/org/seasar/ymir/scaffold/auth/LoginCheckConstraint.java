package org.seasar.ymir.scaffold.auth;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.impl.AbstractCrosscuttingConstraint;
import org.seasar.ymir.scaffold.SiteManager;
import org.seasar.ymir.scaffold.auth.annotation.Logined;
import org.seasar.ymir.scaffold.auth.annotation.SuppressLoginCheck;
import org.seasar.ymir.session.SessionManager;

public class LoginCheckConstraint extends AbstractCrosscuttingConstraint {
    @Binding(bindingType = BindingType.MUST)
    protected SessionManager sessionManager;

    public ConstraintType getConstraintType() {
        return ConstraintType.PERMISSION;
    }

    public void confirm(Object component, Request request)
            throws PermissionDeniedException, ValidationFailedException {
        Class<? extends Object> componentClass = component.getClass();
        if (componentClass.isAnnotationPresent(SuppressLoginCheck.class)) {
            // ログインチェックが抑制されている場合はOKとする。
            return;
        }

        LoginUser loginUser = (LoginUser) sessionManager
                .getAttribute(SiteManager.ATTR_LOGINUSER);
        if (loginUser != null) {
            Logined logined = componentClass.getAnnotation(Logined.class);
            if (logined == null || logined.value().length == 0) {
                // ロールの指定がない場合は、ログインしてさえいればOKとする。
                return;
            } else {
                if (loginUser.isInRole(logined.value())) {
                    return;
                }
            }
        }

        throw new PermissionDeniedException();
    }
}
