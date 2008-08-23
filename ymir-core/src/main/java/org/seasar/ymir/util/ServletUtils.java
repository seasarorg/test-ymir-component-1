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
 * サーブレット関連のユーティリティメソッドを提供するクラスです。
 * 
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

    public static final String SCHEME_HTTP = "http";

    public static final String SCHEME_HTTPS = "https";

    public static final int PORT_HTTP = 80;

    public static final int PORT_HTTPS = 443;

    private static final String PROTOCOL_DOMAIN_DELIMITER = "://";

    private ServletUtils() {
    }

    /**
     * リクエストされたURLのコンテキストパス部分を返します。
     * <p>リクエストがforwardやincludeの処理中であっても元々のパスを返します。
     * </p>
     * 
     * @param request リクエスト。
     * @return リクエストされたURLのコンテキストパス部分。
     */
    public static String getRequestContextPath(HttpServletRequest request) {
        if (wasForwareded(request)) {
            return (String) request.getAttribute(ATTR_FORWARD_CONTEXT_PATH);
        } else {
            return request.getContextPath();
        }
    }

    /**
     * ディスパッチされたURLのコンテキストパス部分を返します。
     * <p>通常はリクエストされたパス、リクエストがforwardの処理中である場合はforward先のパス、
     * リクエストがincludeの処理中である場合はincludeしたページのパスを返します。
     * </p>
     * 
     * @param request リクエスト。
     * @return ディスパッチされたURLのコンテキストパス部分。
     */
    public static String getContextPath(HttpServletRequest request) {
        Dispatcher dispatcher = getDispatcher(request);
        if (dispatcher == Dispatcher.INCLUDE) {
            return (String) request.getAttribute(ATTR_INCLUDE_CONTEXT_PATH);
        } else if (dispatcher == Dispatcher.FORWARD) {
            return request.getContextPath();
        } else {
            String contextPath = (String) request
                    .getAttribute(ATTR_FORWARD_CONTEXT_PATH);
            // 念のため。
            if (contextPath == null) {
                contextPath = request.getContextPath();
            }
            return contextPath;
        }
    }

    /**
     * リクエストされたURLのコンテキスト相対パス部分を返します。
     * <p>リクエストがforwardやincludeの処理中であっても元々のパスを返します。
     * </p>
     * <p>パスの末尾に「/」がついている場合は取り除かれます。
     * </p>
     * 
     * @param request リクエスト。
     * @return リクエストされたURLのコンテキスト相対パス部分。
     * @see #getNativeRequestPath(HttpServletRequest)
     */
    public static String getRequestPath(HttpServletRequest request) {
        String path = getNativeRequestPath(request);
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    /**
     * ディスパッチされたURLのURLのコンテキスト相対パス部分を返します。
     * <p>通常はリクエストされたパス、リクエストがforwardの処理中である場合はforward先のパス、
     * リクエストがincludeの処理中である場合はincludeしたページのパスを返します。
     * </p>
     * <p>パスの末尾に「/」がついている場合は取り除かれます。
     * </p>
     * 
     * @param request リクエスト。
     * @return ディスパッチされたURLのURLのコンテキスト相対パス部分。
     */
    public static String getPath(HttpServletRequest request) {
        String path = getNativePath(request);
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    /**
     * リクエストされたURLのコンテキスト相対パス部分を返します。
     * <p>リクエストがforwardやincludeの処理中であっても元々のパスを返します。
     * </p>
     * <p>パスの末尾に「/」がついていてもそのまま返します。
     * </p>
     * 
     * @param request リクエスト。
     * @return リクエストされたURLのコンテキスト相対パス部分。
     * @see #getRequestPath(HttpServletRequest)
     */
    public static String getNativeRequestPath(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        String servletPath;
        String pathInfo;
        if (wasForwareded(request)) {
            servletPath = (String) request
                    .getAttribute(ATTR_FORWARD_SERVLET_PATH);
            pathInfo = (String) request.getAttribute(ATTR_FORWARD_PATH_INFO);
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

    /**
     * ディスパッチされたURLのURLのコンテキスト相対パス部分を返します。
     * <p>通常はリクエストされたパス、リクエストがforwardの処理中である場合はforward先のパス、
     * リクエストがincludeの処理中である場合はincludeしたページのパスを返します。
     * </p>
     * <p>パスの末尾に「/」がついていてもそのまま返します。
     * </p>
     * 
     * @param request リクエスト。
     * @return ディスパッチされたURLのURLのコンテキスト相対パス部分。
     */
    public static String getNativePath(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        String servletPath;
        String pathInfo;
        Dispatcher dispatcher = getDispatcher(request);
        if (dispatcher == Dispatcher.INCLUDE) {
            servletPath = (String) request
                    .getAttribute(ATTR_INCLUDE_SERVLET_PATH);
            pathInfo = (String) request.getAttribute(ATTR_INCLUDE_PATH_INFO);
        } else if (dispatcher == Dispatcher.FORWARD) {
            servletPath = request.getServletPath();
            pathInfo = request.getPathInfo();
        } else {
            servletPath = (String) request
                    .getAttribute(ATTR_FORWARD_SERVLET_PATH);
            // 念のため。
            if (servletPath == null) {
                servletPath = request.getServletPath();
            }
            pathInfo = (String) request.getAttribute(ATTR_FORWARD_PATH_INFO);
            // 念のため。
            if (pathInfo == null) {
                pathInfo = request.getPathInfo();
            }
        }
        sb.append(servletPath);
        if (pathInfo != null) {
            sb.append(pathInfo);
        }
        return sb.toString();
    }

    /**
     * パスを正規化します。
     * <p><code>path</code>がnullの場合はnullを返します。
     * </p>
     * <p><code>path</code>の末尾に「/」がついている場合は取り除いたパスを返します。
     * </p>
     * 
     * @param path パス。
     * @return 正規化されたパス。
     */
    public static String normalizePath(String path) {
        if (path == null) {
            return null;
        } else if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        } else {
            return path;
        }
    }

    /**
     * ディスパッチされたURLのクエリ文字列を返します。
     * <p>通常はリクエストされたパスのクエリ文字列、リクエストがforwardの処理中である場合はforward先のパスのクエリ文字列、
     * リクエストがincludeの処理中である場合はincludeしたページのパスのクエリ文字列を返します。
     * </p>
     * <p>クエリ文字列の開始を表す「?」は取り除かれます。
     * </p>
     * 
     * @param request リクエスト。
     * @return クエリ文字列。クエリ文字列が付与されていない場合はnull。
     */
    public static String getQueryString(HttpServletRequest request) {
        Dispatcher dispatcher = getDispatcher(request);
        if (dispatcher == Dispatcher.INCLUDE) {
            return (String) request.getAttribute(ATTR_INCLUDE_QUERY_STRING);
        } else if (dispatcher == Dispatcher.FORWARD) {
            return request.getQueryString();
        } else {
            String queryString = (String) request
                    .getAttribute(ATTR_FORWARD_QUERY_STRING);
            // 念のため。
            if (queryString == null) {
                queryString = request.getQueryString();
            }
            return queryString;
        }
    }

    /**
     * 指定されたパスに付与されているクエリ文字列を返します。
     * <p><code>path</code>がnullの場合はnullを返します。
     * </p>
     * <p>クエリ文字列の開始を表す「?」は取り除かれます。
     * </p>
     * 
     * @param path パス。
     * @return クエリ文字列。クエリ文字列が付与されていない場合はnull。
     */
    public static String getQueryString(String path) {
        if (path == null) {
            return null;
        }

        int question = path.indexOf('?');
        if (question >= 0) {
            String rawQueryString = path.substring(question + 1);
            if (rawQueryString.trim().length() == 0) {
                return null;
            } else {
                return rawQueryString;
            }
        } else {
            return null;
        }
    }

    /**
     * 現在のリクエストの処理状態を表すディスパッチャを返します。
     * 
     * @param request リクエスト。
     * @return ディスパッチャ。
     */
    public static Dispatcher getDispatcher(HttpServletRequest request) {
        if (request.getAttribute(ATTR_ERROR_EXCEPTION) != null) {
            return Dispatcher.ERROR;
        } else if (request.getAttribute(ATTR_INCLUDE_CONTEXT_PATH) != null) {
            // forward->includeの場合はforwardとincludeが両方セットされるため、includeを先に見ている。
            // include->forwardはエラーになるので気にしなくて良い（はず）。
            return Dispatcher.INCLUDE;
        } else if (request.getAttribute(ATTR_FORWARD_CONTEXT_PATH) != null) {
            return Dispatcher.FORWARD;
        } else {
            return Dispatcher.REQUEST;
        }
    }

    /**
     * 指定されたパスとパラメータからURIを構築して返します。
     * <p>パラメータを保持するMapの値はStringまたはString[]型であるべきです。
     * また値としてnullを指定することもできます。その場合そのパラメータは無視されます。
     * </p>
     * 
     * @param path パス。nullを指定してはいけません。
     * @param paramMap パラメータを持つMap。nullを指定することもできます。
     * @param encoding パラメータをURIに付与させる際の文字エンコーディング。
     * @return 構築したURI。
     * @throws UnsupportedEncodingException 指定された文字エンコーディングが存在しない場合。
     */
    public static String constructURI(String path,
            Map<String, Object> paramMap, String encoding)
            throws UnsupportedEncodingException {
        if (paramMap == null) {
            return path;
        }

        if (encoding == null) {
            encoding = "ISO-8859-1";
        }
        StringBuilder sb = new StringBuilder(path);
        String delim = "?";
        Iterator<Map.Entry<String, Object>> itr = paramMap.entrySet()
                .iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Object> entry = itr.next();
            String key = entry.getKey();
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

    static boolean wasForwareded(HttpServletRequest request) {
        return request.getAttribute(ATTR_FORWARD_CONTEXT_PATH) != null;
    }

    /**
     * レスポンスがキャッシュされないようにするためのヘッダ情報をResponseオブジェクトに設定します。
     * <p>これは<code>setNoCache(response, false)</code>と等価です。
     * 
     * @param response HttpServletResponseオブジェクト。nullを指定した場合は何もしません。
     */
    public static void setNoCache(HttpServletResponse response) {
        setNoCache(response, false);
    }

    /**
     * レスポンスがキャッシュされないようにするためのヘッダ情報をResponseオブジェクトに設定します。
     * 
     * @param response HttpServletResponseオブジェクト。nullを指定した場合は何もしません。
     * @param force falseの場合、既にCache-Controlヘッダが設定されていれば何もしません。
     * trueの場合、Cache-Controlヘッダの情報を上書きします。
     */
    public static void setNoCache(HttpServletResponse response, boolean force) {
        if (response == null || !force
                && response.containsHeader("Cache-Control")) {
            return;
        }

        response.setHeader("Pragma", "No-cache");
        response
                .setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
        response.setDateHeader("Expires", 1);
    }

    /**
     * 完全なURLを構築して返します。
     * 
     * @param request リクエスト。
     * @param scheme スキーム。
     * @param port ポート。
     * @param absolutePath ドメイン相対パス。
     * @return 構築したURL。
     */
    public static String constructURL(HttpServletRequest request,
            String scheme, int port, String absolutePath) {
        StringBuilder sb = new StringBuilder(256);
        sb.append(scheme).append(PROTOCOL_DOMAIN_DELIMITER);
        sb.append(request.getServerName());
        if (!(SCHEME_HTTP.equals(scheme) && port == PORT_HTTP || SCHEME_HTTPS
                .equals(scheme)
                && port == PORT_HTTPS)) {
            sb.append(':').append(port);
        }
        sb.append(absolutePath);

        return sb.toString();
    }

    /**
     * リクエストされたURLを返します。
     * <p>リクエストがforwardやincludeの処理中であっても元々のパスを返します。
     * </p>
     * <p>返されるURLにはクエリ文字列も付与されます。
     * </p>
     * 
     * @param request リクエスト。
     * @return リクエストされたURL。
     */
    public static String getRequestURL(HttpServletRequest request) {
        return constructRequestURL(request, request.getScheme(), request
                .getServerPort());
    }

    /**
     * リクエストされたURLについてスキームとポートを差し替えたものを返します。
     * <p>リクエストがforwardやincludeの処理中であっても元々のパスを返します。
     * </p>
     * <p>返されるURLにはクエリ文字列も付与されます。
     * </p>
     * 
     * @param request リクエスト。
     * @param scheme スキーム。
     * @param port ポート。
     * @return リクエストされたURLのプロトコルとポートを差し替えたURL。
     */
    public static String constructRequestURL(HttpServletRequest request,
            String scheme, int port) {
        StringBuilder sb = new StringBuilder(256);
        sb.append(scheme).append(PROTOCOL_DOMAIN_DELIMITER);
        sb.append(request.getServerName());
        if (!(SCHEME_HTTP.equals(scheme) && port == PORT_HTTP || SCHEME_HTTPS
                .equals(scheme)
                && port == PORT_HTTPS)) {
            sb.append(':').append(port);
        }
        String contextPath;
        String servletPath;
        String pathInfo;
        String queryString;
        if (wasForwareded(request)) {
            contextPath = (String) request
                    .getAttribute(ATTR_FORWARD_CONTEXT_PATH);
            servletPath = (String) request
                    .getAttribute(ATTR_FORWARD_SERVLET_PATH);
            pathInfo = (String) request.getAttribute(ATTR_FORWARD_PATH_INFO);
            queryString = (String) request
                    .getAttribute(ATTR_FORWARD_QUERY_STRING);
        } else {
            contextPath = request.getContextPath();
            servletPath = request.getServletPath();
            pathInfo = request.getPathInfo();
            queryString = request.getQueryString();
        }
        sb.append(contextPath).append(servletPath);
        if (pathInfo != null) {
            sb.append(pathInfo);
        }
        if (queryString != null) {
            sb.append('?').append(queryString);
        }
        return sb.toString();
    }

    /**
     * 指定されたクエリ文字列からパラメータを保持するMapを構築します。
     * 
     * @param param クエリ文字列。nullが指定された場合はこのメソッドはnullを返します。
     * @param encoding クエリ文字列をエンコードしている文字エンコーディング。
     * @return 構築したMapオブジェクト。
     * @throws UnsupportedEncodingException 指定された文字エンコーディングが存在しない場合。
     */
    public static Map<String, String[]> parseParameters(String param,
            String encoding) throws UnsupportedEncodingException {
        if (param == null) {
            return null;
        }

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

    /**
     * 指定されたパスからクエリ文字列を取り除いた部分を返します。
     * <p>nullが指定された場合はnullを返します。
     * </p>
     * 
     * @param path パス。
     * @return パスからクエリ文字列を取り除いた部分。
     */
    public static String getTrunk(String path) {
        if (path == null) {
            return null;
        }

        int question = path.indexOf('?');
        if (question < 0) {
            return path;
        } else {
            return path.substring(0, question);
        }
    }
}
