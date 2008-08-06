package org.seasar.ymir.window.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
import org.seasar.ymir.Request;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.window.WindowManager;

public class WindowManagerImpl implements WindowManager {
    public static final String ATTRPREFIX_WINDOW = Globals.IDPREFIX + "window.";

    public static final String ATTRPREFIX_WINDOW_WINDOWID = ATTRPREFIX_WINDOW
            + "windowId.";

    public static final String KEY_WINDOWID = Globals.IDPREFIX + "window.id";

    private static final String DEFAULT_WINDOWID = "_self";

    private ApplicationManager applicationManager_;

    private SessionManager sessionManager_;

    private String windowIdKey_ = KEY_WINDOWID;

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setSessionManager(SessionManager sessionManager) {
        sessionManager_ = sessionManager;
    }

    public void addStraddlingAttributeNamePattern(String namePattern) {
        sessionManager_.addStraddlingAttributeNamePattern(namePattern);
    }

    public void setWindowIdKey(String windowIdKey) {
        windowIdKey_ = windowIdKey;
    }

    public String getWindowIdKey() {
        return windowIdKey_;
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> getScopeMap(String windowId, boolean create) {
        HttpSession session = getSession(create);
        if (session == null) {
            return null;
        }

        String key = getKey(windowId);
        Map<String, Object> scopeMap = (Map<String, Object>) session
                .getAttribute(key);
        if (scopeMap == null && create) {
            scopeMap = new HashMap<String, Object>();
            session.setAttribute(key, scopeMap);
        }

        return scopeMap;
    }

    String getKey(String windowId) {
        if (windowId == null) {
            return null;
        }
        return ATTRPREFIX_WINDOW_WINDOWID + windowId;
    }

    HttpServletRequest getRequest() {
        return (HttpServletRequest) getS2Container().getComponent(
                HttpServletRequest.class);
    }

    HttpSession getSession() {
        return getSession(true);
    }

    HttpSession getSession(boolean create) {
        return sessionManager_.getSession(create);
    }

    @SuppressWarnings("unchecked")
    public <T> T getScopeAttribute(String name) {
        Map<String, Object> scopeMap = getScopeMap(findWindowIdFromRequest(),
                false);
        if (scopeMap == null) {
            return null;
        }

        return (T) scopeMap.get(name);
    }

    public String findWindowIdFromRequest() {
        String windowId = getWindowIdFromRequest();
        if (windowId == null) {
            windowId = DEFAULT_WINDOWID;
        }
        return windowId;
    }

    public String getWindowIdFromRequest() {
        Request request = (Request) getS2Container()
                .getComponent(Request.class);
        if (request == null) {
            return null;
        } else {
            return request.getParameter(getWindowIdKey());
        }
    }

    S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    @SuppressWarnings("unchecked")
    public void setScopeAttribute(String name, Object value) {
        getScopeMap(findWindowIdFromRequest(), true).put(name, value);
    }

    @SuppressWarnings("unchecked")
    public void removeScopeAttribute(String name) {
        Map<String, Object> scopeMap = getScopeMap(findWindowIdFromRequest(),
                false);
        if (scopeMap == null) {
            return;
        }
        scopeMap.remove(name);
    }
}
