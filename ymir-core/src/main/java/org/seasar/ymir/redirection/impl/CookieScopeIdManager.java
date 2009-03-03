package org.seasar.ymir.redirection.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.redirection.ScopeIdManager;
import org.seasar.ymir.util.StringUtils;
import org.seasar.ymir.window.WindowManager;

public class CookieScopeIdManager extends AbstractScopeIdManager {
    private WindowManager windowManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setWindowManager(WindowManager windowManager) {
        windowManager_ = windowManager;
    }

    public String getScopeId() {
        String cookieName = getCookieName();
        Cookie[] cookies = getHttpServletRequest().getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookieName.equals(cookies[i].getName())) {
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }

    String getCookieName() {
        StringBuilder sb = new StringBuilder();
        sb.append(getScopeIdKey());
        String windowId = windowManager_.getWindowId();
        if (windowId != null) {
            sb.append(".").append(windowId);
        }
        return sb.toString();
    }

    public String populateScopeId(boolean scopeExists) {
        String scopeId = getScopeIdForNextRequest();

        // Cookieとしてキーを保存する。またキーが不要ならCookieを削除する。
        if (scopeExists) {
            Cookie cookie = newCookie(getRequest(), scopeId);
            getHttpServletResponse().addCookie(cookie);
        } else if (getScopeId() != null) {
            // redirectionScopeにオブジェクトが新たにバインドされていない、かつ
            // redirectionScopeのキーがリクエストに指定されていたのでキーを削除する。
            Cookie cookie = newCookie(getRequest(), "");
            cookie.setMaxAge(0);
            getHttpServletResponse().addCookie(cookie);
        }

        return scopeId;
    }

    Cookie newCookie(Request request, String value) {
        Cookie cookie = new Cookie(getCookieNameForNextRequest(), value);
        String path = request.getContextPath();
        if (path.length() == 0) {
            path = "/";
        }
        cookie.setPath(path);

        return cookie;
    }

    String getCookieNameForNextRequest() {
        StringBuilder sb = new StringBuilder();
        sb.append(getScopeIdKey());
        String windowId = windowManager_.getWindowIdForNextRequest();
        if (windowId != null) {
            sb.append(".").append(windowId);
        }
        return sb.toString();
    }
}
