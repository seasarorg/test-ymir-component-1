package org.seasar.ymir.plugin;

import java.lang.annotation.Annotation;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.Action;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseProcessor;

/**
 * 特定のPageの処理時にフレームワークのリクエスト処理のライフサイクル毎に
 * 別処理を挟み込むためのプラグイン機構において、
 * 挟み込まれる処理を規定するためのインタフェースです。
 * 
 * @since 0.9.6
 * @author yokota
 */
public interface Plugin<A extends Annotation> {
    /**
     * このプラグインの優先順位を返します。
     * <p>プラグインは優先順位の高い（＝数値の小さい）順に登録されて実行されます。
     * 他のプラグインよりも優先的に、または後に実行したい場合はこの数値を調整して下さい。
     * </p>
     * 
     * @return 優先順位。
     */
    double getPriority();

    /**
     * 現在のリクエストに対応するPageComponentオブジェクトをフレームワークが構築した際に、
     * PageComponentオブジェクトを加工できるように呼び出されるメソッドです。
     * <p>PageComponentオブジェクトを加工しない場合は引数で渡されたPageComponentオブジェクトをそのまま返すようにして下さい。
     * </p>
     * 
     * @param request 現在のRequestオブジェクト。
     * @param pageComponent フレームワークによって構築されたPageComponentオブジェクト。
     * @param annotation プラグイン実行のトリガとなったアノテーション。
     * @return PageComponentオブジェクト。
     */
    PageComponent pageComponentCreated(Request request,
            PageComponent pageComponent, A annotation);

    /**
     * 現在のリクエストに対応するアクションが実行される前に、
     * Actionオブジェクトを加工できるように呼び出されるメソッドです。
     * <p>Actionオブジェクトを加工しない場合は引数で渡されたActionオブジェクトをそのまま返すようにして下さい。
     * </p>
     * 
     * @param request 現在のRequestオブジェクト。
     * @param originalAction フレームワークが構築した元もとのActionオブジェクト。
     * @param action 現在のActionオブジェクト。他のYmirProcessInterceptorによって、
     * 元もとのActionではないものに差し替えられていることがあります。
     * @param annotation プラグイン実行のトリガとなったアノテーション。
     * @return Actionオブジェクト。
     */
    Action actionInvoking(Request request, Action originalAction,
            Action action, A annotation);

    /**
     * フレームワークがResponseオブジェクトを構築した際に、
     * Responseオブジェクトを加工できるように呼び出されるメソッドです。
     * <p>Responseオブジェクトを加工しない場合は引数で渡されたResponseオブジェクトをそのまま返すようにして下さい。
     * </p>
     * <p><strong>注意：</strong>この時点でrequestオブジェクトのカレントのDispatchには
     * 通常PageComponentオブジェクトが設定されていますが、開発モードでは自動生成の都合上
     * PageComponentが設定されていない場合があります。
     * このメソッドの中でPageComponentを利用する場合は必ずnullチェックを行なうようにして下さい。
     * </p>
     * 
     * @param request 現在のRequestオブジェクト。
     * @param response フレームワークによって構築されたResponseオブジェクト。
     * @param annotation プラグイン実行のトリガとなったアノテーション。
     * @return Responseオブジェクト。
     */
    Response responseCreated(Request request, Response response, A annotation);

    /**
     * {@link ResponseProcessor#process(ServletContext, HttpServletRequest, HttpServletResponse, Request, Response)}
     * メソッドによってResponseの処理が開始された際に呼び出されるメソッドです。
     * 
     * @param context ServletContextオブジェクト。
     * @param httpRequest HttpServletRequestオブジェクト。
     * @param httpResponse HttpServletResponseオブジェクト。
     * @param request Requestオブジェクト。
     * @param response Responseオブジェクト。
     * @param annotation プラグイン実行のトリガとなったアノテーション。
     */
    void responseProcessingStarted(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response, A annotation);

    /**
     * フレームワークがHTTPリクエストの処理を完了する直前に呼び出されるメソッドです。
     * 
     * @param request Requestオブジェクト。
     * @param annotation プラグイン実行のトリガとなったアノテーション。
     */
    void leavingRequest(Request request, A annotation);

    /**
     * フレームワークがHTTPリクエストの処理を完了した後に呼び出されるメソッドです。
     * 
     * @param annotation プラグイン実行のトリガとなったアノテーション。
     */
    void leftRequest(A annotation);
}
