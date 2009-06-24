package org.seasar.ymir.util;

import java.io.File;

public class FileUtils {
    protected FileUtils() {
    }

    public static boolean isRelativePath(String path) {
        if (path.length() == 0 || path.startsWith("/")
                || path.startsWith("\\\\")) {
            return false;
        } else if (path.length() >= 3 && path.charAt(1) == ':'
                && path.charAt(2) == '\\') {
            return false;
        } else {
            return true;
        }
    }
}
