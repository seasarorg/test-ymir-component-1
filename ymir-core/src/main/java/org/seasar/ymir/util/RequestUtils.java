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
    protected RequestUtils() {
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
        return ResponseUtils.isProceed((Response) request
                .getAttribute(Ymir.ATTR_RESPONSE));
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

        String name1 = action1.getName();
        String name2 = action2.getName();
        if (name1 == null) {
            return name2 == null;
        } else {
            return name1.equals(name2);
        }
    }
}
