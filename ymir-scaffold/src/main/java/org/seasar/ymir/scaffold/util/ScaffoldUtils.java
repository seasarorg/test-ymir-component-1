package org.seasar.ymir.scaffold.util;

public class ScaffoldUtils {
    private static final String LS = System.getProperty("line.separator");

    private ScaffoldUtils() {
    }

    public static String adjustContentForTextarea(String content) {
        if (content == null) {
            return null;
        }

        if (content.length() > 0) {
            char ch = content.charAt(0);
            if (ch == '\r' || ch == '\n') {
                content = LS + content;
            }
        }

        return content;
    }
}
