package org.seasar.ymir.impl;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.seasar.cms.pluggable.Configuration;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.LifecycleListener;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PageNotFoundException;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseProcessor;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.util.YmirUtils;

public class YmirImpl implements Ymir {
    public static final String PARAM_METHOD = "__ymir__method";

    private LifecycleListener[] lifecycleListeners_ = new LifecycleListener[0];

    private Configuration configuration_;

    private ApplicationManager applicationManager_;

    private RequestProcessor requestProcessor_;

    private ResponseProcessor responseProcessor_;

    private ExceptionProcessor exceptionProcessor_;

    private YmirProcessInterceptor[] ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

    private final Logger logger_ = Logger.getLogger(getClass());

    public void setLifecycleListeners(
            final LifecycleListener[] lifecycleListeners) {
        lifecycleListeners_ = lifecycleListeners;
    }

    public void setRequestProcessor(final RequestProcessor requestProcessor) {
        requestProcessor_ = requestProcessor;
    }

    public void setResponseProcessor(final ResponseProcessor responseProcessor) {
        responseProcessor_ = responseProcessor;
    }

    public void setExceptionProcessor(
            final ExceptionProcessor exceptionProcessor) {
        exceptionProcessor_ = exceptionProcessor;
    }

    public void setYmirProcessInterceptors(
            final YmirProcessInterceptor[] ymirProcessInterceptors) {
        ymirProcessInterceptors_ = ymirProcessInterceptors;
        YmirUtils.sortYmirProcessInterceptors(ymirProcessInterceptors_);
    }

    public void setConfiguration(final Configuration configuration) {
        configuration_ = configuration;
    }

    public void setApplicationManager(
            final ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    public void init() {
        logger_.debug("Ymir initialize start");

        initializeListeners();

        DisposableUtil.add(new Disposable() {
            public void dispose() {
                PropertyUtils.clearDescriptors();
            }
        });

        logger_.debug("Ymir initialize end");
    }

    void initializeListeners() {
        for (int i = 0; i < lifecycleListeners_.length; i++) {
            lifecycleListeners_[i].init();
        }
    }

    public Request prepareForProcessing(final String contextPath,
            String method, final Map<String, String[]> parameterMap,
            final Map<String, FormFile[]> fileParameterMap,
            final AttributeContainer attributeContainer, final Locale locale) {
        if (isUnderDevelopment()) {
            method = correctMethod(method, parameterMap);
        }

        Request request = new RequestImpl(contextPath, method, parameterMap,
                fileParameterMap, attributeContainer, locale);
        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            request = ymirProcessInterceptors_[i].requestCreated(request);
        }
        return request;
    }

    String correctMethod(final String method, final Map parameterMap) {
        final String[] values = (String[]) parameterMap.get(PARAM_METHOD);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return method;
        }
    }

    /*
     * includeされているページで、リクエストパスを取得することもinclude対象のテンプレートのパスを取得することも両方できるようにするためです。
     * includeされているページで自分自身のパスを取るのは、servletAPI的にはrequestにあるキーでバインドされているパス情報を取ればいいのだけど、
     * それだとservletAPIを意識しないといけなくなるので、オブジェクトからその情報を取れるようにしました。
     */
    public void enterDispatch(final Request request, final String path,
            final String dispatcher) {
        request.enterDispatch(new DispatchImpl(request.getContextPath(), path,
                dispatcher, findMatchedPathMapping(path, request.getMethod())));
    }

    public MatchedPathMapping findMatchedPathMapping(final String path,
            final String method) {
        VariableResolver resolver = null;
        final PathMapping[] pathMappings = getPathMappings();
        if (pathMappings != null) {
            for (int i = 0; i < pathMappings.length; i++) {
                resolver = pathMappings[i].match(path, method);
                if (resolver != null) {
                    return new MatchedPathMappingImpl(pathMappings[i], resolver);
                }
            }
        }
        return null;
    }

    protected PathMapping[] getPathMappings() {
        return getApplication().getPathMappingProvider().getPathMappings();
    }

    public void leaveDispatch(final Request request) {
        request.leaveDispatch();
    }

    public Response processRequest(final Request request)
            throws PageNotFoundException, PermissionDeniedException {
        return requestProcessor_.process(request);
    }

    public HttpServletResponseFilter processResponse(
            final ServletContext servletContext,
            final HttpServletRequest httpRequest,
            final HttpServletResponse httpResponse, final Request request,
            final Response response) throws IOException, ServletException {
        return responseProcessor_.process(servletContext, httpRequest,
                httpResponse, request, response);
    }

    public void destroy() {
        logger_.debug("Ymir destroy start");

        destroyListeners();
        requestProcessor_ = null;
        ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

        logger_.debug("Ymir destroy end");
    }

    void destroyListeners() {
        for (int i = 0; i < lifecycleListeners_.length; i++) {
            try {
                lifecycleListeners_[i].destroy();
            } catch (final Throwable t) {
                logger_.error("Can't destroy lifecycleListener: "
                        + lifecycleListeners_[i], t);
            }
        }
        lifecycleListeners_ = new LifecycleListener[0];
    }

    public Object backupForInclusion(final AttributeContainer attributeContainer) {
        return requestProcessor_.backupForInclusion(attributeContainer);
    }

    public void restoreForInclusion(
            final AttributeContainer attributeContainer, final Object backupped) {
        requestProcessor_.restoreForInclusion(attributeContainer, backupped);
    }

    public Response processException(final Request request, final Throwable t) {
        return exceptionProcessor_.process(request, t);
    }

    public Application getApplication() {
        return applicationManager_.findContextApplication();
    }

    public String getProjectStatus() {
        if (configuration_ != null) {
            return configuration_.getProperty(Configuration.KEY_PROJECTSTATUS);
        } else {
            return null;
        }
    }

    public boolean isUnderDevelopment() {
        return Configuration.PROJECTSTATUS_DEVELOP.equals(getProjectStatus())
                && getApplication().isUnderDevelopment();
    }

    public YmirProcessInterceptor[] getYmirProcessInterceptors() {
        return ymirProcessInterceptors_;
    }
}
