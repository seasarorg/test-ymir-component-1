package org.seasar.ymir.dbflute.util;

public class DBFluteUtils {
    private DBFluteUtils() {
    }

    /**
     * 指定された名前をcamel形式にします。
     * <p><code>'_'</code>で結合された名前をcamel形式にします。
     * </p>
     * <p>名前は全て小文字に変換されてからcamelizeされます。
     * camelizeされた名前の先頭は小文字になります。
     * </p>
     * 
     * @param name '_'で結合された名前。
     * nullを指定することもできます。
     * @return camel形式の名前。
     * nullが指定された場合はnullが返されます。
     */
    public static String camelize(String name) {
        if (name == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean edge = false;
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (ch == '_') {
                edge = true;
            } else {
                if (edge) {
                    sb.append(Character.toUpperCase(ch));
                    edge = false;
                } else {
                    sb.append(Character.toLowerCase(ch));
                }
            }
        }
        return sb.toString();
    }
}
