package org.seasar.ymir.util;

import org.seasar.ymir.Action;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.MethodInvoker;
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

    /**
     * 指定されたリクエストの現在のディスパッチに設定されているアクションが、
     * リクエストに対応する元々のアクションを呼び出すためのアクションであるかどうかを返します。
     * 
     * @param request リクエスト。nullを指定してはいけません。
     * @return リクエストに対応する元々のアクションを呼び出すためのアクションであるかどうか。
     * @since 1.0.3
     */
    public static boolean isOriginalActionInvoked(Request request) {
        return isOriginalActionInvoked(request.getCurrentDispatch());
    }

    /**
     * 指定されたディスパッチに設定されているアクションが、
     * リクエストに対応する元々のアクションを呼び出すためのアクションであるかどうかを返します。
     * 
     * @param dispatch ディスパッチ。nullを指定してはいけません。
     * @return リクエストに対応する元々のアクションを呼び出すためのアクションであるかどうか。
     * @since 1.0.3
     */
    public static boolean isOriginalActionInvoked(Dispatch dispatch) {
        Action originalAction = dispatch.getOriginalAction();
        Action action = dispatch.getAction();
        if (action == null || originalAction == null) {
            return false;
        }
        return equals(originalAction, action);
    }

    static boolean equals(Action action1, Action action2) {
        if (action1.getTarget() != action2.getTarget()) {
            return false;
        }

        return methodEquals(action1.getMethodInvoker(), action2
                .getMethodInvoker());
    }

    static boolean methodEquals(MethodInvoker methodInvoker1,
            MethodInvoker methodInvoker2) {
        if (methodInvoker1 == null) {
            return methodInvoker2 == null;
        } else {
            if (methodInvoker2 == null) {
                return false;
            }
        }

        if (methodInvoker1.getMethod() != methodInvoker2.getMethod()) {
            return false;
        }

        return true;
    }
}
