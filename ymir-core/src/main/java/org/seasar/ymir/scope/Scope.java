package org.seasar.ymir.scope;

import java.util.Iterator;

/**
 * オブジェクトを保持する領域（スコープ）を定義するためのインタフェースです。
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

    /**
     * このスコープの名前を返します。
     * <p>このメソッドは主にフレームワークがスコープを区別するために使用されます。
     * このインタフェースの実装クラスを作成する場合、
     * 通常は実装クラスのFQCNを返すようにして下さい。
     * 
     * @return スコープの名前。
     */
    String getName();

    /**
     * このスコープが持つ全ての属性の名前を返します。
     * @return
     */
    Iterator<String> getAttributeNames();
}
