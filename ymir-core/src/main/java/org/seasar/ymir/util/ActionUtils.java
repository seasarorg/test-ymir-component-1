package org.seasar.ymir.util;

import org.seasar.ymir.Action;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.Request;

/**
 * アクションに関するユーティリティメソッドを提供するクラスです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public class ActionUtils {
    private ActionUtils() {
    }

    /**
     * 2つのアクションが同一であるかどうかを返します。
     * 具体的には、アクションの対象オブジェクトと呼び出しメソッドが同一である場合に
     * trueを返します。
     *
     * @param action1 アクション。
     * @param action2 アクション。
     * @return 同一であるかどうか。
     */
    public static boolean equals(Action action1, Action action2) {
        if (action1 == null) {
            return action2 == null;
        } else {
            if (action2 == null) {
                return false;
            }
        }

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
