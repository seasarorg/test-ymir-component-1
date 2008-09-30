package org.seasar.ymir.impl;

import javax.servlet.http.HttpSession;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
import org.seasar.ymir.Request;
import org.seasar.ymir.Token;
import org.seasar.ymir.TokenManager;
import org.seasar.ymir.session.SessionManager;
import org.seasar.ymir.util.StringUtils;

public class TokenManagerImpl implements TokenManager {
    public static final String KEY_TOKEN = Globals.IDPREFIX + "token";

    private ApplicationManager applicationManager_;

    private SessionManager sessionManager_;

    private String tokenKey_ = KEY_TOKEN;

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setSessionManager(SessionManager sessionManager) {
        sessionManager_ = sessionManager;
    }

    public String getTokenKey() {
        return tokenKey_;
    }

    public void setTokenKey(String tokenKey) {
        tokenKey_ = tokenKey;
    }

    public Token newToken() {
        return new TokenImpl();
    }

    public String generateToken() {
        HttpSession session = sessionManager_.getSession();
        try {
            return StringUtils.getScopeKey(session.getId(), true);
        } catch (IllegalStateException ex) {
            return null;
        }
    }

    public String getToken(String tokenKey) {
        HttpSession session = sessionManager_.getSession(false);
        if (session == null) {
            return null;
        } else {
            synchronized (session.getId().intern()) {
                return (String) session.getAttribute(tokenKey);
            }
        }
    }

    public boolean isTokenValid(String tokenKey) {
        return isTokenValid(tokenKey, false);
    }

    public boolean isTokenValid(String tokenKey, boolean reset) {
        HttpSession session = sessionManager_.getSession(false);
        if (session == null) {
            return false;
        }

        synchronized (session.getId().intern()) {
            Object saved = session.getAttribute(tokenKey);
            if (saved == null) {
                return false;
            }
            if (reset) {
                resetToken(tokenKey);
            }

            String token = getRequest().getParameter(tokenKey);
            if (token == null) {
                return false;
            }

            return saved.equals(token);
        }
    }

    public void resetToken(String tokenKey) {
        HttpSession session = sessionManager_.getSession(false);
        if (session != null) {
            synchronized (session.getId().intern()) {
                session.removeAttribute(tokenKey);
            }
        }
    }

    public void saveToken(String tokenKey) {
        saveToken(tokenKey, true);
    }

    public void saveToken(String tokenKey, boolean force) {
        HttpSession session = sessionManager_.getSession(false);
        if (!force && session != null && session.getAttribute(tokenKey) != null) {
            return;
        }

        if (session == null) {
            session = sessionManager_.getSession();
        }

        synchronized (session.getId().intern()) {
            String token = generateToken();
            if (token != null) {
                session.setAttribute(tokenKey, token);
            }
        }
    }

    protected Request getRequest() {
        return (Request) applicationManager_.findContextApplication()
                .getS2Container().getComponent(Request.class);
    }

    protected class TokenImpl implements Token {
        public boolean exists() {
            return (getToken(getTokenKey()) != null);
        };

        public String getName() {
            return getTokenKey();
        }

        public String getValue() {
            saveToken(getTokenKey(), false);
            return getToken(getTokenKey());
        }
    }
}
