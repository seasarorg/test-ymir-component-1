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
     * <p>属性の名前とタイプに該当する属性の値を返します。
     * 存在しない場合はnullを返します。
     * </p>
     * <p>このメソッドの呼び出し元はtypeまたはtypeの配列にキャスト可能な値を返すことを期待していますが、
     * 返される値は必ずしも<code>type</code>にキャスト可能な値とは限りません。
     * </p>
     * 
     * @param name 属性の名前。nullを指定することもできます。
     * @param type 属性の値に期待されるタイプ。nullを指定することもできます。配列型を指定してはいけません。
     * @return 属性の値。
     * 属性が見つからなかった場合はnullを返します。
     */
    Object getAttribute(String name, Class<?> type);

    /**
     * 指定された属性をこのスコープに設定します。
     * 
     * @param name 属性の名前。nullを指定することもできます。
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
     * <p>スコープが持つ全ての属性の名前が取得できないスコープについては、
     * このメソッドは名前の取得が可能な一部の属性についてだけのIteratorや空のIteratorを返すことがあります。
     * </p>
     * 
     * @return このスコープが持つ全ての属性の名前を取り出すためのIterator。
     */
    Iterator<String> getAttributeNames();
}
