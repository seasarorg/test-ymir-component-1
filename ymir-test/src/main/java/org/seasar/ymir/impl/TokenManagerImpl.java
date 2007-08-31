package org.seasar.ymir.impl;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Globals;
import org.seasar.ymir.Token;
import org.seasar.ymir.TokenManager;
import org.seasar.ymir.util.TokenUtils;

public class TokenManagerImpl implements TokenManager {
    public static final String KEY_TOKEN = Globals.IDPREFIX + "token";

    private ApplicationManager applicationManager_;

    private String tokenKey_ = KEY_TOKEN;

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    public String getTokenKey() {
        return tokenKey_;
    }

    public void setTokenKey(String tokenKey) {
        tokenKey_ = tokenKey;
    }

    public Token newToken() {
        return new TokenImpl(getRequest());
    }

    public String generateToken() {
        return TokenUtils.generateToken(getRequest());
    }

    public String getToken(String tokenKey) {
        return TokenUtils.getToken(getRequest(), tokenKey);
    }

    public boolean isTokenValid(String tokenKey) {
        return TokenUtils.isTokenValid(getRequest(), tokenKey);
    }

    public boolean isTokenValid(String tokenKey, boolean reset) {
        return TokenUtils.isTokenValid(getRequest(), tokenKey, reset);
    }

    public void resetToken(String tokenKey) {
        TokenUtils.resetToken(getRequest(), tokenKey);
    }

    public void saveToken(String tokenKey) {
        TokenUtils.saveToken(getRequest(), tokenKey);
    }

    public void saveToken(String tokenKey, boolean force) {
        TokenUtils.saveToken(getRequest(), tokenKey, force);
    }

    HttpServletRequest getRequest() {
        return (HttpServletRequest) applicationManager_
                .findContextApplication().getS2Container().getComponent(
                        HttpServletRequest.class);
    }

    class TokenImpl implements Token {
        private HttpServletRequest request_;

        public TokenImpl(HttpServletRequest request) {
            request_ = request;
        }

        public boolean exists() {
            return (TokenUtils.getToken(request_, getTokenKey()) != null);
        };

        public String getName() {
            return getTokenKey();
        }

        public String getValue() {
            TokenUtils.saveToken(request_, getTokenKey(), false);
            return TokenUtils.getToken(request_, getTokenKey());
        }
    }
}
