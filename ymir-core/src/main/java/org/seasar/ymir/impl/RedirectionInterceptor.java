package org.seasar.ymir.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Request;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.redirection.RedirectionManager;

public class RedirectionInterceptor extends AbstractYmirProcessInterceptor {
    private RedirectionManager redirectionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setRedirectionManager(RedirectionManager redirectionManager) {
        redirectionManager_ = redirectionManager;
    }

    @Override
    public String encodingRedirectURL(String url) {
        if (redirectionManager_.getScopeMap(false) == null) {
            return url;
        }

        String scopeKeyParam;
        try {
            scopeKeyParam = RedirectionManager.KEY_SCOPE
                    + "="
                    + URLEncoder.encode(redirectionManager_.getScopeKey(),
                            "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }

        int question = url.indexOf('?');
        if (question < 0) {
            // /context/path 形式。
            return url + "?" + scopeKeyParam;
        } else if (question == url.length() - 1) {
            // /context/path? 形式。
            return url + scopeKeyParam;
        } else {
            // /context/path?a=b 形式。
            return url + "&" + scopeKeyParam;
        }
    }

    @Override
    public void leavingRequest(Request request) {
        redirectionManager_.removeScopeMap(request
                .getParameter(RedirectionManager.KEY_SCOPE));
    }
}
