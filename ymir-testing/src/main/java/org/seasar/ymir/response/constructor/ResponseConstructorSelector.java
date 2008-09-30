package org.seasar.ymir.response.constructor;

/**
 * {@link ResponseConstructor}を管理するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface ResponseConstructorSelector {
    /**
     * 指定された型に対応するResponseConstructorが存在するかどうかを返します。
     * 
     * @param type 型。
     * @return 指定された型に対応するResponseConstructorが存在するかどうか。
     */
    boolean hasResponseConstructor(Class<?> type);

    /**
     * 指定された型に対応するResponseConstructorを返します。
     * 
     * @param type 型。
     * @return 指定された型に対応するResponseConstructor。
     * 存在しない場合はnullを返します。
     */
    <T> ResponseConstructor<T> getResponseConstructor(Class<T> type);
}
