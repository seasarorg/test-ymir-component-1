package org.seasar.ymir.redirection.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
import org.seasar.ymir.LifecycleListener;
import org.seasar.ymir.Request;
import org.seasar.ymir.redirection.RedirectionManager;
import org.seasar.ymir.util.StringUtils;
import org.seasar.ymir.window.WindowManager;

public class RedirectionManagerImpl implements RedirectionManager,
        LifecycleListener {
    public static final String ATTRPREFIX_SCOPEMAP = Globals.IDPREFIX
            + "redirection.scopeMap.";

    public static final String KEY_SCOPEID = Globals.IDPREFIX
            + "redirection.id";

    private ApplicationManager applicationManager_;

    private WindowManager windowManager_;

    private boolean addScopeIdAsRequestParameter_;

    private String scopeIdKey_ = KEY_SCOPEID;

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setWindowManager(WindowManager windowManager) {
        windowManager_ = windowManager;
    }

    public void init() {
        windowManager_.addStraddlingAttributeNamePattern(ATTRPREFIX_SCOPEMAP
                .replace(".", "\\.").concat(".*"));
    }

    public void destroy() {
    }

    public void setAddScopeIdAsRequestParameter(
            boolean addScopeIdAsRequestParameter) {
        addScopeIdAsRequestParameter_ = addScopeIdAsRequestParameter;
    }

    public boolean isAddScopeIdAsRequestParameter() {
        return addScopeIdAsRequestParameter_;
    }

    public void setScopeIdKey(String scopeIdKey) {
        scopeIdKey_ = scopeIdKey;
    }

    public String getScopeIdKey() {
        return scopeIdKey_;
    }

    public String getScopeId() {
        return StringUtils.getScopeKey((getS2Container()
                .getComponent(Request.class)));
    }

    String getScopeMapAttributeKey(String scopeId) {
        return ATTRPREFIX_SCOPEMAP + scopeId;
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> getScopeMap(String scopeId, boolean create) {
        String key = getScopeMapAttributeKey(scopeId);
        Map<String, Object> scopeMap = (Map<String, Object>) windowManager_
                .getScopeAttribute(key);
        if (scopeMap == null && create) {
            scopeMap = new HashMap<String, Object>();
            windowManager_.setScopeAttribute(key, scopeMap);
        }

        return scopeMap;
    }

    public void removeScopeMap() {
        removeScopeMap(getScopeId());
    }

    @SuppressWarnings("unchecked")
    public void removeScopeMap(String scopeId) {
        if (scopeId == null) {
            return;
        }
        windowManager_.removeScopeAttribute(getScopeMapAttributeKey(scopeId));
    }

    HttpServletRequest getRequest() {
        return (HttpServletRequest) getS2Container().getComponent(
                HttpServletRequest.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T getScopeAttribute(String name) {
        String scopeId = getScopeIdFromRequest();
        if (scopeId == null) {
            return null;
        }

        Map<String, Object> scopeMap = getScopeMap(scopeId, false);
        if (scopeMap == null) {
            return null;
        }

        return (T) scopeMap.get(name);
    }

    public Iterator<String> getScopeAttributeNames() {
        String scopeId = getScopeIdFromRequest();
        if (scopeId == null) {
            return new ArrayList<String>().iterator();
        }

        Map<String, Object> scopeMap = getScopeMap(scopeId, false);
        if (scopeMap == null) {
            return new ArrayList<String>().iterator();
        }

        return scopeMap.keySet().iterator();
    }

    public String getScopeIdFromRequest() {
        S2Container container = getS2Container();
        if (!addScopeIdAsRequestParameter_) {
            // Cookieから取り出す。
            HttpServletRequest httpRequest = (HttpServletRequest) container
                    .getComponent(HttpServletRequest.class);
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if (getScopeIdKey().equals(cookies[i].getName())) {
                        return cookies[i].getValue();
                    }
                }
            }
        }

        // リクエストパラメータから取り出す。
        return ((Request) container.getComponent(Request.class))
                .getParameter(getScopeIdKey());
    }

    S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    @SuppressWarnings("unchecked")
    public void setScopeAttribute(String name, Object value) {
        getScopeMap(getScopeId(), true).put(name, value);
    }

    @SuppressWarnings("unchecked")
    public void removeScopeAttribute(String name) {
        Map<String, Object> scopeMap = getScopeMap(getScopeId(), false);
        if (scopeMap == null) {
            return;
        }
        scopeMap.remove(name);
        if (scopeMap.isEmpty()) {
            removeScopeMap();
        }
    }

    public boolean existsScopeMap() {
        return getScopeMap(getScopeId(), false) != null;
    }
}
