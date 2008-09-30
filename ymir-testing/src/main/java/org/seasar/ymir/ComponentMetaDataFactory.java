package org.seasar.ymir;

/**
 * コンポーネントのメタ情報を取得するためのインタフェースです。
 *
 * @since 0.9.6
 * @author yokota
 */
public interface ComponentMetaDataFactory {
    /**
     * 指定されたクラスに関するメタ情報を保持する{@link ComponentMetaData}オブジェクトを返します。
     * 
     * @param clazz クラス。nullを指定してはいけません。
     * @return {@link ComponentMetaData}オブジェクト。
     */
    ComponentMetaData getInstance(Class<?> clazz);
}
