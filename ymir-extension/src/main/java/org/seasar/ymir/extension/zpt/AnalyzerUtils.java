package org.seasar.ymir.extension.zpt;

public class AnalyzerUtils {
    private AnalyzerUtils() {
    }

    public static boolean isValidAsSimplePropertyName(String name) {
        if (name == null || name.length() == 0) {
            return false;
        }
        char ch = name.charAt(0);
        if (!(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_')) {
            return false;
        }
        for (int i = 1; i < name.length(); i++) {
            ch = name.charAt(i);
            if (!(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_' || ch >= '0'
                    && ch <= '9')) {
                return false;
            }
        }
        return true;
    }
}
