package org.seasar.ymir;

/**
 * 開発モードで動作するYmirにおいて、リクエストを受け付けた後に動作する自動生成・更新ロジックを表すインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface Updater {
    /**
     * 必要に応じて開発プロジェクト内のリソースを更新します。
     * <p>このメソッドは、リクエストをまたいで更新処理を行なう場合などに
     * 自動生成・更新画面への遷移用のレスポンスを出力できるよう、メソッドの返り値として
     * Responseオブジェクトを返すようになっています。
     * このメソッドによる更新処理がリクエストをまたがない場合は、
     * 引数として与えられたResponseオブジェクトをそのまま返します。
     * </p>
     * 
     * @param request 現在のRequestオブジェクト。
     * @param response 現在のResponseオブジェクト。
     * @return 最終的なResponseオブジェクト。
     */
    Response update(Request request, Response response);

    /**
     * レスポンスボディを加工します。
     * <p>開発モードで動作している際にレスポンスボディを加工したい場合、
     * このメソッドでレスポンスボディを加工することができます。
     * </p>
     * 
     * @param response レスポンスボディ文字列。
     * @return 加工後のレスポンスボディ文字列。
     */
    String filterResponse(String response);

    /**
     * 例外がスローされたことをトリガとして、必要に応じて開発プロジェクト内のリソースを更新します。
     * <p>このメソッドは、リクエストをまたいで更新処理を行なう場合などに
     * 自動生成・更新画面への遷移用のレスポンスを出力できるよう、メソッドの返り値として
     * Responseオブジェクトを返すようになっています。
     * このメソッドによる更新処理がリクエストをまたがない場合はnullを返します。
     * </p>
     * 
     * @param request 現在のRequestオブジェクト。
     * @param t スローされた例外。
     * @return Responseオブジェクト。
     */
    Response updateByException(Request request, Throwable t);
}
