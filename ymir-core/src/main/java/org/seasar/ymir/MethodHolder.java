package org.seasar.ymir;

import java.lang.reflect.Method;

public interface MethodHolder<C> {
    Method getMethod(C condition);
}
