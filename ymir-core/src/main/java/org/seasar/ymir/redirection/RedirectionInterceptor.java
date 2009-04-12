package org.seasar.ymir.redirection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;

public class RedirectionInterceptor extends AbstractYmirProcessInterceptor {
    private RedirectionManager redirectionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setRedirectionManager(RedirectionManager redirectionManager) {
        redirectionManager_ = redirectionManager;
    }

    @Override
    public void responseProcessingStarted(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response) {
        redirectionManager_.clearScopeAttributes();
        redirectionManager_.populateScopeId();
    }
}
