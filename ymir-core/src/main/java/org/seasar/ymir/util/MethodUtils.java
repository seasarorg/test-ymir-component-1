package org.seasar.ymir.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @since 1.0.2
 */
public class MethodUtils {
    private MethodUtils() {
    }

    public static Object invoke(Method method, Object obj, Object... args) {
        if (method == null) {
            return null;
        }

        try {
            return method.invoke(obj, args);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
