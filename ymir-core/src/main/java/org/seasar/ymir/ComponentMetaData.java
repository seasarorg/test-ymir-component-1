package org.seasar.ymir;

import java.lang.reflect.Method;

/**
 * コンポーネントに関する情報を表すインタフェースです。
 * <p>主にコンポーネントが持つ属性やメソッドなどを表します。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはオブジェクトの内部状態を変えない操作に関してスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface ComponentMetaData {
    /**
     * 指定されたフェーズに関連付けられているメソッドのうち、
     * 指定されたアクションに紐づいているものを返します。
     * <p>フレームワークは、フェーズに関連付けられているPageオブジェクトのメソッドを呼び出します。
     * このメソッドは呼び出すメソッドを取得するためにフレームワークから呼び出されます。
     * </p>
     * 
     * @param phase フェーズ。
     * @param actionName アクション名。nullを指定することもできます。
     * nullを指定した場合はそのフェーズに関する全てのメソッドが返されます。
     * @return メソッド。nullを返すことはありません。
     * @see PageProcessor#invokeMethods(Object, PageMetaData, Phase)
     */
    Method[] getMethods(Phase phase, String actionName);
}
