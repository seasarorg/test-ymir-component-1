package org.seasar.ymir.redirection.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
import org.seasar.ymir.LifecycleListener;
import org.seasar.ymir.Request;
import org.seasar.ymir.redirection.RedirectionManager;
import org.seasar.ymir.redirection.ScopeIdManager;
import org.seasar.ymir.util.StringUtils;
import org.seasar.ymir.window.WindowManager;

public class RedirectionManagerImpl implements RedirectionManager,
        LifecycleListener {
    public static final String ATTRPREFIX_SCOPEMAP = Globals.IDPREFIX
            + "redirection.scopeMap.";

    protected ApplicationManager applicationManager_;

    private ScopeIdManager scopeIdManager_;

    private WindowManager windowManager_;

    @Binding(bindingType = BindingType.MUST)
    public final void setApplicationManager(
            ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setScopeIdManager(ScopeIdManager scopeIdManager) {
        scopeIdManager_ = scopeIdManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setWindowManager(WindowManager windowManager) {
        windowManager_ = windowManager;
    }

    public void init() {
        windowManager_.addStraddlingAttributeNamePattern(Pattern.quote(
                ATTRPREFIX_SCOPEMAP).concat(".*"));
    }

    public void destroy() {
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

    void setScopeMap(String windowId, String scopeId,
            Map<String, Object> scopeMap) {
        windowManager_.setScopeAttribute(windowId,
                getScopeMapAttributeKey(scopeId), scopeMap);
    }

    void removeScopeMap(String scopeId) {
        if (scopeId == null) {
            return;
        }
        windowManager_
                .setScopeAttribute(getScopeMapAttributeKey(scopeId), null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getScopeAttribute(String name) {
        String scopeId = getScopeId();
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
        String scopeId = getScopeId();
        if (scopeId == null) {
            return new ArrayList<String>().iterator();
        }

        Map<String, Object> scopeMap = getScopeMap(scopeId, false);
        if (scopeMap == null) {
            return new ArrayList<String>().iterator();
        }

        return scopeMap.keySet().iterator();
    }

    public String getScopeId() {
        return scopeIdManager_.getScopeId();
    }

    public void setScopeAttributeForNextRequest(String name, Object value) {
        String scopeId = getTemporaryScopeId();

        if (value != null) {
            getScopeMap(scopeId, true).put(name, value);
        } else {
            Map<String, Object> scopeMap = getScopeMap(scopeId, false);
            if (scopeMap == null) {
                return;
            }
            scopeMap.remove(name);
            if (scopeMap.isEmpty()) {
                removeScopeMap(scopeId);
            }
        }
    }

    String getTemporaryScopeId() {
        return StringUtils.getScopeKey(getRequest());
    }

    S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    Request getRequest() {
        return (Request) getS2Container().getComponent(Request.class);
    }

    public void clearScopeAttributes() {
        removeScopeMap(getScopeId());
    }

    public void populateScopeId() {
        String temporaryScopeId = getTemporaryScopeId();

        Map<String, Object> scopeMap = getScopeMap(temporaryScopeId, false);
        String scopeId = scopeIdManager_.populateScopeId(scopeMap != null);
        if (scopeMap != null) {
            removeScopeMap(temporaryScopeId);
            if (scopeId != null) {
                setScopeMap(windowManager_.findWindowIdForNextRequest(),
                        scopeId, scopeMap);
            }
        }
    }
}
