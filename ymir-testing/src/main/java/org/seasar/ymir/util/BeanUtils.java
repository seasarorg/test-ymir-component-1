package org.seasar.ymir.util;

import java.beans.Introspector;

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
        return name != null && name.length() == 0 ? null : Introspector
                .decapitalize(name);
    }

    public static String getFirstSimpleSegment(String propertyName) {
        String segment = getFirstSegment(propertyName);
        if (segment == null) {
            return null;
        }
        int index = segment.indexOf('[');
        if (index < 0) {
            index = segment.indexOf('(');
            if (index < 0) {
                return segment;
            }
        }
        return segment.substring(0, index);
    }

    public static String getFirstSegment(String propertyName) {
        if (propertyName == null) {
            return null;
        }

        int dot = propertyName.indexOf('.');
        int beginMap = propertyName.indexOf('(');
        int endMap = propertyName.indexOf(')');
        if (endMap >= 0 && beginMap >= 0
                && (dot < 0 || dot >= 0 && dot > beginMap)) {
            dot = propertyName.indexOf('.', endMap);
        }
        if (dot < 0) {
            return propertyName;
        } else {
            return propertyName.substring(0, dot);
        }
    }

    public static boolean isSingleSegment(String propertyName) {
        return propertyName.indexOf('.') < 0;
    }

    public static boolean isAmbiguousPropertyName(String propertyName) {
        if (propertyName == null || propertyName.length() == 0) {
            return false;
        } else if (propertyName.length() >= 2
                && Character.isUpperCase(propertyName.charAt(1))) {
            return Character.isLowerCase(propertyName.charAt(0));
        } else {
            return Character.isUpperCase(propertyName.charAt(0));
        }
    }

    public static String normalizePropertyName(String propertyName) {
        if (!isAmbiguousPropertyName(propertyName)) {
            return propertyName;
        } else {
            if (propertyName.length() >= 2
                    && Character.isUpperCase(propertyName.charAt(1))) {
                return Character.toUpperCase(propertyName.charAt(0))
                        + propertyName.substring(1);
            } else {
                return Character.toLowerCase(propertyName.charAt(0))
                        + propertyName.substring(1);
            }
        }
    }
}
