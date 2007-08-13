package org.seasar.ymir.redirection.impl;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Request;
import org.seasar.ymir.redirection.RedirectionManager;
import org.seasar.ymir.scope.impl.AbstractServletScope;

public class RedirectionScope extends AbstractServletScope {
    private RedirectionManager redirectionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setRedirectionManager(RedirectionManager redirectionManager) {
        redirectionManager_ = redirectionManager;
    }

    @SuppressWarnings("unchecked")
    public Object getAttribute(String name) {
        HttpSession session = getSession(false);
        if (session == null) {
            return null;
        }

        String scopeKey = ((Request) container_.getComponent(Request.class))
                .getParameter(RedirectionManager.KEY_SCOPE);
        if (scopeKey == null) {
            return null;
        }

        Map<String, Object> scopeMap = redirectionManager_.getScopeMap(
                scopeKey, false);
        if (scopeMap == null) {
            return null;
        }

        return scopeMap.get(name);
    }

    public void setAttribute(String name, Object value) {
        if (value == null) {
            Map<String, Object> scopeMap = redirectionManager_
                    .getScopeMap(false);
            if (scopeMap != null) {
                scopeMap.remove(name);
            }
        } else {
            redirectionManager_.getScopeMap().put(name, value);
        }
    }
}
