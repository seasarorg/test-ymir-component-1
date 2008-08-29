package org.seasar.ymir.util;

public class RegexUtils {
    public static final String SPECIAL_LETTERS = "\\.^?*+|(){}[]:!<>=$";

    private RegexUtils() {
    }

    public static String toRegexPattern(String string) {
        if (string == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (SPECIAL_LETTERS.indexOf(ch) >= 0) {
                sb.append('\\');
            }
            sb.append(ch);
        }
        return sb.toString();
    }
}
