package org.seasar.ymir.extension.zpt;

import org.seasar.ymir.extension.Globals;

public class AnalyzerUtils {
    private AnalyzerUtils() {
    }

    public static boolean isValidVariableName(String name) {
        if (name == null || name.length() == 0) {
            return false;
        }

        char ch = name.charAt(0);
        if (!(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_')) {
            return false;
        }
        for (int i = 1; i < name.length(); i++) {
            ch = name.charAt(i);
            if (!(ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0'
                    && ch <= '9' || ch == '_')) {
                return false;
            }
        }
        return true;
    }

    public static boolean shouldGeneratePropertyForParameter(String name) {
        if (name == null || name.startsWith(Globals.IDPREFIX)) {
            return false;
        }

        int pre = 0;
        int idx;
        while ((idx = name.indexOf('.', pre)) >= 0) {
            if (!shouldGeneratePropertyForParameterSegment(name.substring(pre,
                    idx))) {
                return false;
            }
            pre = idx + 1;
        }
        if (!shouldGeneratePropertyForParameterSegment(name.substring(pre))) {
            return false;
        }
        return true;
    }

    public static boolean shouldGeneratePropertyForParameterSegment(String name) {
        if (name == null) {
            return false;
        }
        if (name.endsWith("]")) {
            int lbracket = name.indexOf('[');
            if (lbracket < 0) {
                return false;
            }
            name = name.substring(0, lbracket);
        }
        return AnalyzerUtils.isValidVariableName(name);
    }
}
