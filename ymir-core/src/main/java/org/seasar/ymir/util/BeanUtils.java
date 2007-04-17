package org.seasar.ymir.util;

public class BeanUtils {
    protected BeanUtils() {
    }

    public static String toPropertyName(String methodName) {
        for (int i = 0; i < methodName.length(); i++) {
            char ch = methodName.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i + 1 < methodName.length()
                        && Character.isUpperCase(methodName.charAt(i + 1))) {
                    return methodName.substring(i);
                } else {
                    return String.valueOf(Character.toLowerCase(ch))
                            + methodName.substring(i + 1);
                }
            }
        }
        return methodName;
    }
}
