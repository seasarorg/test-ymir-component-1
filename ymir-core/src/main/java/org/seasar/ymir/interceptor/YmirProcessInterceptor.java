package org.seasar.ymir.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.Action;
import org.seasar.ymir.ExceptionProcessor;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseProcessor;

/**
 * Ymirの処理の途中で独自の処理を挟み込むためのインタフェースです。
 * <p>Ymirの処理の途中で独自の処理を挟み込みたい場合、
 * このインタフェースの実装クラスをコンテナにコンポーネント登録して下さい。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface YmirProcessInterceptor {
    /**
     * このインターセプタの優先順位を返します。
     * <p>インターセプタは優先順位の高い（＝数値の小さい）順に登録されて実行されます。
     * 他のインターセプタよりも優先的に、または後に実行したい場合はこの数値を調整して下さい。
     * </p>
     * 
     * @return 優先順位。
     */
    double getPriority();

    /**
     * リクエストの処理を開始した際に呼び出されるメソッドです。
     * <p>リクエストがYmirの処理対象である場合だけ呼び出されます。</p>
     * <p>このメソッドが非nullを返した場合は、
     * フレームワークは通常のリクエスト処理をスキップして返されたレスポンスを処理します。
     * </p>
     * 
     * @param context ServletContextオブジェクト。
     * @param httpRequest HttpServletRequestオブジェクト。
     * @param httpResponse HttpServletResponseオブジェクト。
     * @param path コンテキスト相対のリクエストパス。末尾に「/」がついている場合はそのまま渡されます。
     * @return レスポンスオブジェクト。
     */
    Response enteringRequest(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            String path);

    /**
     * 現在のリクエストに関する情報を持つRequestオブジェクトをフレームワークが構築した後に、
     * Requestオブジェクトを加工できるように呼び出されるメソッドです。
     * <p>Requestオブジェクトを加工しない場合は引数で渡されたRequestオブジェクトをそのまま返すようにして下さい。
     * </p>
     * 
     * @param request フレームワークによって構築されたRequestオブジェクト。
     * @return Requestオブジェクト。
     */
    Request requestCreated(Request request);

    /**
     * 現在のリクエストに対応するPageComponentオブジェクトをフレームワークが構築した際に、
     * PageComponentオブジェクトを加工できるように呼び出されるメソッドです。
     * <p>PageComponentオブジェクトを加工しない場合は引数で渡されたPageComponentオブジェクトをそのまま返すようにして下さい。
     * </p>
     * 
     * @param request 現在のRequestオブジェクト。
     * @param pageComponent フレームワークによって構築されたPageComponentオブジェクト。
     * @return PageComponentオブジェクト。
     */
    PageComponent pageComponentCreated(Request request,
            PageComponent pageComponent);

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
     * nullであることもあります。
     * @return Actionオブジェクト。nullを返すこともできます。
     */
    Action actionInvoking(Request request, Action originalAction, Action action);

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
     * <p>このメソッドの呼び出し時にはコンテナからRequestオブジェクト及びResponseオブジェクトが取得可能です。
     * </p>
     * 
     * @param request 現在のRequestオブジェクト。
     * @param response フレームワークによって構築されたResponseオブジェクト。
     * @return Responseオブジェクト。nullを返してはいけません。
     */
    Response responseCreated(Request request, Response response);

    /**
     * {@link ResponseProcessor#process(ServletContext, HttpServletRequest, HttpServletResponse, Request, Response)}
     * メソッドによってResponseの処理が開始された際に呼び出されるメソッドです。
     * <p>このメソッドの呼び出し時にはコンテナからRequestオブジェクト及びResponseオブジェクトが取得可能です。
     * </p>
     * 
     * @param context ServletContextオブジェクト。
     * @param httpRequest HttpServletRequestオブジェクト。
     * @param httpResponse HttpServletResponseオブジェクト。
     * @param request Requestオブジェクト。
     * @param response Responseオブジェクト。
     */
    void responseProcessingStarted(ServletContext context,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response);

    /**
     * フレームワークがHTTPリクエストの処理を完了する直前に呼び出されるメソッドです。
     * <p>このメソッドの呼び出し時にはコンテナからRequestオブジェクト及びResponseオブジェクトが取得可能です。
     * </p>
     * 
     * @param request Requestオブジェクト。
     */
    void leavingRequest(Request request);

    /**
     * フレームワークがディスパッチの処理を完了する直前に呼び出されるメソッドです。
     * <p>このメソッドの呼び出し時にはコンテナからRequestオブジェクト及びResponseオブジェクトが取得可能です。
     * </p>
     * 
     * @param request Requestオブジェクト。
     */
    void leavingDispatch(Request request);

    /**
     * フレームワークがHTTPリクエストの処理を完了した後に呼び出されるメソッドです。
     * <p>このメソッドは{@link #enteringRequest(ServletContext, HttpServletRequest, HttpServletResponse, String)}
     * が呼び出されたならば必ず呼び出されることが保証されています。
     * </p>
     * <p>このメソッドの呼び出し時にはコンテナからRequestオブジェクト及びResponseオブジェクトは取得できません。
     * </p>
     */
    void leftRequest();

    /**
     * {@link ExceptionProcessor#process(Request, Throwable)}
     * メソッドによってExceptionの処理が開始された際に呼び出されるメソッドです。
     * <p>nullを返した場合は処理が継続されます。
     * nullでないResponseオブジェクトを返した場合はそのResponseオブジェクトを返り値として
     * {@link ExceptionProcessor#process(Request, Throwable)}の処理を終了します。
     * </p>
     * <p>このメソッドの呼び出し時にはコンテナからRequestオブジェクトが取得可能です。Responseオブジェクトは取得できません。
     * </p>
     * 
     * @param request Requestオブジェクト。
     * @param t スローされた例外オブジェクト。
     * @return Responseオブジェクト。
     * @since 1.0.0
     */
    Response exceptionProcessingStarted(Request request, Throwable t);

    /**
     * 例外ハンドラのアクションの処理が実行される前に、
     * Actionオブジェクトを加工できるように呼び出されるメソッドです。
     * <p>Actionオブジェクトを加工しない場合は引数で渡されたActionオブジェクトをそのまま返すようにして下さい。
     * </p>
     * 
     * @param request 現在のRequestオブジェクト。
     * Requestオブジェクトが生成される前に例外が発生した場合など、nullが渡されることもあります。
     * @param originalAction フレームワークが構築した元もとのActionオブジェクト。
     * @param action 現在のActionオブジェクト。他のYmirProcessInterceptorによって、
     * 元もとのActionではないものに差し替えられていることがあります。
     * nullであることもあります。
     * @param global アクションを持つ例外ハンドラがグローバルハンドラかどうか。
     * @return Actionオブジェクト。nullを返すこともできます。
     * @since 1.0.2
     */
    Action exceptionHandlerActionInvoking(Request request,
            Action originalAction, Action action, boolean global);

    /**
     * フレームワークが例外ハンドラの処理結果からResponseオブジェクトを構築した際に、
     * Responseオブジェクトを加工できるように呼び出されるメソッドです。
     * <p>Responseオブジェクトを加工しない場合は引数で渡されたResponseオブジェクトをそのまま返すようにして下さい。
     * </p>
     * <p>このメソッドの呼び出し時にはコンテナからRequestオブジェクト及びResponseオブジェクトが取得可能です。
     * </p>
     * 
     * @param request 現在のRequestオブジェクト。
     * Requestオブジェクトが生成される前に例外が発生した場合など、nullが渡されることもあります。
     * @param response フレームワークによって構築されたResponseオブジェクト。
     * @param handler {@link ExceptionHandler}オブジェクト。
     * @param global アクションを持つ例外ハンドラがグローバルハンドラかどうか。
     * @return Responseオブジェクト。nullを返してはいけません。
     * @since 1.0.2
     */
    Response responseCreatedByExceptionHandler(Request request,
            Response response, Object handler, boolean global);
}
