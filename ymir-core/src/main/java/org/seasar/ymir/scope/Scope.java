package org.seasar.ymir.scope;

/**
 * オブジェクトを保持する範囲を表すインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface Scope {
    /**
     * このスコープから属性の値を取り出して返します。
     * 
     * @param name 属性の名前。
     * @return 属性の値。
     * 属性が見つからなかった場合はnullを返します。
     */
    Object getAttribute(String name);

    /**
     * 指定された属性をこのスコープに設定します。
     * 
     * @param name 属性の名前。
     * @param value 属性の値。
     * nullを指定すると属性をスコープから取り除きます。
     */
    void setAttribute(String name, Object value);
}
