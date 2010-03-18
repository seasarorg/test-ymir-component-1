package org.seasar.ymir.scaffold.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static String hash(String string) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        try {
            digest.update(string.getBytes("ISO-8859-1"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest.digest()) {
                @SuppressWarnings("cast")
                String s = "0" + Integer.toHexString((int) b);
                sb.append(s.substring(s.length() - 2));
            }
            return sb.toString();
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
