package org.seasar.ymir.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.Dispatcher;

/**
 * @author YOKOTA Takehiko
 */
public class ServletUtils {
    public static final String ATTR_INCLUDE_CONTEXT_PATH = "javax.servlet.include.context_path";

    public static final String ATTR_INCLUDE_PATH_INFO = "javax.servlet.include.path_info";

    public static final String ATTR_INCLUDE_SERVLET_PATH = "javax.servlet.include.servlet_path";

    public static final String ATTR_INCLUDE_QUERY_STRING = "javax.servlet.include.query_string";

    public static final String ATTR_FORWARD_CONTEXT_PATH = "javax.servlet.forward.context_path";

    public static final String ATTR_FORWARD_PATH_INFO = "javax.servlet.forward.path_info";

    public static final String ATTR_FORWARD_SERVLET_PATH = "javax.servlet.forward.servlet_path";

    public static final String ATTR_FORWARD_QUERY_STRING = "javax.servlet.forward.query_string";

    public static final String ATTR_ERROR_EXCEPTION = "javax.servlet.error.exception";

    private static final String PROTOCOL_HTTP = "http";

    private static final String PROTOCOL_HTTPS = "https";

    private static final int PORT_HTTP = 80;

    private static final int PORT_HTTPS = 443;

    private ServletUtils() {
    }

    public static String getRequestContextPath(HttpServletRequest request) {
        return request.getContextPath();
    }

    public static String getRequestPath(HttpServletRequest request) {
        String path = getNativeRequestPath(request);
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public static String getNativeRequestPath(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        sb.append(request.getServletPath());
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            sb.append(pathInfo);
        }
        return sb.toString();
    }

    public static String getContextPath(HttpServletRequest request) {
        String contextPath = getIncludeContextPath(request);
        if (contextPath == null) {
            contextPath = request.getContextPath();
        }
        return contextPath;
    }

    public static String getPath(HttpServletRequest request) {
        String path = getNativePath(request);
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public static String getQueryString(HttpServletRequest request) {
        if (getDispatcher(request) == Dispatcher.INCLUDE) {
            return getIncludeQueryString(request);
        } else {
            return request.getQueryString();
        }
    }

    public static Dispatcher getDispatcher(HttpServletRequest request) {
        if (request.getAttribute(ATTR_INCLUDE_CONTEXT_PATH) != null) {
            return Dispatcher.INCLUDE;
        } else if (request.getAttribute(ATTR_FORWARD_CONTEXT_PATH) != null) {
            return Dispatcher.FORWARD;
        } else if (request.getAttribute(ATTR_ERROR_EXCEPTION) != null) {
            return Dispatcher.ERROR;
        } else {
            return Dispatcher.REQUEST;
        }
    }

    static String getIncludeQueryString(HttpServletRequest request) {
        return null;
    }

    public static String getNativePath(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        String servletPath = getIncludeServletPath(request);
        String pathInfo;
        if (servletPath != null) {
            pathInfo = getIncludePathInfo(request);
        } else {
            servletPath = request.getServletPath();
            pathInfo = request.getPathInfo();
        }
        sb.append(servletPath);
        if (pathInfo != null) {
            sb.append(pathInfo);
        }
        return sb.toString();
    }

    public static String getIncludeContextPath(HttpServletRequest request) {
        return (String) request.getAttribute(ATTR_INCLUDE_CONTEXT_PATH);
    }

    public static String getIncludePathInfo(HttpServletRequest request) {
        return (String) request.getAttribute(ATTR_INCLUDE_PATH_INFO);
    }

    public static String getIncludeServletPath(HttpServletRequest request) {
        return (String) request.getAttribute(ATTR_INCLUDE_SERVLET_PATH);
    }

    public static String constructURI(String path, Map paramMap, String encoding)
            throws UnsupportedEncodingException {
        if (paramMap == null) {
            return path;
        }

        if (encoding == null) {
            encoding = "ISO-8859-1";
        }
        StringBuffer sb = new StringBuffer(path);
        String delim = "?";
        Iterator itr = paramMap.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (key == null || value == null) {
                continue;
            }
            String encodedKey = URLEncoder.encode(key, encoding);
            String[] values;
            if (value instanceof String[]) {
                values = (String[]) value;
            } else {
                values = new String[] { value.toString() };
            }
            for (int i = 0; i < values.length; i++) {
                sb.append(delim);
                delim = "&";
                sb.append(encodedKey);
                sb.append("=");
                sb.append(URLEncoder.encode(values[i], encoding));
            }
        }

        return sb.toString();
    }

    public static boolean isIncluded(HttpServletRequest request) {

        return (request.getAttribute(ATTR_INCLUDE_PATH_INFO) != null || request
                .getAttribute(ATTR_INCLUDE_SERVLET_PATH) != null);
    }

    public static void setNoCache(HttpServletResponse response) {
        if (response != null && !response.containsHeader("Cache-Control")) {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control",
                    "no-cache,no-store,must-revalidate");
            response.setDateHeader("Expires", 1);
        }
    }

    public static String constructRequestURL(HttpServletRequest request,
            String protocol, int port) {
        StringBuilder sb = new StringBuilder(256);
        sb.append(protocol).append("://");
        sb.append(request.getServerName());
        if (!(PROTOCOL_HTTP.equals(protocol) && port == PORT_HTTP || PROTOCOL_HTTPS
                .equals(protocol)
                && port == PORT_HTTPS)) {
            sb.append(':').append(port);
        }
        sb.append(request.getContextPath());
        sb.append(request.getServletPath());
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            sb.append(pathInfo);
        }
        String queryString = request.getQueryString();
        if (queryString != null) {
            sb.append('?').append(queryString);
        }
        return sb.toString();
    }

    public static Map<String, String[]> parseParameters(String param,
            String encoding) throws UnsupportedEncodingException {
        Map<String, String[]> map = new HashMap<String, String[]>();

        StringTokenizer st = new StringTokenizer(param, "&");
        while (st.hasMoreTokens()) {
            String tkn = st.nextToken();
            int equal = tkn.indexOf("=");
            String name;
            String value;
            if (equal >= 0) {
                name = tkn.substring(0, equal);
                value = tkn.substring(equal + 1);
            } else {
                name = tkn;
                value = "";
            }
            name = URLDecoder.decode(name, encoding);
            value = URLDecoder.decode(value, encoding);
            String[] current = map.get(name);
            if (current == null) {
                map.put(name, new String[] { value });
            } else {
                map.put(name, (String[]) ArrayUtil.add(current, value));
            }
        }

        return map;
    }
}
