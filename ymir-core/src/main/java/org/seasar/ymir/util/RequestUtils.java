package org.seasar.ymir.util;

import org.seasar.ymir.Action;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.Ymir;

/**
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public class RequestUtils {
    private RequestUtils() {
    }

    /**
     * 現在のリクエストのディスパッチが、proceedによって遷移してきたものかどうかを返します。
     * 
     * @param request 現在のリクエストオブジェクト。
     * @return proceedによって遷移してきたものかどうか。
     */
    public static boolean isProceeded(Request request) {
        if (request.getCurrentDispatch().getDispatcher() != Dispatcher.FORWARD) {
            return false;
        }
        Response response = (Response) request.getAttribute(Ymir.ATTR_RESPONSE);
        if (response == null) {
            return false;
        }
        return ResponseUtils.isProceed(response);
    }
}
