package org.seasar.ymir.response.constructor;

import org.seasar.ymir.Response;

/**
 * アクションメソッドの返り値からResponseオブジェクトを構築するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 *
 * @see ResponseConstructorSelector
 * @author YOKOTA Takehiko
 */
public interface ResponseConstructor<T> {
    /**
     * このResponseConstructorが処理する対象のクラスを返します。
     * 
     * @return 処理対象のクラス。
     */
    Class<T> getTargetClass();

    /**
     * 指定されたコンポーネントとアクションメソッドの返り値からResponseを構築して返します。
     * 
     * @param page Pageコンポーネント。nullであることがあります。
     * @param returnValue 返り値。nullであることがあります。
     * @return 構築したResponseオブジェクト。
     */
    Response constructResponse(Object page, T returnValue);
}
