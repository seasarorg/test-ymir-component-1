package org.seasar.ymir.test;

/**
 * YmirのPageクラスをテストするためのTestCaseのベースとなるクラスです。
 * <p>Pageクラスをテストする場合はこのクラスのサブクラスとしてTestCaseを作成すると便利かもしれません。
 * </p>
 * <p>基本的にはテストメソッドの中から以下のメソッドを順番に呼び出し、
 * {@link #processRequest(Request)}の返り値や{@link #getNotes(Request)}の返り値を
 * アサーションするようにします。
 * </p>
 * <ol>
 *   <li><code>prepareForProcessing()</code></li>
 *   <li>{@link #processRequest(Request)}</li>
 * </ol>
 */
abstract public class PageTestCase<P> extends YmirTestCase {
    /**
     * テスト対象であるPageクラスのクラスオブジェクトを返します。
     * <p>テスト作成者はこのメソッドをオーバライドして適切な値を返すようにして下さい。
     * </p>
     *
     * @return テスト対象であるPageクラスのクラスオブジェクト。
     */
    abstract protected Class<P> getPageClass();

    @SuppressWarnings("unchecked")
    protected P getPage() {
        checkStatus(STATUS_PREPARED);
        return (P) getContainer().getComponent(getPageClass());
    }
}
