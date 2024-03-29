package org.seasar.ymir.handler;

/**
 * 発生した例外をハンドリングするためのインタフェースです。
 * <p>アプリケーションのhandlerサブパッケージ以下に、
 * ハンドリングしたい例外クラス名に「Handler」という文字列を連結した名前で
 * このインタフェースの実装クラスを作成することで、
 * 例外をハンドリングすることができます。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @deprecated 代わりにExceptionHandlerアノテーションを使用して下さい。
 */
public interface ExceptionHandler<T extends Throwable> {
    /**
     * 例外をハンドリングします。
     * <p>指定された例外が発生した場合にこのメソッドが呼び出されます。
     * </p>
     * <p>このメソッドで例外に関する処理を行なった後、エラーページ等の遷移先を返すようにして下さい。
     * 遷移先の意味はPageオブジェクトのアクションメソッドの返り値と同じです。
     * </p>
     * 
     * @param t 発生した例外。
     * @return 遷移先。
     */
    String handle(T t);
}
