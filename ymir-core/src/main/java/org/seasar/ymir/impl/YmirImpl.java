package org.seasar.ymir.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.cms.pluggable.Configuration;
import org.seasar.cms.pluggable.ThreadContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.Application;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.LifecycleListener;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseProcessor;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.convention.YmirNamingConvention;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.util.ArtifactMetaData;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.ServletUtils;
import org.seasar.ymir.util.YmirUtils;

public class YmirImpl implements Ymir {
    public static final String PARAM_METHOD = "__ymir__method";

    private static final String PATH_POM_PROPERTIES = "META-INF/maven/"
            + ClassUtils.getPackageName(Ymir.class)
            + "/ymir-core/pom.properties";

    private LifecycleListener[] lifecycleListeners_ = new LifecycleListener[0];

    private Configuration configuration_;

    private ApplicationManager applicationManager_;

    private YmirNamingConvention ymirNamingConvention_;

    private RequestProcessor requestProcessor_;

    private ResponseProcessor responseProcessor_;

    private ExceptionProcessor exceptionProcessor_;

    private YmirProcessInterceptor[] ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

    private final Log log_ = LogFactory.getLog(YmirImpl.class);

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

    @Binding(bindingType = BindingType.MUST)
    public void setYmirNamingConvention(
            YmirNamingConvention ymirNamingConvention) {
        ymirNamingConvention_ = ymirNamingConvention;
    }

    public void init() {
        String version = getVersion();
        if (version == null) {
            version = "unknown";
        }
        log_.info("Ymir (version: " + version + ") initialization start");

        initializeListeners();

        log_.info("Ymir has beena initialized");
    }

    void initializeListeners() {
        for (int i = 0; i < lifecycleListeners_.length; i++) {
            lifecycleListeners_[i].init();
        }
    }

