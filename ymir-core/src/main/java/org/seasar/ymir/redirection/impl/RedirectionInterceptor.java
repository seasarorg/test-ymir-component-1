package org.seasar.ymir.redirection.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;
import org.seasar.ymir.redirection.RedirectionManager;

public class RedirectionInterceptor extends AbstractYmirProcessInterceptor {
    private RedirectionManager redirectionManager_;

    private ApplicationManager applicationManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setRedirectionManager(RedirectionManager redirectionManager) {
        redirectionManager_ = redirectionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Override
    public void responseProcessingStarted(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response) {
        if (!redirectionManager_.isAddScopeIdAsRequestParameter()) {
            // Cookieとしてキーを保存する。またキーが不要ならCookieを削除する。
            if (redirectionManager_.existsScopeMap()) {
                // redirectionScopeにオブジェクトが新たにバインドされているのでキーを保存する。
                Cookie cookie = newCookie(request, redirectionManager_
                        .getScopeIdKey(), redirectionManager_.getScopeId());
                httpResponse.addCookie(cookie);
            } else if (redirectionManager_.getScopeIdFromRequest() != null) {
                // redirectionScopeにオブジェクトが新たにバインドされていない、かつ
                // redirectionScopeのキーがリクエストに指定されているのでキーを削除する。
                Cookie cookie = newCookie(request, redirectionManager_
                        .getScopeIdKey(), "");
                cookie.setMaxAge(0);
                getHttpServletResponse().addCookie(cookie);
            }
        }
    }

    Cookie newCookie(Request request, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        String path = request.getContextPath();
        if (path.length() == 0) {
            path = "/";
        }
        cookie.setPath(path);

        return cookie;
    }

    @Override
    public String encodingRedirectURL(String url) {
        if (redirectionManager_.existsScopeMap()
                && redirectionManager_.isAddScopeIdAsRequestParameter()) {
            // リクエストパラメータとしてキーを保存する。
            String scopeKeyParam;
            try {
                scopeKeyParam = redirectionManager_.getScopeIdKey()
                        + "="
                        + URLEncoder.encode(redirectionManager_.getScopeId(),
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
        } else {
            return url;
        }
    }

    @Override
    public void leavingRequest(Request request) {
        String scopeKey = redirectionManager_.getScopeIdFromRequest();
        if (scopeKey != null) {
            // redirectionScopeのキーがリクエストに指定されているので対応するscopeMapを削除する。
            redirectionManager_.removeScopeMap(scopeKey);
        }
    }

    S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) getS2Container().getComponent(
                HttpServletResponse.class);
    }
}
