package org.seasar.ymir.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * ログ出力用のユーティリティクラスです。
 *
 * @since 1.0.1
 */
public class LogUtils {
    public static final String INDENT = "  ";

    public static final String DELIM = ", ";

    public static final String LS = System.getProperty("line.separator");

    public static String addIndent(Object obj) {
        return addIndent(obj, INDENT);
    }

    /**
     * 指定されたオブジェクトの文字列表現の各行にインデント文字を追加します。
     * <p>オブジェクトとしてnullが指定された場合はnullを返します。
     * </p>
     * 
     * @param obj 対象となるオブジェクト。nullを指定することもできます。
     * @param indent インデント文字。nullを指定してはいけません。
     * @return インデントを付加した文字列。
     */
    public static String addIndent(Object obj, String indent) {
        if (obj == null) {
            return null;
        }
        String text = obj.toString();
        if (text.length() == 0) {
            return text;
        }

        BufferedReader br = new BufferedReader(new StringReader(text));
        StringBuilder sb = new StringBuilder();
        String line;
        String delim = "";
        String idt = "";
        try {
            while ((line = br.readLine()) != null) {
                sb.append(delim).append(idt).append(line);
                delim = LS;
                idt = indent;
            }
        } catch (IOException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }

        char lastChar = text.charAt(text.length() - 1);
        if (lastChar == '\n' || lastChar == '\r') {
            sb.append(LS);
        }

        return sb.toString();
    }

    public static String toString(Map<?, ?> map) {
        if (map == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (!map.isEmpty()) {
            sb.append(LS);
            for (Iterator<?> it = toSortedSet(map.keySet()).iterator(); it
                    .hasNext();) {
                final Object key = it.next();
                sb.append(INDENT).append(key).append("=").append(
                        addIndent(map.get(key), INDENT)).append(LS);
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public static <V> SortedSet<V> toSortedSet(Collection<V> collection) {
        final SortedSet<V> set = new TreeSet<V>();
        set.addAll(collection);
        return set;
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            return toString((Map<?, ?>) obj);
        } else if (obj.getClass().isArray()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            String delim = "";
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                sb.append(delim).append(Array.get(obj, i));
                delim = DELIM;
            }
            sb.append("]");
            return sb.toString();
        } else {
            return obj.toString();
        }
    }
}
