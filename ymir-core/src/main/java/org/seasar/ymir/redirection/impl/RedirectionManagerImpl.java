package org.seasar.ymir.redirection.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.ymir.Globals;
import org.seasar.ymir.redirection.RedirectionManager;
import org.seasar.ymir.util.StringUtils;

public class RedirectionManagerImpl implements RedirectionManager {
    public static final String ATTRPREFIX_SCOPEMAP = Globals.IDPREFIX
            + "scope.redirectionScope.scopeMap.";

    private S2Container container_;

    public void setContainer(S2Container container) {
        container_ = container;
    }

    public String getScopeKey() {
        return StringUtils.getScopeKey(getRequest());
    }

    public Map<String, Object> getScopeMap() {
        return getScopeMap(true);
    }

    public Map<String, Object> getScopeMap(boolean create) {
        return getScopeMap(getScopeKey(), create);
    }

    public Map<String, Object> getScopeMap(String scopeKey) {
        return getScopeMap(scopeKey, true);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getScopeMap(String scopeKey, boolean create) {
        HttpSession session = getSession();
        String scopeMapAttr = getScopeMapKey(scopeKey);
        Map<String, Object> scopeMap = (Map<String, Object>) session
                .getAttribute(scopeMapAttr);
        if (scopeMap == null && create) {
            scopeMap = new HashMap<String, Object>();
            session.setAttribute(scopeMapAttr, scopeMap);
        }
        return scopeMap;
    }

    public void removeScopeMap() {
        removeScopeMap(getScopeKey());
    }

    public void removeScopeMap(String scopeKey) {
        if (scopeKey == null) {
            return;
        }
        HttpSession session = getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(getScopeMapKey(scopeKey));
    }

    String getScopeMapKey(String scopeKey) {
        return ATTRPREFIX_SCOPEMAP + scopeKey;
    }

    ExternalContext getExternalContext() {
        return container_.getRoot().getExternalContext();
    }

    HttpSession getSession() {
        return getSession(true);
    }

    HttpServletRequest getRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }

    HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }
}
