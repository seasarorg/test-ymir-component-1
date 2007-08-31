package org.seasar.ymir.response.constructor;

import org.seasar.ymir.Response;

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
