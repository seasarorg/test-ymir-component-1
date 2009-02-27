package org.seasar.ymir;

import java.lang.reflect.Method;

public interface ActionManager {
    Action newAction(Object page, Class<?> pageClass, Method method);

    Action newAction(Object page, Class<?> pageClass, Method method,
            Object[] extendedParams);

    MethodInvoker newMethodInvoker(Class<?> pageClass, Method method);

    MethodInvoker newMethodInvoker(Class<?> pageClass, Method method,
            Object[] extendedParams);

    Action newAction(Object page, MethodInvoker methodInvoker);

    Action newVoidAction(Object page);

    Response invokeAction(Action action);

    /**
     * 指定されたPageオブジェクトに関してアクションを実行した結果からResponseオブジェクトを構築します。
     * 
     * @param page Pageオブジェクト。nullを指定することもできます。
     * @param returnType 実行したアクションメソッドの返り値型。
     * @param returnValue アクションメソッドの実行結果。
     * @return 構築したResponseオブジェクト。nullを返すことはありません。
     */
    Response constructResponse(Object page, Class<?> returnType,
            Object returnValue);
}
