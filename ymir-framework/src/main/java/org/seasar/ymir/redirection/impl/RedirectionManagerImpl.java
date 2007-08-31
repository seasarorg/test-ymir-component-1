package org.seasar.ymir.redirection.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
import org.seasar.ymir.Request;
import org.seasar.ymir.redirection.RedirectionManager;
import org.seasar.ymir.util.StringUtils;

public class RedirectionManagerImpl implements RedirectionManager {
    public static final String ATTRPREFIX_SCOPEMAP = Globals.IDPREFIX
            + "scope.redirectionScope.scopeMap.";

    public static final String KEY_SCOPEID = Globals.IDPREFIX + "redirection";

    private ApplicationManager applicationManager_;

    private boolean addScopeIdAsRequestParameter_;

    private String scopeIdKey_ = KEY_SCOPEID;

    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
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

    public Map<String, Object> getScopeMap() {
        return getScopeMap(true);
    }

    public Map<String, Object> getScopeMap(boolean create) {
        return getScopeMap(getScopeId(), create);
    }

    public Map<String, Object> getScopeMap(String scopeId) {
        return getScopeMap(scopeId, true);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getScopeMap(String scopeId, boolean create) {
        HttpSession session = getSession();
        String scopeMapAttr = getScopeMapId(scopeId);
        Map<String, Object> scopeMap = (Map<String, Object>) session
                .getAttribute(scopeMapAttr);
        if (scopeMap == null && create) {
            scopeMap = new HashMap<String, Object>();
            session.setAttribute(scopeMapAttr, scopeMap);
        }
        return scopeMap;
    }

    public void removeScopeMap() {
        removeScopeMap(getScopeId());
    }

    public void removeScopeMap(String scopeId) {
        if (scopeId == null) {
            return;
        }
        HttpSession session = getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(getScopeMapId(scopeId));
    }

    String getScopeMapId(String scopeId) {
        return ATTRPREFIX_SCOPEMAP + scopeId;
    }

    HttpSession getSession() {
        return getSession(true);
    }

    HttpServletRequest getRequest() {
        return (HttpServletRequest) getS2Container().getComponent(
                HttpServletRequest.class);
    }

    HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }

    public Object get(String name) {
        HttpSession session = getSession(false);
        if (session == null) {
            return null;
        }

        String scopeId = getScopeIdFromRequest();
        if (scopeId == null) {
            return null;
        }

        Map<String, Object> scopeMap = getScopeMap(scopeId, false);
        if (scopeMap == null) {
            return null;
        }

        return scopeMap.get(name);
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
}
