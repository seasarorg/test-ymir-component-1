package org.seasar.ymir.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.util.BooleanConversionUtil;
import org.seasar.ymir.Response;
import org.seasar.ymir.util.LogUtils;

/**
 * リクエストの情報を出力するためのフィルタです。
 * <p><a href="http://teeda.seasar.org/">Teeda</a>のRequestDumpFilterを参考にしています。</p>
 * 
 * @author jflute
 */
public class RequestLoggingFilter implements Filter {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Log log_ = LogFactory
            .getLog(RequestLoggingFilter.class);

    private static final String LF = System.getProperty("line.separator");

    private static final String IND = "  ";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected FilterConfig config_ = null;

    protected boolean errorLogging_;

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    public void init(FilterConfig filterConfig) throws ServletException {
        config_ = filterConfig;
        errorLogging_ = getBooleanParameter(filterConfig, "errorLogging_", true);
    }

    protected boolean getBooleanParameter(FilterConfig filterConfig,
            String name, boolean defaultValue) {
        String value = filterConfig.getInitParameter(name);
        if (value == null) {
            return defaultValue;
        } else {
            return BooleanConversionUtil.toPrimitiveBoolean(value);
        }
    }

    // ===================================================================================
    //                                                                                Main
    //                                                                                ====
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest)) {
            String msg = "Request that is Not Http is unsupported: "
                    + servletRequest;
            throw new UnsupportedOperationException(msg);
        }
        if (!(servletResponse instanceof HttpServletResponse)) {
            String msg = "Response that is Not Http is unsupported: "
                    + servletResponse;
            throw new UnsupportedOperationException(msg);
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Long before = null;
        if (log_.isDebugEnabled()) {
            before(request, response);
            before = System.currentTimeMillis();
        }
        try {
            chain.doFilter(request, response);
        } catch (RuntimeException e) {
            logError("RuntimeException occurred at " + gerServletPath(request),
                    e);
            throw e;
        } catch (ServletException e) {
            logError("ServletException occurred at " + gerServletPath(request),
                    e.getRootCause());
            throw e;
        } catch (IOException e) {
            logError("IOException occurred at " + gerServletPath(request), e);
            throw e;
        } catch (Error e) {
            logError("Error occurred at " + gerServletPath(request), e);
            throw e;
        } finally {
            if (log_.isDebugEnabled()) {
                Long after = System.currentTimeMillis();
                after(request, response, before, after);
            }
        }
    }

    protected void before(HttpServletRequest request,
            HttpServletResponse response) {
        final StringBuilder sb = new StringBuilder();
        sb.append("* * * * * * * * * * {BEGIN}: " + gerServletPath(request))
                .append(LF);
        sb.append(IND).append("Request=").append(request.toString().trim())
                .append(LF);

        sb.append(IND).append("RequestedSessionId=").append(
                request.getRequestedSessionId()).append(LF);

        sb.append(IND).append("REQUEST_URI=").append(request.getRequestURI());
        sb.append(", SERVLET_PATH=").append(request.getServletPath())
                .append(LF);

        sb.append(IND).append(
                "CharacterEncoding=" + request.getCharacterEncoding());
        sb.append(", ContentLength=").append(request.getContentLength());
        sb.append(", ContentType=").append(request.getContentType());
        sb.append(", Locale=").append(request.getLocale());
        sb.append(", Locales=");
        final Enumeration<?> locales = request.getLocales();
        boolean first = true;
        while (locales.hasMoreElements()) {
            final Locale locale = (Locale) locales.nextElement();
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(locale.toString());
        }
        sb.append(", Scheme=").append(request.getScheme());
        sb.append(", isSecure=").append(request.isSecure()).append(LF);

        sb.append(IND).append("SERVER_PROTOCOL=").append(request.getProtocol());
        sb.append(", REMOTE_ADDR=").append(request.getRemoteAddr());
        sb.append(", REMOTE_HOST=").append(request.getRemoteHost());
        sb.append(", SERVER_NAME=").append(request.getServerName());
        sb.append(", SERVER_PORT=").append(request.getServerPort()).append(LF);

        sb.append(IND).append("ContextPath=").append(request.getContextPath());
        sb.append(", REQUEST_METHOD=").append(request.getMethod());
        sb.append(", PathInfo=").append(request.getPathInfo());
        sb.append(", RemoteUser=").append(request.getRemoteUser()).append(LF);

        sb.append(IND).append("QUERY_STRING=").append(request.getQueryString())
                .append(LF);

        buildRequestHeaders(sb, request);
        buildCookies(sb, request);
        buildRequestParameters(sb, request);
        buildSessionAttributes(sb, request);

        String logString = sb.toString();
        if (logString.endsWith(LF)) {
            logString = logString
                    .substring(0, logString.length() - LF.length());
        }
        log_.debug(logString);
    }

    protected void after(HttpServletRequest request,
            HttpServletResponse response, Long before, Long after) {
        final StringBuilder sb = new StringBuilder();
        sb.append(LF);

        sb.append(IND).append("Response=")
                .append(response.getClass().getName()).append(LF);
        sb.append(IND).append("CharacterEncoding=").append(
                response.getCharacterEncoding());
        sb.append(", ContentType=").append(response.getContentType());
        sb.append(", Locale=").append(response.getLocale());
        // It is possible that Response toString() show all HTML strings.
        // sb.append(", instance=").append(response.toString().trim());
        sb.append(LF);

        buildRequestAttributes(sb, request);
        buildSessionAttributes(sb, request);

        sb.append("* * * * * * * * * * {END}: ")
                .append(gerServletPath(request));
        sb.append(
                " ["
                        + convertToPerformanceView(after.longValue()
                                - before.longValue()) + "]").append(LF);
        sb.append(LF);

        String logString = sb.toString();
        log_.debug(logString);
    }

    protected String gerServletPath(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return ((HttpServletRequest) request).getServletPath();
        }
        return "";
    }

    protected void buildRequestHeaders(StringBuilder sb,
            HttpServletRequest request) {
        for (Iterator<?> it = toSortedSet(request.getHeaderNames()).iterator(); it
                .hasNext();) {
            String name = (String) it.next();
            String value = request.getHeader(name);
            sb.append(IND);
            sb.append("[header] ").append(name);
            sb.append("=").append(value);
            sb.append(LF);
        }
    }

    protected void buildCookies(StringBuilder sb, HttpServletRequest request) {
        final Cookie cookies[] = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (int i = 0; i < cookies.length; i++) {
            sb.append(IND);
            sb.append("[cookie] ").append(cookies[i].getName());
            sb.append("=").append(cookies[i].getValue());
            sb.append(LF);
        }
    }

    protected void buildRequestParameters(StringBuilder sb,
            HttpServletRequest request) {
        for (final Iterator<?> it = toSortedSet(request.getParameterNames())
                .iterator(); it.hasNext();) {
            final String name = (String) it.next();
            sb.append(IND);
            sb.append("[param] ").append(name).append("=");
            final String values[] = request.getParameterValues(name);
            for (int i = 0; i < values.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(values[i]);
            }
            sb.append(LF);
        }
    }

    protected void buildRequestAttributes(StringBuilder sb,
            HttpServletRequest request) {
        for (Iterator<?> it = toSortedSet(request.getAttributeNames())
                .iterator(); it.hasNext();) {
            String name = (String) it.next();
            Object attr = request.getAttribute(name);
            sb.append(IND);
            sb.append("[request] ").append(name).append("=").append(
                    LogUtils.addIndent(filterAttribute(attr), IND));
            sb.append(LF);
        }
    }

    protected void buildSessionAttributes(StringBuilder sb,
            HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        for (Iterator<?> it = toSortedSet(session.getAttributeNames())
                .iterator(); it.hasNext();) {
            final String name = (String) it.next();
            final Object attr = session.getAttribute(name);
            sb.append(IND);
            sb.append("[session] ").append(name).append("=").append(
                    LogUtils.addIndent(filterAttribute(attr), IND));
            sb.append(LF);
        }
    }

    protected Object filterAttribute(Object attribute) {
        if (attribute instanceof Response) {
            // Responseの実装クラスは場合によってはtoString()すると内容が全て吐き出されてしまったりすることがあり冗長なので出力しないようにする。
            return attribute.getClass().getName() + "@"
                    + System.identityHashCode(attribute);
        } else {
            return LogUtils.toString(attribute);
        }
    }

    protected void logError(String msg, Throwable e) {
        if (errorLogging_) {
            log_.error(msg, e);
        } else {
            log_.debug(msg, e);
        }
    }

    // ===================================================================================
    //                                                                             Destory
    //                                                                             =======
    public void destroy() {
        config_ = null;
    }

    // ===================================================================================
    //                                                                       Assist Helper
    //                                                                       =============
    @SuppressWarnings("unchecked")
    protected SortedSet toSortedSet(final Enumeration enu) {
        final SortedSet set = new TreeSet();
        set.addAll(Collections.list(enu));
        return set;
    }

    /**
     * Convert to performance view.
     * @param afterMinusBefore The value of after-minus-before millisecond.
     * @return Performance view. (ex. 1m23s456ms) (NotNull)
     */
    protected String convertToPerformanceView(long afterMinusBefore) { // from TraceViewUtil.java
        if (afterMinusBefore < 0) {
            return String.valueOf(afterMinusBefore);
        }

        long sec = afterMinusBefore / 1000;
        final long min = sec / 60;
        sec = sec % 60;
        final long mil = afterMinusBefore % 1000;

        final StringBuffer sb = new StringBuffer();
        if (min >= 10) { // Minute
            sb.append(min).append("m");
        } else if (min < 10 && min >= 0) {
            sb.append("0").append(min).append("m");
        }
        if (sec >= 10) { // Second
            sb.append(sec).append("s");
        } else if (sec < 10 && sec >= 0) {
            sb.append("0").append(sec).append("s");
        }
        if (mil >= 100) { // Millisecond
            sb.append(mil).append("ms");
        } else if (mil < 100 && mil >= 10) {
            sb.append("0").append(mil).append("ms");
        } else if (mil < 10 && mil >= 0) {
            sb.append("00").append(mil).append("ms");
        }

        return sb.toString();
    }
}
