package org.seasar.ymir;

/**
 * レスポンスヘッダを表すクラスです。
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @see Response
 * @author YOKOTA Takehiko
 */
public class ResponseHeader {
    private String name_;

    private Object value_;

    private boolean add_;

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param name ヘッダ名。
     * @param value 値。
     */
    public ResponseHeader(String name, Object value) {
        this(name, value, false);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param name ヘッダ名。
     * @param value 値。
     * @param add 追加設定かどうか。
     */
    public ResponseHeader(String name, Object value, boolean add) {
        name_ = name;
        value_ = value;
        add_ = add;
    }

    @Override
    public String toString() {
        return name_ + "=" + value_ + " (add=" + add_ + ")";
    }

    /**
     * 追加設定かどうかを返します。
     * 
     * @return 追加設定かどうか。
     */
    public boolean isAdd() {
        return add_;
    }

    /**
     * ヘッダ名を返します。
     * 
     * @return ヘッダ名。
     */
    public String getName() {
        return name_;
    }

    /**
     * 値を返します。
     * 
     * @return 値。
     */
    public Object getValue() {
        return value_;
    }
}
