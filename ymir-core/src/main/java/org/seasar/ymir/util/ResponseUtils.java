package org.seasar.ymir.util;

import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.response.ForwardResponse;

/**
 * @author YOKOTA Takehiko
 */
public class ResponseUtils {
    /**
     * 例外発生時のレスポンスとなる標準の画面テンプレートが格納されているディレクトリへのパスです。
     */
    public static final String PATH_EXCEPTION_TEMPLATE = "/WEB-INF/template/exception/";

    /**
     * 例外発生時のレスポンスとなる標準の画面テンプレートの拡張子です。
     */
    public static final String SUFFIX_EXCEPTION_TEMPLATE = ".html";

    protected ResponseUtils() {
    }

    /**
     * レスポンスがキャッシュされないようにするためのヘッダ情報をResponseオブジェクトに設定します。
     * <p>これは<code>setNoCache(response, false)</code>と等価です。
     * 
     * @param response Responseオブジェクト。nullを指定した場合は何もしません。
     */
    public static void setNoCache(Response response) {
        setNoCache(response, false);
    }

    /**
     * レスポンスがキャッシュされないようにするためのヘッダ情報をResponseオブジェクトに設定します。
     * 
     * @param response Responseオブジェクト。nullを指定した場合は何もしません。
     * @param force falseの場合、既にCache-Controlヘッダが設定されていれば何もしません。
     * trueの場合、Cache-Controlヘッダの情報を上書きします。
     */
    public static void setNoCache(Response response, boolean force) {
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
     * レスポンスがprivateにのみキャッシュされるようにするためのヘッダ情報をResponseオブジェクトに設定します。
     * <p>これは<code>setPrivateCache(response, false)</code>と等価です。
     * 
     * @param response Responseオブジェクト。nullを指定した場合は何もしません。
     */
    public static void setPrivateCache(Response response) {
        setPrivateCache(response, false);
    }

    /**
     * レスポンスがprivateにのみキャッシュされるようにするためのヘッダ情報をResponseオブジェクトに設定します。
     * 
     * @param response Responseオブジェクト。nullを指定した場合は何もしません。
     * @param force falseの場合、既にCache-Controlヘッダが設定されていれば何もしません。
     * trueの場合、Cache-Controlヘッダの情報を上書きします。
     */
    public static void setPrivateCache(Response response, boolean force) {
        if (response == null || !force
                && response.containsHeader("Cache-Control")) {
            return;
        }

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "private");
    }

    /**
     * 例外に対応するエラーページテンプレートの標準的なパスを返します。
     * <p>返されるパスはコンテキスト相対パスです。
     * </p>
     * 
     * @param exceptionClass 例外クラス。
     * @return パス。
     * @since 1.0.2
     */
    public static String getExceptionTemplatePath(Class<?> exceptionClass) {
        return PATH_EXCEPTION_TEMPLATE
                + ClassUtils.getShortName(exceptionClass)
                + SUFFIX_EXCEPTION_TEMPLATE;
    }

    /**
     * 指定されたレスポンスがproceedレスポンスかどうかを返します。
     * <p>responseにnullが指定された場合はfalseを返します。
     * </p>
     * 
     * @param response レスポンス。
     * @return レスポンスがproceedレスポンスかどうか。
     * @since 1.0.2
     */
    public static boolean isProceed(Response response) {
        return response != null && response.getType() == ResponseType.FORWARD
                && !response.isSubordinate();
    }

    /**
     * 指定されたレスポンスがリダイレクト系レスポンスかどうかを返します。
     * <p>ResponseTypeがREDIRECTであるかproceedレスポンスである場合にtrueを返します。
     * </p>
     * <p>responseにnullが指定された場合はfalseを返します。
     * </p>
     * 
     * @param response レスポンス。nullを指定することもできます。
     * @return レスポンスがリダイレクト系レスポンスかどうか。
     * @since 1.0.3
     */
    public static boolean isRedirect(Response response) {
        return response != null
                && (response.getType() == ResponseType.REDIRECT || isProceed(response));
    }

    /**
     * 指定されたレスポンスがパスの遷移を表すレスポンスかどうかを返します。
     * <p>responseにnullが指定された場合はfalseを返します。
     * </p>
     * 
     * @param response レスポンス。nullを指定することもできます。
     * @return レスポンスがパスの遷移を表すレスポンスかどうか。
     * @since 1.0.2
     */
    public static boolean isTransitionResponse(Response response) {
        return response != null
                && (response.getType() == ResponseType.FORWARD || response
                        .getType() == ResponseType.REDIRECT);
    }

    /**
     * 指定されたレスポンスがパススルーレスポンスかどうかを返します。
     * <p>responseにnullが指定された場合はfalseを返します。
     * </p>
     * 
     * @param response レスポンス。nullを指定することもできます。
     * @return レスポンスがパススルーレスポンスかどうか。
     * @since 1.0.4
     */
    public static boolean isPassthroughResponse(Response response) {
        return response != null
                && response.getType() == ResponseType.PASSTHROUGH;
    }
}
