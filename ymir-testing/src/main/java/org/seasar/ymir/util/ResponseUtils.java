package org.seasar.ymir.util;

import org.seasar.ymir.Response;

/**
 * @author YOKOTA Takehiko
 */
public class ResponseUtils {
    private ResponseUtils() {
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
}
