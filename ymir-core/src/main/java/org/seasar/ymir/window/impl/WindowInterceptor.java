package org.seasar.ymir.window.impl;

import static org.seasar.ymir.window.impl.WindowManagerImpl.ATTRPREFIX_WINDOW_WINDOWID;

import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Request;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.window.WindowManager;

public class WindowInterceptor extends AbstractYmirProcessInterceptor {
    private SessionManager sessionManager_;

    private WindowManager windowManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setSessionManager(SessionManager sessionManager) {
        sessionManager_ = sessionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setWindowManager(WindowManager windowManager) {
        windowManager_ = windowManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void leavingRequest(Request request) {
        // ScopeMapが不要になった場合に削除する。
        String key = ATTRPREFIX_WINDOW_WINDOWID
                + windowManager_.findWindowId();
        Map<String, Object> scopeMap = (Map<String, Object>) sessionManager_
                .getAttribute(key);
        if (scopeMap != null) {
            if (scopeMap.isEmpty()) {
                sessionManager_.removeAttribute(key);
            }
        }
    }
}
