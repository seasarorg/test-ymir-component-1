package org.seasar.ymir;

public interface Action {
    MethodInvoker getMethodInvoker();

    Object getTarget();

    String getName();

    boolean shouldInvoke();

    Object invoke();
}
