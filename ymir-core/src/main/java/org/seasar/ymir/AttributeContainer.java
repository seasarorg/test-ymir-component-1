package org.seasar.ymir;

import java.util.Enumeration;

/**
 * 属性を保持するものを表すインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface AttributeContainer {
    /**
     * 指定された名前に対応する属性値を返します。
     * <p>対応する属性値がみつからなかった場合はnullを返します。
     * </p>
     * 
     * @param name 名前。
     * @return 値。
     */
    Object getAttribute(String name);

    /**
     * このオブジェクトが保持している全ての属性の名前を返します。
     * <p>属性を保持していない場合は空のEnumerationを返します。
     * </p>
     * 
     * @return 属性名を取り出すためのEnumeration。
     */
    Enumeration<String> getAttributeNames();

    /**
     * 指定された名前に対応する属性の値を設定します。
     * 
     * @param name 名前。
     * @param value 値。
     */
    void setAttribute(String name, Object value);

    /**
     * 指定された名前に対応する属性を削除します。
     * 
     * @param name 名前。
     */
    void removeAttribute(String name);
}
