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
     * このオブジェクトの子PageComponentを返します。
     * 
     * @return このオブジェクトの子PageComponent。子PageComponentが存在しない場合は空の配列を返します。
     */
    PageComponent[] getChildren();

    /**
     * このオブジェクトの全ての子孫PageComponentを返します。
     * <p>このオブジェクト自身も含まれます。
     * </p>
     * 
     * @return このオブジェクトの全ての子孫PageComponent。
     */
    PageComponent[] getDescendants();
}
