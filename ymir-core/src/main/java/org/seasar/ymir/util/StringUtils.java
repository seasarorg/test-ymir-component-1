package org.seasar.ymir.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class StringUtils {
    private StringUtils() {
    }

    public static String getScopeKey(Object scope) {
        return getScopeKey(scope, false);
    }

    public static String getScopeKey(Object scope, boolean local) {
        if (scope == null) {
            return null;
        }

        return generateKey(String.valueOf(System.identityHashCode(scope)),
                local);
    }

    public static String generateLocalKey(Object scope) {
        if (scope == null) {
            return null;
        }

        return generateKey(String.valueOf(System.identityHashCode(scope)), true);
    }

    public static String generateKey(String scopeId, boolean local) {
        if (scopeId == null) {
            return null;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] id = scopeId.getBytes();
            md.update(id);
            if (local) {
                byte[] now = new Long(System.currentTimeMillis()).toString()
                        .getBytes();
                md.update(now);
            }
            return toHex(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }
    }

    public static String toHex(byte[] buffer) {
        StringBuffer sb = new StringBuffer();
        String s = null;
        for (int i = 0; i < buffer.length; i++) {
            s = Integer.toHexString(buffer[i] & 0xff);
            if (s.length() < 2) {
                sb.append('0');
            }
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * 指定された文字列が空かどうかを返します。
     * <p>指定された文字列がnullか空文字列か空白だけからなる文字列である場合はtrueを返します。
     * そうでない場合はfalseを返します。
     * </p>
     * 
     * @param str 文字列。
     * @return 空かどうか。
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String[] unique(String... strings) {
        if (strings == null) {
            return null;
        }
        return new LinkedHashSet<String>(Arrays.asList(strings))
                .toArray(new String[0]);
    }
}
