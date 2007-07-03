package org.seasar.ymir.util;

import java.lang.reflect.Method;

public class MethodUtils {
    private MethodUtils() {
    }

    public static Method getMethod(Object object, String methodName) {

        return getMethod(object, methodName, new Class[0]);
    }

    public static Method getMethod(Object object, String methodName,
            Class[] parameterTypes) {

        try {
            return object.getClass().getMethod(methodName, parameterTypes);
        } catch (SecurityException ex) {
            return null;
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }
}
