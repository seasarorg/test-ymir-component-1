package org.seasar.ymir.util;

public class BeanUtils {
    private static final String PREFIX_SET = "set";

    private static final String PREFIX_GET = "get";

    private static final String PREFIX_IS = "is";

    protected BeanUtils() {
    }

    public static String toPropertyName(String methodName) {
        return toPropertyName(methodName, true);
    }

    public static String toPropertyName(String methodName, boolean strict) {
        String name = null;
        if (methodName == null) {
            return null;
        } else if (methodName.startsWith(PREFIX_IS)) {
            name = methodName.substring(PREFIX_IS.length());
        } else if (methodName.startsWith(PREFIX_GET)) {
            name = methodName.substring(PREFIX_GET.length());
        } else if (methodName.startsWith(PREFIX_SET)) {
            name = methodName.substring(PREFIX_SET.length());
        } else {
            if (!strict) {
                for (int i = 0; i < methodName.length(); i++) {
                    char ch = methodName.charAt(i);
                    if (Character.isUpperCase(ch)) {
                        name = methodName.substring(i);
                        break;
                    }
                }
            }
        }
        if (name == null || name.length() == 0) {
            return null;
        } else if (name.length() > 1 && Character.isUpperCase(name.charAt(1))) {
            return name;
        } else {
            return Character.toLowerCase(name.charAt(0)) + name.substring(1);
        }
    }
}
