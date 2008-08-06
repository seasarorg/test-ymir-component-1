package org.seasar.ymir.window.impl;

import static org.seasar.ymir.window.impl.WindowManagerImpl.ATTRPREFIX_WINDOW_WINDOWID;

import java.util.Map;

import javax.servlet.http.HttpSession;

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
        // クラスタリング環境でScopeMapが正しくレプリケートされるようにsetAttributeしなおす。
        // また、ScopeMapが不要になった場合に削除する。
        // ここで現在のリクエストに関するものだけを処理しているのは、現在のリクエストに関するもの以外は
        // 非同期に操作されている可能性があるためレプリケーション実施のために今のタイミングで
        // setAttribute()することが安全である保証がないから。
        HttpSession session = sessionManager_.getSession(false);
        if (session != null) {
            String key = ATTRPREFIX_WINDOW_WINDOWID
                    + windowManager_.findWindowIdFromRequest();
            Map<String, Object> scopeMap = (Map<String, Object>) session
                    .getAttribute(key);
            if (scopeMap != null) {
                if (scopeMap.isEmpty()) {
                    session.removeAttribute(key);
                } else {
                    session.setAttribute(key, scopeMap);
                }
            }
        }
    }
}
