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
     * 
     * @param response Responseオブジェクト。nullを指定した場合は何もしません。
     */
    public static void setNoCache(Response response) {
        if (response == null) {
            return;
        }

        response.setHeader("Pragma", "No-cache");
        response
                .setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
        response.setDateHeader("Expires", 1);
    }
}
