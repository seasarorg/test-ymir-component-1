package org.seasar.ymir.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.cms.pluggable.ThreadContext;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.MultipartServletRequest;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.WrappingRuntimeException;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.impl.HttpServletRequestAttributeContainer;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.util.LocaleUtils;
import org.seasar.ymir.util.ServletUtils;

public class YmirFilter implements Filter {
    private Dispatcher dispatcher_;

    private ServletContext context_;

    private Ymir ymir_;

    private YmirProcessInterceptor[] ymirProcessInterceptors_;

    public void init(FilterConfig config) throws ServletException {

        context_ = config.getServletContext();
        ymir_ = (Ymir) context_.getAttribute(YmirListener.ATTR_YMIR);
        ymirProcessInterceptors_ = ymir_.getYmirProcessInterceptors();

        String dispatcher = config.getInitParameter("dispatcher");
        if (dispatcher == null) {
            throw new ServletException(
                    "Init-param 'dispatcher' must be specified");
        }
        dispatcher_ = Dispatcher.valueOf(dispatcher.toUpperCase());
    }

    public void destroy() {

        context_ = null;
        ymir_ = null;
        ymirProcessInterceptors_ = null;
        dispatcher_ = null;
    }

    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;
        AttributeContainer attributeContainer = new HttpServletRequestAttributeContainer(
                httpRequest);

        ThreadContext context = getThreadContext();

        Object backupped = null;
        if (dispatcher_ == Dispatcher.INCLUDE) {
            backupped = ymir_.backupForInclusion(attributeContainer);
        }

        Request request;
        if (dispatcher_ == Dispatcher.REQUEST) {
            Map<String, FormFile[]> fileParameterMap = (Map<String, FormFile[]>) httpRequest
                    .getAttribute(MultipartServletRequest.ATTR_FORMFILEMAP);
            if (fileParameterMap != null) {
                httpRequest
                        .removeAttribute(MultipartServletRequest.ATTR_FORMFILEMAP);
            } else {
                fileParameterMap = new HashMap<String, FormFile[]>();
            }

            request = ymir_.prepareForProcessing(ServletUtils
                    .getContextPath(httpRequest), httpRequest.getMethod(),
                    httpRequest.getCharacterEncoding(), httpRequest
                            .getParameterMap(), fileParameterMap,
                    attributeContainer, LocaleUtils.findLocale(httpRequest));
            context.setComponent(Request.class, request);
        } else {
            request = (Request) context.getComponent(Request.class);
            ymir_.updateRequest(request, httpRequest);
        }

        ymir_.enterDispatch(request, ServletUtils.getPath(httpRequest),
                ServletUtils.getQueryString(httpRequest), dispatcher_);
        try {
            Response response = ymir_.processRequest(request);

            HttpServletResponseFilter responseFilter = ymir_.processResponse(
                    context_, httpRequest, httpResponse, request, response);
            if (responseFilter != null) {
                chain.doFilter(httpRequest, responseFilter);
                responseFilter.commit();
            }
        } catch (Throwable t) {
            if (dispatcher_ == Dispatcher.REQUEST) {
                ymir_.processResponse(context_, httpRequest, httpResponse,
                        request, ymir_.processException(request, t));
            } else {
                rethrow(t);
            }
        } finally {
            ymir_.leaveDispatch(request);

            if (dispatcher_ == Dispatcher.REQUEST) {
                for (int i = 0; i < ymirProcessInterceptors_.length; i++) {
                    ymirProcessInterceptors_[i].leavingRequest(request);
                }
                context.setComponent(Request.class, null);
            } else if (dispatcher_ == Dispatcher.INCLUDE) {
                ymir_.restoreForInclusion(attributeContainer, backupped);
            }
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
        return (ThreadContext) ymir_.getApplication().getS2Container()
                .getRoot().getComponent(ThreadContext.class);
    }
}
