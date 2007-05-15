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

import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.MultipartServletRequest;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.impl.HttpServletRequestAttributeContainer;
import org.seasar.ymir.util.LocaleUtils;
import org.seasar.ymir.util.ServletUtils;

public class YmirFilter implements Filter {

    private String dispatcher_;

    private ServletContext context_;

    private Ymir ymir_;

    public void init(FilterConfig config) throws ServletException {

        context_ = config.getServletContext();
        ymir_ = (Ymir) context_.getAttribute(YmirListener.ATTR_YMIR);

        String dispatcher = config.getInitParameter("dispatcher");
        if (dispatcher == null) {
            throw new ServletException(
                    "Init-param 'dispatcher' must be specified");
        }
        dispatcher_ = dispatcher.toUpperCase();
    }

    public void destroy() {

        context_ = null;
        ymir_ = null;
        dispatcher_ = null;
    }

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) res;

        Map fileParameterMap = (Map) httpRequest
                .getAttribute(MultipartServletRequest.ATTR_FORMFILEMAP);
        if (fileParameterMap != null) {
            httpRequest
                    .removeAttribute(MultipartServletRequest.ATTR_FORMFILEMAP);
        } else {
            fileParameterMap = new HashMap();
        }
        AttributeContainer attributeContainer = new HttpServletRequestAttributeContainer(
                httpRequest);

        Object backupped = null;
        if (Request.DISPATCHER_INCLUDE.equals(dispatcher_)) {
            backupped = ymir_.backupForInclusion(attributeContainer);
        }

        Request request = ymir_.prepareForProcessing(ServletUtils
                .getContextPath(httpRequest),
                ServletUtils.getPath(httpRequest), httpRequest.getMethod(),
                dispatcher_, httpRequest.getParameterMap(), fileParameterMap,
                attributeContainer, LocaleUtils.findLocale(httpRequest));
        try {
            Response response = ymir_.processRequest(request);
            HttpServletResponseFilter responseFilter = ymir_.processResponse(
                    context_, httpRequest, httpResponse, request, response);
            if (responseFilter != null) {
                chain.doFilter(httpRequest, responseFilter);
                responseFilter.commit();
            }
        } catch (Throwable t) {
            ymir_.processResponse(context_, httpRequest, httpResponse, request,
                    ymir_.processException(request, t));
        } finally {
            if (Request.DISPATCHER_INCLUDE.equals(dispatcher_)) {
                ymir_.restoreForInclusion(attributeContainer, backupped);
            }
        }
    }
}
