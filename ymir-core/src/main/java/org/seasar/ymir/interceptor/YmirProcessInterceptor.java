package org.seasar.ymir.interceptor;

import org.seasar.ymir.Action;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.PermissionDeniedException;

/**
 * Ymirの処理の途中で独自の処理を挟み込むためのインタフェースです。
 * <p>このインタフェースの実装クラスはスレッドセーフである必要があります。</p>
 */
public interface YmirProcessInterceptor {
    /**
     * このYmirProcessInterceptorの優先順位を返します。
     * <p>YmirProcessInterceptorは優先順位の高い（＝数値の小さい）順に登録されて実行されます。
     * 他のYmirProcessInterceptorよりも優先的に、または後に実行したい場合はこの数値を調整して下さい。
     * </p>
     * 
     * @return 優先順位。
     */
    double getPriority();

    /**
     * 現在のリクエストに関する情報を持つRequestオブジェクトをフレームワークが生成した後に、
     * Requestオブジェクトを加工できるように呼び出されるメソッドです。
     * <p>Requestオブジェクトを加工しない場合は引数で渡されたRequestオブジェクトをそのまま返すようにして下さい。
     * </p>
     * 
     * @param request フレームワークによって作成されたRequestオブジェクト。
     * @return Requestオブジェクト。
     */
    Request requestCreated(Request request);

    PageComponent pageComponentCreated(PageComponent pageComponent);

    Action actionInvoking(Action originalAction, Request request, Action action)
            throws PermissionDeniedException;

    /**
     * リダイレクト先のURLを加工できるように呼び出されるメソッドです。
     * <p>URLを加工しない場合は引数で渡されたURLをそのまま返すようにして下さい。
     * </p>
     * <p>このメソッドに渡されるURLは内部URLだけです。
     * また、URLはドメイン相対のURL（コンテキストパスで開始されるURL）です。
     * </p>
     *
     * @param url URL。
     * @return 加工後のURL。
     */
    String encodingRedirectURL(String url);

    void leavingRequest(Request request);
}
