package org.seasar.ymir.util;

import java.lang.reflect.Method;

public class MethodUtils {
    private MethodUtils() {
    }

    public static Method getMethod(Object object, String methodName) {
        return getMethod(object.getClass(), methodName, new Class[0]);
    }

    public static Method getMethod(Object object, String methodName,
            Class<?>[] parameterTypes) {
        return getMethod(object.getClass(), methodName, parameterTypes);
    }

    public static Method getMethod(Class<?> clazz, String methodName) {
        return getMethod(clazz, methodName, new Class[0]);
    }

    public static Method getMethod(Class<?> clazz, String methodName,
            Class<?>[] parameterTypes) {
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (SecurityException ex) {
            return null;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }
}