    @SuppressWarnings("unchecked")
    public void process(ServletContext servletContext,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Dispatcher dispatcher, String path, HttpMethod method,
            Map<String, FormFile[]> fileParameterMap, FilterChain chain)
            throws IOException, ServletException {
        // 開発モードではResponseを加工できるように、マッチするかに関わらずYmirで処理するようにする。
        // また、この時点ではmethodは仮のものである（開発モードではHTTPメソッドが差し替えられることが
        // ある。proceedの場合はモードに依らずHTTPメソッドが差し替えられる）ため、ここで作成した
        // MatchedPathMappingはこの場で破棄して、HTTPメソッド差し替え後にMatchedPathMapping
        // を作成する必要がある。
        if (!isUnderDevelopment()) {
            if (findMatchedPathMapping(path, method) == null) {
                // マッチしないのでYmirでは処理しない。
                chain.doFilter(httpRequest, httpResponse);
                return;
            }
        }

        ThreadContext context = getThreadContext();
        AttributeContainer attributeContainer = new HttpServletRequestAttributeContainer(
                httpRequest);
        boolean backupped = false;
        Object backuppedObject = null;
        Response backuppedResponse = null;
        try {
            Response response = null;
            if (dispatcher == Dispatcher.REQUEST) {
                for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                    response = ymirProcessInterceptors_[i].enteringRequest(
                            servletContext, httpRequest, httpResponse, path);
                    if (response != null) {
                        break;
                    }
                }
            } else if (dispatcher == Dispatcher.INCLUDE) {
                backuppedObject = backupForInclusion(attributeContainer);
                backuppedResponse = (Response) context
                        .getComponent(Response.class);
                backupped = true;
            }

            Request request;
            if (dispatcher == Dispatcher.REQUEST) {
                request = prepareForProcessing(ServletUtils
                        .getContextPath(httpRequest), method, httpRequest
                        .getCharacterEncoding(), filterParameterMap(
                        httpRequest, path, dispatcher, httpRequest
                                .getParameterMap()), fileParameterMap,
                        attributeContainer);
                context.setComponent(Request.class, request);
                request.setAttribute(ATTR_REQUEST, request);
            } else {
                request = (Request) context.getComponent(Request.class);
                updateRequest(request, httpRequest, dispatcher);
            }

            try {
                enterDispatch(request, path, httpRequest, dispatcher);
                try {
                    try {
                        if (response == null) {
                            response = processRequest(request);
                        } else {
                            context.setComponent(Response.class, response);
                        }
                    } catch (Throwable t) {
                        response = processException(request, t);
                    }

                    try {
                        processResponse(servletContext, httpRequest,
                                httpResponse, request, response, chain);
                    } catch (Throwable t) {
                        if (dispatcher == Dispatcher.REQUEST) {
                            processResponse(servletContext, httpRequest,
                                    httpResponse, request, processException(
                                            request, t), chain);
                        } else {
                            rethrow(t);
                        }
                    }
                } finally {
                    leaveDispatch(request, dispatcher);

                    context.setComponent(Response.class, null);
                }
            } finally {
                for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                    ymirProcessInterceptors_[i].leftDispatch(request);
                }
            }
        } finally {
            if (dispatcher == Dispatcher.REQUEST) {
                context.setComponent(Request.class, null);

                for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                    ymirProcessInterceptors_[i].leftRequest();
                }
            } else if (dispatcher == Dispatcher.INCLUDE) {
                if (backupped) {
                    context.setComponent(Response.class, backuppedResponse);
                    restoreForInclusion(attributeContainer, backuppedObject);
                }
            }
        }
    }

    Map<String, String[]> filterParameterMap(HttpServletRequest httpRequest,
            String path, Dispatcher dispatcher,
            final Map<String, String[]> parameterMap) {
        if (ymirProcessInterceptors_.length == 0) {
            // 余計な処理を行なわないようにするため。
            return parameterMap;
        }

        Map<String, String[]> map = new HashMap<String, String[]>(parameterMap);
        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            ymirProcessInterceptors_[i].filterParameterMap(httpRequest, path,
                    dispatcher, map);
        }
        return Collections.unmodifiableMap(map);
    }

    protected void processResponse(ServletContext servletContext,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponseFilter responseFilter = responseProcessor_.process(
                servletContext, httpRequest, httpResponse, request, response);
        if (responseFilter != null) {
            chain.doFilter(httpRequest, responseFilter);
            responseFilter.commit();
        }
    }

    void rethrow(Throwable t) throws IOException, ServletException {
        if (t instanceof ServletException) {
            throw (ServletException) t;
        } else if (t instanceof IOException) {
            throw (IOException) t;
        } else if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        } else {
            throw new WrappingRuntimeException(t);
        }
    }

    protected ThreadContext getThreadContext() {
        return (ThreadContext) getApplication().getS2Container().getRoot()
                .getComponent(ThreadContext.class);
    }

    /**
     * HTTPリクエストを処理するための前準備を行ないます。
     * <p>必要な前準備を行ないRequestオブジェクトを構築します。
     * </p>
     * 
     * @param contextPath コンテキストパス。
     * @param method HTTPメソッド。
     * @param characterEncoding リクエストの文字エンコーディング。
     * @param parameterMap 文字列型のリクエストパラメータが格納されているMap。
     * nullを指定することもできます。
     * @param fileParameterMap FormFile型のリクエストパラメータが格納されているMap。
     * nullを指定することもできます。
     * @param attributeContainer リクエストスコープの属性を保持するための属性コンテナ。
     * @return 構築したRequestオブジェクト。
     */
    protected Request prepareForProcessing(final String contextPath,
            HttpMethod method, String characterEncoding,
            final Map<String, String[]> parameterMap,
            final Map<String, FormFile[]> fileParameterMap,
            final AttributeContainer attributeContainer) {
        if (isUnderDevelopment()) {
            method = correctMethod(method, parameterMap);
        }

        Request request = new RequestImpl(contextPath, method,
                characterEncoding, parameterMap, fileParameterMap,
                attributeContainer);
        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            request = ymirProcessInterceptors_[i].requestCreated(request);
        }
        return request;
    }

    HttpMethod correctMethod(final HttpMethod method,
            final Map<String, String[]> parameterMap) {
        final String[] values = parameterMap.get(PARAM_METHOD);
        if (values != null && values.length > 0) {
            return HttpMethod.valueOf(values[0]);
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
    /**
     * ディスパッチの処理を開始します。
     * 
     * @param request 現在のRequest。
     * @param path ディスパッチのパス。
     * @param httpRequest HTTPリクエスト。
     * @param dispatcher ディスパッチを表すDispatcher。
     * @see Dispatch
     * @see Dispatcher
     */
    protected void enterDispatch(Request request, String path,
            HttpServletRequest httpRequest, Dispatcher dispatcher) {
        MatchedPathMapping matched = findMatchedPathMapping(path, request
                .getMethod());
        if (dispatcher == Dispatcher.FORWARD) {
            Response response = (Response) request.getAttribute(ATTR_RESPONSE);
            if (!response.isSubordinate()) {
                // proceedされてきた場合。

                // 前のページの情報を削除しておく。
                // XXX ATTR_NOTESは残した方が便利な気がするので消していない。
                request.removeAttribute(RequestProcessor.ATTR_SELF);
                request.removeAttribute(RequestProcessor.ATTR_PAGECOMPONENT);

                // 自画面にproceedされたケースに重複して初期化してしまわないよう、
                // S2からPageコンポーネントを削除しておく。
                if (matched != null) {
                    String pageComponentName = matched.getPageComponentName();
                    if (pageComponentName != null) {
                        request.removeAttribute(pageComponentName);
                    }
                }
            }
        }

        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            ymirProcessInterceptors_[i].enteringDispatch(request, path,
                    dispatcher);
        }
        request.enterDispatch(new DispatchImpl(request.getContextPath(), path,
                request.getParameterMap(), request.getCharacterEncoding(),
                dispatcher, matched));
    }

    /**
     * 指定されたパスとHTTPメソッドに対応するMatchedPathMappingオブジェクトを構築して返します。
     * <p>マッチするPathMappingが見つからなかった場合はnullを返します。
     * </p>
     * 
     * @param path パス。末尾に「/」がついていてもついていなくても構いません。
     * @param method HTTPメソッド。
     * @return 構築したMatchedPathMappingオブジェクト。
     */
    public MatchedPathMapping findMatchedPathMapping(final String path,
            final HttpMethod method) {
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

    /**
     * ディスパッチの処理を終了します。
     * 
     * @param request 現在のRequest。
     * @param dispatcher 現在のDispatcher。
     * @see Dispatch
     * @see Dispatcher
     */
    protected void leaveDispatch(final Request request,
            final Dispatcher dispatcher) {
        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            ymirProcessInterceptors_[i].leavingDispatch(request);
        }
        if (dispatcher == Dispatcher.REQUEST) {
            for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                ymirProcessInterceptors_[i].leavingRequest(request);
            }
        }

        request.leaveDispatch();
    }

    protected Response processRequest(final Request request) {
        ThreadContext context = getThreadContext();

        Response response = requestProcessor_.process(request);
        context.setComponent(Response.class, response);

        for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
            response = ymirProcessInterceptors_[i].responseCreated(request,
                    response);
            context.setComponent(Response.class, response);
        }

        request.setAttribute(ATTR_RESPONSE, response);

        return response;
    }

    public void destroy() {
        log_.debug("Ymir destroy start");

        destroyListeners();
        requestProcessor_ = null;
        ymirProcessInterceptors_ = new YmirProcessInterceptor[0];

        log_.debug("Ymir destroy end");
    }

    void destroyListeners() {
        for (int i = 0; i < lifecycleListeners_.length; i++) {
            try {
                lifecycleListeners_[i].destroy();
            } catch (final Throwable t) {
                log_.warn("Can't destroy lifecycleListener: "
                        + lifecycleListeners_[i], t);
            }
        }
        lifecycleListeners_ = new LifecycleListener[0];
    }

    /**
     * サーブレットのインクルード処理を行なう前に属性を退避します。
     * 
     * @param attributeContainer コンテナ。
     * @return 退避した情報が格納されているオブジェクト。
     */
    protected Object backupForInclusion(
            final AttributeContainer attributeContainer) {
        // XXX Objectの配列というのもイマイチ…。
        return new Object[] {
            requestProcessor_.backupForInclusion(attributeContainer),
            getUnwrappedRequest().getParameterMap() };
    }

    /**
     * サーブレットのインクルード処理が終了した後に退避情報を復元します。
     * 
     * @param attributeContainer 退避情報の復元先である属性コンテナ。
     * @param backupped 退避した情報が格納されているオブジェクト。
     */
    @SuppressWarnings("unchecked")
    protected void restoreForInclusion(
            final AttributeContainer attributeContainer, final Object backupped) {
        Object[] backuppeds = (Object[]) backupped;
        getUnwrappedRequest().setQueryParameterMap(
                (Map<String, String[]>) backuppeds[1]);
        requestProcessor_
                .restoreForInclusion(attributeContainer, backuppeds[0]);
    }

    /**
     * 例外の処理をします。
     * 
     * @param request 現在のRequestオブジェクト。
     * @param t スローされた例外。
     * @return 処理結果のResponseオブジェクト。
     */
    protected Response processException(final Request request, final Throwable t) {
        Response response = exceptionProcessor_.process(request, t, false);

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

    /**
     * 指定されたディスパッチャの処理を開始するために、現在のRequestオブジェクトの状態を更新します。
     *  
     * @param request 現在のRequestオブジェクト。
     * @param httpRequest 現在のHttpServletRequestオブジェクト。
     * @param dispatcher ディスパッチャ。
     */
    @SuppressWarnings("unchecked")
    protected void updateRequest(Request request,
            HttpServletRequest httpRequest, Dispatcher dispatcher) {
        if (dispatcher != Dispatcher.FORWARD) {
            // REQUESTの時はRequestを更新する必要はない。
            // また、INCLUDEに関してはインクルード元Pageの処理の時に被インクルードPageのアクション呼び出し等が完了しているため、
            // ここでパラメータを調整しても仕方がないので何もしない。
            return;
        }

        RequestImpl unwrappedRequest = YmirUtils.unwrapRequest(request);
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
            parameterMap = filterParameterMap(httpRequest, path, dispatcher,
                    Collections.unmodifiableMap(parameterMap));

            unwrappedRequest.setMethod(HttpMethod.GET);
        }
        unwrappedRequest.setQueryParameterMap(parameterMap);
    }

    protected RequestImpl getUnwrappedRequest() {
        return YmirUtils.unwrapRequest(((Request) getApplication()
                .getS2Container().getComponent(Request.class)));
    }

    public String getPathOfPageClass(Class<?> pageClass) {
        return getPathOfPageClass(pageClass != null ? pageClass.getName()
                : null);
    }

    public String getPathOfPageClass(String pageClassName) {
        final PathMapping[] pathMappings = getPathMappings();
        if (pathMappings != null) {
            for (int i = 0; i < pathMappings.length; i++) {
                VariableResolver resolver = pathMappings[i]
                        .matchPageComponentName(ymirNamingConvention_
                                .fromClassNameToComponentName(pageClassName));
                if (resolver != null) {
                    return pathMappings[i].getPath(resolver);
                }
            }
        }
        return null;
    }

    public Class<?> getPageClassOfPath(String path) {
        MatchedPathMapping matched = findMatchedPathMapping(path,
                HttpMethod.GET);
        if (matched == null) {
            return null;
        } else {
            String componentName = matched.getPageComponentName();
            S2Container s2container = getApplication().getS2Container();
            if (s2container.hasComponentDef(componentName)) {
                return s2container.getComponentDef(componentName)
                        .getComponentClass();
            } else {
                return null;
            }
        }
    }

    public String getPageClassNameOfPath(String path) {
        MatchedPathMapping matched = findMatchedPathMapping(path,
                HttpMethod.GET);
        if (matched == null) {
            return null;
        } else {
            return ymirNamingConvention_.fromComponentNameToClassName(matched
                    .getPageComponentName());
        }
    }

    public String getVersion() {
        ArtifactMetaData metaData = ArtifactMetaData.newInstance(getClass()
                .getClassLoader().getResource(PATH_POM_PROPERTIES));
        if (metaData == null) {
            return null;
        } else {
            return metaData.getVersion() + " (" + metaData.getTimestamp() + ")";
        }
    }
}
