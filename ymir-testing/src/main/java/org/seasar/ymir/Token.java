package org.seasar.ymir;

/**
 * トランザクショントークンを扱うためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface Token {
    /**
     * トークンが現在のセッション中に存在するかどうかを返します。
     * 
     * @return トークンが存在するかどうか。
     */
    public boolean exists();

    /**
     * トークンの名前を返します。
     * <p>トークンの名前は、トークンを持つリクエストパラメータの名前やトークンをセッション中に保持する際の属性名として使われます。
     * </p>
     * 
     * @return トークンの名前。
     */
    public String getName();

    /**
     * トークンを返します。
     * 
     * @return トークン。
     */
    public String getValue();
}
