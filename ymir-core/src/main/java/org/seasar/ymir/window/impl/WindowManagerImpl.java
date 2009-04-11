package org.seasar.ymir.window.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
import org.seasar.ymir.Path;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.util.ResponseUtils;
import org.seasar.ymir.window.WindowManager;

public class WindowManagerImpl implements WindowManager {
    public static final String ATTRPREFIX_WINDOW = Globals.IDPREFIX + "window.";

    public static final String ATTRPREFIX_WINDOW_WINDOWID = ATTRPREFIX_WINDOW
            + "windowId.";

    private static final String DEFAULT_WINDOWID = "_self";

    private ApplicationManager applicationManager_;

    private SessionManager sessionManager_;

    private String windowIdKey_;

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

    /**
     * @deprecated app.propertiesを使って設定して下さい。
     */
    @Binding(bindingType = BindingType.MAY)
    public void setWindowIdKey(String windowIdKey) {
        windowIdKey_ = windowIdKey;
    }

    public String getWindowIdKey() {
        if (windowIdKey_ != null) {
            return windowIdKey_;
        } else {
            return applicationManager_.findContextApplication().getProperty(
                    APPKEY_CORE_WINDOW_KEY, DEFAULT_CORE_WINDOW_KEY);
        }
    }

    Map<String, Object> getScopeMap(String windowId, boolean create) {
        String key = getKey(windowId);
        @SuppressWarnings("unchecked")
        Map<String, Object> scopeMap = (Map<String, Object>) sessionManager_
                .getAttribute(key);
        if (scopeMap == null && create) {
            scopeMap = new HashMap<String, Object>();
            sessionManager_.setAttribute(key, scopeMap);
        }

        return scopeMap;
    }

    void removeScopeMap(String windowId) {
        sessionManager_.setAttribute(getKey(windowId), null);
    }

    String getKey(String windowId) {
        if (windowId == null) {
            return null;
        }
        return ATTRPREFIX_WINDOW_WINDOWID + windowId;
    }

    @SuppressWarnings("unchecked")
    public <T> T getScopeAttribute(String name) {
        // Sun JDKでエラーになるのを回避するため。
        Object value = getScopeAttribute(findWindowId(), name);
        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getScopeAttribute(String windowId, String name) {
        synchronized (windowId.intern()) {
            Map<String, Object> scopeMap = getScopeMap(windowId, false);
            if (scopeMap == null) {
                return null;
            }

            return (T) scopeMap.get(name);
        }
    }

    public Iterator<String> getScopeAttributeNames() {
        return getScopeAttributeNames(findWindowId());
    }

    public Iterator<String> getScopeAttributeNames(String windowId) {
        synchronized (windowId.intern()) {
            Map<String, Object> scopeMap = getScopeMap(windowId, false);
            if (scopeMap == null) {
                return new ArrayList<String>().iterator();
            }

            return scopeMap.keySet().iterator();
        }
    }

    public boolean existsWindowScope() {
        return existsWindowScope(findWindowId());
    }

    public boolean existsWindowScope(String windowId) {
        return sessionManager_.getSession(false) != null;
    }

    public String findWindowId() {
        String windowId = getWindowId();
        if (windowId == null) {
            windowId = DEFAULT_WINDOWID;
        }
        return windowId;
    }

    public String getWindowId() {
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

    public void setScopeAttribute(String name, Object value) {
        setScopeAttribute(findWindowId(), name, value);
    }

    public void setScopeAttribute(String windowId, String name, Object value) {
        synchronized (windowId.intern()) {
            if (value != null) {
                getScopeMap(windowId, true).put(name, value);
            } else {
                Map<String, Object> scopeMap = getScopeMap(windowId, false);
                if (scopeMap == null) {
                    return;
                }
                scopeMap.remove(name);
                if (scopeMap.isEmpty()) {
                    removeScopeMap(windowId);
                }
            }
        }
    }

    public String findWindowIdForNextRequest() {
        String windowId = getWindowIdForNextRequest();
        if (windowId == null) {
            windowId = DEFAULT_WINDOWID;
        }
        return windowId;
    }

    public String getWindowIdForNextRequest() {
        Response response = (Response) getS2Container().getComponent(
                Response.class);
        if (response != null
                && (ResponseUtils.isProceed(response) || response.getType() == ResponseType.REDIRECT)) {
            String[] value = new Path(response.getPath()).getParameterMap()
                    .get(getWindowIdKey());
            if (value != null && value.length > 0) {
                return value[0];
            } else {
                return null;
            }
        } else {
            return getWindowId();
        }
    }
}
