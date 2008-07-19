package org.seasar.ymir.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.cms.pluggable.ThreadContext;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.log.Logger;
import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.Globals;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.LifecycleListener;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PageNotFoundException;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.RequestWrapper;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseProcessor;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.util.ServletUtils;
import org.seasar.ymir.util.YmirUtils;

public class YmirImpl implements Ymir {
    public static final String PARAM_METHOD = "__ymir__method";

    private static final String ATTR_RESPONSE = Globals.IDPREFIX
            + "impl.response";

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

    @Binding(bindingType = BindingType.MUST)
    public void setRequestProcessor(final RequestProcessor requestProcessor) {
        requestProcessor_ = requestProcessor;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setResponseProcessor(final ResponseProcessor responseProcessor) {
        responseProcessor_ = responseProcessor;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setExceptionProcessor(
            final ExceptionProcessor exceptionProcessor) {
        exceptionProcessor_ = exceptionProcessor;
    }

    @Binding(value = "@org.seasar.ymir.util.ContainerUtils@findAllComponents(container, @org.seasar.ymir.interceptor.YmirProcessInterceptor@class)", bindingType = BindingType.MUST)
    public void setYmirProcessInterceptors(
            final YmirProcessInterceptor[] ymirProcessInterceptors) {
        ymirProcessInterceptors_ = ymirProcessInterceptors;
        YmirUtils.sortYmirProcessInterceptors(ymirProcessInterceptors_);
    }

    @Binding(bindingType = BindingType.MUST)
    public void setConfiguration(final Configuration configuration) {
        configuration_ = configuration;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(
            final ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    public void init() {
        logger_.debug("Ymir initialize start");

        initializeListeners();

        logger_.debug("Ymir initialize end");
    }

    void initializeListeners() {
        for (int i = 0; i < lifecycleListeners_.length; i++) {
            lifecycleListeners_[i].init();
        }
    }

    public Request prepareForProcessing(final String contextPath,
            String method, String characterEncoding,
            final Map<String, String[]> parameterMap,
            final Map<String, FormFile[]> fileParameterMap,
            final AttributeContainer attributeContainer, final Locale locale) {
        if (isUnderDevelopment()) {
            method = correctMethod(method, parameterMap);
        }

        Request request = new RequestImpl(contextPath, method,
                characterEncoding, parameterMap, fileParameterMap,
                attributeContainer, locale);
        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            request = ymirProcessInterceptors_[i].requestCreated(request);
        }
        return request;
    }

    String correctMethod(final String method,
            final Map<String, String[]> parameterMap) {
        final String[] values = parameterMap.get(PARAM_METHOD);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return method;
        }
    }

    /*
     * dispatchの概念を追加したのは、例えばincludeされているページでリクエストパスを取得することも
     * include対象のテンプレートのパスを取得することも両方できるようにするためです。
     * includeされているページで自分自身のパスを取るのは、servletAPI的にはrequestにあるキーで
     * バインドされているパス情報を取ればいいのだけど、
     * それだとservletAPIを意識しないといけなくなるので、オブジェクトからその情報を取れるようにしました。
     */
    public void enterDispatch(Request request, String path, String queryString,
            Dispatcher dispatcher) {
        enterDispatch(request, path, queryString, dispatcher,
                findMatchedPathMapping(path, request.getMethod()));
    }

    public void enterDispatch(Request request, String path, String queryString,
            Dispatcher dispatcher, MatchedPathMapping matched) {
        if (matched == null) {
            matched = findMatchedPathMapping(path, request.getMethod());
        }
        // proceedされてきた場合はqueryStringとして適切な値が渡ってこないので、ここで
        // 差し替えている。
        if (dispatcher == Dispatcher.FORWARD) {
            Response response = (Response) request.getAttribute(ATTR_RESPONSE);
            if (!response.isSubordinate()) {
                queryString = ServletUtils.getQueryString(response.getPath());
            }
        }
        request.enterDispatch(new DispatchImpl(request.getContextPath(), path,
                queryString, dispatcher, matched));
    }

    public MatchedPathMapping findMatchedPathMapping(final String path,
            final String method) {
        String normalizedPath = ServletUtils.normalizePath(path);
        VariableResolver resolver = null;
        final PathMapping[] pathMappings = getPathMappings();
        if (pathMappings != null) {
            for (int i = 0; i < pathMappings.length; i++) {
                resolver = pathMappings[i].match(normalizedPath, method);
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
        Response response = requestProcessor_.process(request);
        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            response = ymirProcessInterceptors_[i].responseCreated(response);
        }
        request.setAttribute(ATTR_RESPONSE, response);
        return response;
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
        // FIXME Objectの配列というのもイマイチ…。
        return new Object[] {
            requestProcessor_.backupForInclusion(attributeContainer),
            unwrapRequest(
                    ((Request) getThreadContext().getComponent(Request.class)))
                    .getParameterMap() };
    }

    @SuppressWarnings("unchecked")
    public void restoreForInclusion(
            final AttributeContainer attributeContainer, final Object backupped) {
        Object[] backuppeds = (Object[]) backupped;
        unwrapRequest((Request) getThreadContext().getComponent(Request.class))
                .setParameterMap((Map<String, String[]>) backuppeds[1]);
        requestProcessor_
                .restoreForInclusion(attributeContainer, backuppeds[0]);
    }

    protected ThreadContext getThreadContext() {
        return (ThreadContext) getApplication().getS2Container().getRoot()
                .getComponent(ThreadContext.class);
    }

    public Response processException(final Request request, final Throwable t) {
        Response response = exceptionProcessor_.process(request, t);
        request.setAttribute(ATTR_RESPONSE, response);
        return response;
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

    @SuppressWarnings("unchecked")
    public void updateRequest(Request request, HttpServletRequest httpRequest,
            Dispatcher dispatcher) {
        if (dispatcher != Dispatcher.FORWARD) {
            return;
        }

        RequestImpl unwrappedRequest = unwrapRequest(request);
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        Response response = (Response) request.getAttribute(ATTR_RESPONSE);
        if (!response.isSubordinate()) {
            String path = response.getPath();
            int question = path.indexOf('?');
            if (question >= 0) {
                try {
                    parameterMap = ServletUtils.parseParameters(path
                            .substring(question + 1), request
                            .getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException("May framework's logic error",
                            ex);
                }
            } else {
                parameterMap = new HashMap<String, String[]>();
            }
            parameterMap = Collections.unmodifiableMap(parameterMap);

            unwrappedRequest.setMethod(Request.METHOD_GET);
        }
        unwrappedRequest.setParameterMap(parameterMap);
    }

    RequestImpl unwrapRequest(Request request) {
        while (request instanceof RequestWrapper) {
            request = ((RequestWrapper) request).getRequest();
        }
        try {
            return (RequestImpl) request;
        } catch (ClassCastException ex) {
            throw new RuntimeException(
                    "Must give the original Request instance or an instance of RequestWrapper to Ymir",
                    ex);
        }
    }
}
