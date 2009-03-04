package org.seasar.ymir;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.seasar.ymir.util.StringUtils;

/**
 * コンテキスト相対のパスを表すクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class Path {
    private String trunk_;

    private Map<String, String[]> parameterMap_;

    private String parameterEncoding_;

    private String pathParameter_;

    private String fragment_ = "";

    private boolean asNoCache_;

    /**
     * このクラスのオブジェクトを構築します。
     */
    public Path() {
        this(null);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param path コンテキスト相対のパス文字列。
     */
    public Path(String path) {
        this(path, new LinkedHashMap<String, String[]>());
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param path コンテキスト相対のパス文字列。
     * @param parameterEncoding パスに追加するパラメータの文字エンコーディング。
     */
    public Path(String path, String parameterEncoding) {
        this(path, new LinkedHashMap<String, String[]>(), parameterEncoding);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param path コンテキスト相対のパス文字列。
     * @param parameterMap パスに追加するパラメータが格納されているMap。
     * パラメータの文字エンコーディングはUTF-8とみなされます。
     */
    public Path(String path, Map<String, String[]> parameterMap) {
        this(path, parameterMap, "UTF-8");
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param path コンテキスト相対のパス文字列。
     * @param parameterMap パスに追加するパラメータが格納されているMap。
     * @param parameterEncoding パスに追加するパラメータの文字エンコーディング。
     */
    public Path(String path, Map<String, String[]> parameterMap,
            String parameterEncoding) {
        parameterMap_ = parameterMap;
        parameterEncoding_ = parameterEncoding;
        analyze(path);
    }

    void analyze(String path) {
        if (path == null) {
            return;
        }

        int sharp = path.lastIndexOf('#');
        if (sharp >= 0) {
            fragment_ = path.substring(sharp);
            path = path.substring(0, sharp);
        }

        int question = path.indexOf('?');
        if (question >= 0) {
            int pre = question + 1;
            int idx;
            while ((idx = path.indexOf('&', pre)) >= 0) {
                addEncodedParameter(path.substring(pre, idx));
                pre = idx + 1;
            }
            if (pre < path.length()) {
                addEncodedParameter(path.substring(pre));
            }

            path = path.substring(0, question);
        }

        int semi = path.indexOf(';');
        if (semi >= 0) {
            pathParameter_ = path.substring(semi + 1);
            path = path.substring(0, semi);
        }

        trunk_ = path;
    }

    void addEncodedParameter(String encodedParam) {
        String encodedName;
        String encodedValue;

        int equal = encodedParam.indexOf('=');
        if (equal >= 0) {
            encodedName = encodedParam.substring(0, equal);
            encodedValue = encodedParam.substring(equal + 1);
        } else {
            // 値が空文字列であるとみなす。
            encodedName = encodedParam;
            encodedValue = "";
        }

        try {
            addParameter(URLDecoder.decode(encodedName, parameterEncoding_),
                    URLDecoder.decode(encodedValue, parameterEncoding_));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String toString() {
        return asString();
    }

    /**
     * パスを表す文字列を構築して返します。
     * <p>文字列はパラメータとフラグメントを追加した形で返します。
     * パラメータは適切な文字エンコーディングでURLエンコーディングされます。
     * </p>
     * <p>asNoCacheプロパティがtrueである場合はパラメータとしてユニークキーが付与されます。
     * </p>
     * 
     * @return 構築したパス文字列。
     */
    public String asString() {
        StringBuilder sb = new StringBuilder();
        sb.append(trunk_);
        if (pathParameter_ != null) {
            sb.append(";").append(pathParameter_);
        }
        String queryString = getQueryString();
        if (queryString != null) {
            sb.append("?").append(queryString);
        }
        sb.append(fragment_);

        return sb.toString();
    }

    /**
     * パラメータからクエリ文字列を構築して返します。
     * <p>先頭に「?」は付与しません。
     * またパラメータは適切な文字エンコーディングでURLエンコーディングされます。
     * </p>
     * <p>asNoCacheプロパティがtrueである場合はパラメータとしてユニークキーが付与されます。
     * </p>
     * <p>パラメータが存在しない場合はnullを返します。
     * </p>
     * 
     * @return 構築したクエリ文字列。
     */
    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        String delim = "";
        if (parameterMap_ != null) {
            for (Iterator<Map.Entry<String, String[]>> itr = parameterMap_
                    .entrySet().iterator(); itr.hasNext();) {
                Map.Entry<String, String[]> entry = itr.next();
                String key = entry.getKey();
                String[] values = entry.getValue();
                if (key == null || values == null || values.length == 0) {
                    continue;
                }
                String encodedKey;
                try {
                    encodedKey = URLEncoder.encode(key, parameterEncoding_);
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException(ex);
                }
                for (int i = 0; i < values.length; i++) {
                    sb.append(delim).append(encodedKey).append("=");
                    try {
                        sb.append(URLEncoder.encode(values[i],
                                parameterEncoding_));
                    } catch (UnsupportedEncodingException ex) {
                        throw new RuntimeException(ex);
                    }
                    delim = "&";
                }
            }
        }
        if (asNoCache_) {
            sb.append(delim).append(getUniqueKey());
            delim = "&";
        }
        if (sb.length() == 0) {
            return null;
        } else {
            return sb.toString();
        }
    }

    String getUniqueKey() {
        return StringUtils.generateLocalKey(this);
    }

    /**
     * パスからパラメータやフラグメントを取り去った部分を返します。
     * 
     * @return パスからパラメータやフラグメントを取り去った部分。nullを返すこともあります。
     */
    public String getTrunk() {
        return trunk_;
    }

    /**
     * パスからパラメータやフラグメントを取り去った部分を設定します。
     * 
     * @param trunk パスからパラメータやフラグメントを取り去った部分。
     * @return このオブジェクト自身。
     */
    public Path setTrunk(String trunk) {
        trunk_ = trunk;
        return this;
    }

    /**
     * パラメータのMapを返します。
     * 
     * @return パラメータのMap。
     */
    public Map<String, String[]> getParameterMap() {
        return parameterMap_;
    }

    /**
     * パラメータのMapを設定します。
     * 
     * @param parameterMap パラメータのMap。
     * @return このオブジェクト自身。
     */
    public Path setParameterMap(Map<String, String[]> parameterMap) {
        parameterMap_ = parameterMap;
        return this;
    }

    /**
     * パラメータを設定します。
     * <p>同じ名前のパラメータが既に存在する場合は置き換えられます。
     * </p>
     * 
     * @param name パラメータの名前。nullを指定してはいけません。
     * @param value パラメータの値。nullを指定してはいけません。
     * @return このオブジェクト自身。
     */
    public Path setParameter(String name, String value) {
        return setParameter(name, new String[] { value });
    }

    /**
     * パラメータを設定します。
     * <p>このメソッドは1つのパラメータ名に複数の値を指定したい場合に使われます。
     * </p>
     * <p>同じ名前のパラメータが既に存在する場合は置き換えられます。
     * </p>
     * 
     * @param name パラメータの名前。nullを指定してはいけません。
     * @param value パラメータの値。
     * @return このオブジェクト自身。
     */
    public Path setParameter(String name, String[] values) {
        parameterMap_.put(name, values);
        return this;
    }

    /**
     * パラメータを設定します。
     * <p>同じ名前のパラメータが既に存在する場合は追加されます。
     * </p>
     * 
     * @param name パラメータの名前。nullを指定してはいけません。
     * @param value パラメータの値。
     * @return このオブジェクト自身。
     */
    public Path addParameter(String name, String value) {
        if (value == null) {
            return this;
        }

        String[] values = parameterMap_.get(name);
        String[] newValues;
        if (values == null) {
            newValues = new String[] { value };
        } else {
            newValues = new String[values.length + 1];
            System.arraycopy(values, 0, newValues, 0, values.length);
            newValues[values.length] = value;
        }
        parameterMap_.put(name, newValues);
        return this;
    }

    /**
     * 指定された名前のパラメータを削除します。
     * 
     * @param name パラメータの名前。nullを指定してはいけません。
     * @return このオブジェクト自身。
     */
    public Path removeParameter(String name) {
        parameterMap_.remove(name);
        return this;
    }

    /**
     * パラメータを全て削除します。
     * 
     * @return このオブジェクト自身。
     */
    public Path clearParameters() {
        parameterMap_.clear();
        return this;
    }

    /**
     * パラメータの文字エンコーディングを返します。
     * 
     * @return パラメータの文字エンコーディング。
     */
    public String getParameterEncoding() {
        return parameterEncoding_;
    }

    /**
     * パラメータの文字エンコーディングを設定します。
     * 
     * @param parameterEncoding パラメータの文字エンコーディング。
     * @return このオブジェクト自身。
     */
    public Path setParameterEncoding(String parameterEncoding) {
        parameterEncoding_ = parameterEncoding;
        return this;
    }

    /**
     * パス文字列を構築する際にユニークキーをパラメータとして付与するかどうかを返します。
     *
     * @return ユニークキーをパラメータとして付与するかどうか。
     */
    public boolean isAsNoCache() {
        return asNoCache_;
    }

    /**
     * パス文字列を構築する際にユニークキーをパラメータとして付与するかどうかを設定します。
     * <p>Webクライアント等にレスポンスをキャッシュされないよう、
     * パス文字列を構築する際にユニークキーをパラメータとして付与することができるようになっています。
     * このメソッドでtrueを設定することで、
     * パス文字列を構築する際にユニークキーをパラメータとして付与するようになります。
     * </p>
     * <p>デフォルトの状態はfalseです。
     * </p>
     * 
     * @param asNoCache ユニークキーをパラメータとして付与するかどうか。
     * @return このオブジェクト自身。
     */
    public Path setAsNoCache(boolean asNoCache) {
        asNoCache_ = asNoCache;
        return this;
    }

    /**
     * パスパラメータを返します。
     * <p>パスパラメータとは、パスのうち「;」が指定されている部分から後ろの文字列です。
     * 例えば<code>/path;f</code>というパス文字列からこのオブジェクトを構築した場合は、
     * このメソッドは<code>f</code>を返します。
     * パスパラメータを持たないパス文字列からこのオブジェクトを構築した場合は、
     * このメソッドはnullを返します。
     * </p>
     * 
     * @return パスパラメータ。
     */
    public String getPathParameter() {
        return pathParameter_;
    }

    /**
     * フラグメントを返します。
     * <p>フラグメントとは、パスのうち「#」が指定されている部分から後ろの文字列です。
     * 例えば<code>/path#f</code>というパス文字列からこのオブジェクトを構築した場合は、
     * このメソッドは<code>#f</code>を返します。
     * フラグメントを持たないパス文字列からこのオブジェクトを構築した場合は、
     * このメソッドは空文字列を返します。
     * </p>
     * 
     * @return フラグメント。nullを返すことはありません。
     */
    public String getFragment() {
        return fragment_;
    }
}
