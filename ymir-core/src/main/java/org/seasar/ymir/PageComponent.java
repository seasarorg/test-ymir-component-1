package org.seasar.ymir;

/**
 * HTTPリクエストを処理するPageオブジェクトに関する情報を表すインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface PageComponent extends Acceptor<PageComponentVisitor<?>> {
    /**
     * Pageオブジェクトを返します。
     * 
     * @return Pageオブジェクト。
     */
    Object getPage();

    /**
     * Pageオブジェクトのクラスを返します。
     * <p>Pageオブジェクトの実際のクラスはコンテナによってエンハンスされていることがありますが、
     * このメソッドはPageオブジェクトの元々のクラスを返します。
     * 
     * @return Pageオブジェクトのクラス。
     */
    Class<?> getPageClass();

    /**
     * 指定されたClassオブジェクトに関連付けられたオブジェクトを返します。
     * <p>このクラスではClassオブジェクトをキーとして任意のオブジェクトを関連付けることができます。
     * このメソッドは、指定されたClassオブジェクトに関連付けられたオブジェクトを返します。
     * オブジェクトが関連付けられていない場合はnullを返します。
     * </p>
     * 
     * @param <T> オブジェクトの型。
     * @param clazz キーとなるClassオブジェクト。
     * @return 関連付けられたオブジェクト。
     * @see #setRelatedObject(Class, Object)
     */
    <T> T getRelatedObject(Class<T> clazz);

    /**
     * lassオブジェクトにオブジェクトを関連付けます。
     * 
     * @param <T> オブジェクトの型。
     * @param clazz キーとなるClassオブジェクト。
     * @param object 関連付けるオブジェクト。nullを指定することで関連付けが削除されます。
     */
    <T> void setRelatedObject(Class<T> clazz, T object);

    /**
     * このオブジェクトの子PageComponentを返します。
     * 
     * @return このオブジェクトの子PageComponent。子PageComponentが存在しない場合は空の配列を返します。
     */
    PageComponent[] getChildren();

    /**
     * このオブジェクトの全ての子孫PageComponentを返します。
     * 
     * @return このオブジェクトの全ての子孫PageComponent。子孫PageComponentが存在しない場合は空の配列を返します。
     */
    PageComponent[] getDescendants();
}
