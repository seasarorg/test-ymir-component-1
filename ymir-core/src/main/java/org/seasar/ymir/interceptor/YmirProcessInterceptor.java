package org.seasar.ymir.interceptor;

import java.lang.reflect.Method;

import org.seasar.ymir.MethodInvoker;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.constraint.PermissionDeniedException;

/**
 * Ymirの処理の途中で独自の処理を挟み込むためのインタフェースです。
 * <p>このインタフェースの実装クラスはスレッドセーフである必要があります。</p>
 */
public interface YmirProcessInterceptor {

    /**
     * 現在のリクエストに関する情報を持つRequestオブジェクトをフレームワークが生成した後に、
     * Requestオブジェクトを加工できるように呼び出されるメソッドです。
     * <p>Requestオブジェクトを加工しない場合は引数で渡されたRequestオブジェクトをそのまま返すようにして下さい。
     * </p>
     * 
     * @param request フレームワークによって作成されたRequestオブジェクト。
     * @return Requestオブジェクト。
     */
    Request filterRequest(Request request);

    void beginProcessingComponent(Object component);

    Object filterComponent(Object component);

    Response beginInvokingAction(Object component, Method action,
            Request request);

    MethodInvoker aboutToInvokeAction(Object component, Request request,
            MethodInvoker methodInvoker) throws PermissionDeniedException;
}
