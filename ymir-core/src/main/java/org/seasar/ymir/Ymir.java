package org.seasar.ymir;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;

/**
 * Ymirフレームワークのコアとなるインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface Ymir {
    /**
     * このオブジェクトを初期化します。
     */
    void init();

    /**
     * HTTPリクエストを処理するための前準備を行ないます。
     * <p>必要な前準備を行ないRequestオブジェクトを構築します。
     * </p>
     * 
     * @param contextPath コンテキストパス。
     * @param method HTTPメソッド。
     * @param characterEncoding リクエストの文字エンコーディング。
     * @param parameterMap 文字列型のリクエストパラメータが格納されているMap。
     * @param fileParameterMap FormFile型のリクエストパラメータが格納されているMap。
     * @param attributeContainer リクエストスコープの属性を保持するための属性コンテナ。
     * @param locale 現在のロケール。
     * @return 構築したRequestオブジェクト。
     */
    Request prepareForProcessing(String contextPath, String method,
            String characterEncoding, Map<String, String[]> parameterMap,
            Map<String, FormFile[]> fileParameterMap,
            AttributeContainer attributeContainer, Locale locale);

    /**
     * ディスパッチの処理を開始します。
     * <p>このメソッドはMatchedPathMappingを生成して
     * {@link #enterDispatch(Request, String, String, Dispatcher, MatchedPathMapping)}
     * を呼び出します。
     * </p>
     * 
     * @param request 現在のRequest。
     * @param path ディスパッチのパス。
     * @param queryString ディスパッチのクエリ文字列。nullを指定することもできます。
     * @param dispatcher ディスパッチを表すDispatcher。
     * @see Dispatch
     * @see Dispatcher
     */
    void enterDispatch(Request request, String path, String queryString,
            Dispatcher dispatcher);

    /**
     * ディスパッチの処理を開始します。
     * 
     * @param request 現在のRequest。
     * @param path ディスパッチのパス。
     * @param queryString ディスパッチのクエリ文字列。nullを指定することもできます。
     * @param dispatcher ディスパッチを表すDispatcher。
     * @param matched ディスパッチのパスとパスマッピングのマッチング情報を持つMatchedPathMappingオブジェクト。
     * nullを指定してはいけません。
     * @see Dispatch
     * @see Dispatcher
     */
    void enterDispatch(Request request, String path, String queryString,
            Dispatcher dispatcher, MatchedPathMapping matched);

    /**
     * RequestオブジェクトからResponseオブジェクトを構築します。
     * 
     * @param request 現在のRequestオブジェクト。
     * @return 構築したResponseオブジェクト。nullを返すことはありません。
     * @throws PageNotFoundException リクエストに対応するページが見つからなかった場合。
     * @throws PermissionDeniedException リクエストに対応するページを処理する権限がなかった場合。
     */
    Response processRequest(Request request) throws PageNotFoundException,
            PermissionDeniedException;

    /**
     * ResponseオブジェクトからHTTPレスポンスを構築します。
     * 
     * @param context ServletContextオブジェクト。
     * @param httpRequest HttpServletRequestオブジェクト。
     * @param httpResponse HttpServletResponseオブジェクト。
     * @param request Requestオブジェクト。
     * @param response Responseオブジェクト。
     * @return 構築したHTTPレスポンスを表すHttpServletResponseFilterオブジェクト。
     * @throws IOException I/Oエラーが発生した場合。
     * @throws ServletException サーブレットエラーが発生した場合。
     */
    HttpServletResponseFilter processResponse(ServletContext context_,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response) throws IOException,
            ServletException;

    /**
     * ディスパッチの処理を終了します。
     * 
     * @param request 現在のRequest。
     * @see Dispatch
     * @see Dispatcher
     */
    void leaveDispatch(Request request);

    /**
     * このオブジェクトを破棄するための処理を行ないます。
     */
    void destroy();

    /**
     * サーブレットのインクルード処理を行なう前に属性を退避します。
     * 
     * @param attributeContainer コンテナ。
     * @return 退避した情報が格納されているオブジェクト。
     */
    Object backupForInclusion(AttributeContainer attributeContainer);

    /**
     * サーブレットのインクルード処理が終了した後に退避情報を復元します。
     * 
     * @param attributeContainer 退避情報の復元先である属性コンテナ。
     * @param backupped 退避した情報が格納されているオブジェクト。
     */
    void restoreForInclusion(AttributeContainer attributeContainer,
            Object backupped);

    /**
     * 現在のコンテキストに関連付けられたApplicationを返します。
     * <p>関連付けられていない場合はベースとなるApplicationを返します。
     * </p>
     * 
     * @return 現在のコンテキストに関連付けられたApplication。
     * 関連付けられていなければベースとなるApplication。
     * @see ApplicationManager#findContextApplication()
     */
    Application getApplication();

    /**
     * プロジェクトステータスを返します。
     * <p>プロジェクトステータスとはフレームワークの動作ステータスのことです。
     * このメソッドの返り値は、
     * 開発中を表す「delevop」、本番稼動中を表す「release」またはnullのいずれかです。
     * </p>
     * <p>プロジェクトステータスとアプリケーションの動作ステータスは異なることに注意して下さい。
     * </p>
     * 
     * @return プロジェクトステータス。
     */
    String getProjectStatus();

    /**
     * 現在処理中のアプリケーションが開発中であるとみなすかどうかを返します。
     * <p>このメソッドは、Ymir自体のプロジェクトステータスが開発中でかつ
     * 現在処理中のアプリケーションのステータスが開発中である場合にのみtrueを返します。
     * </p>
     *
     * @return 現在処理中のアプリケーションが開発中であるとみなすかどうか。
     */
    boolean isUnderDevelopment();

    /**
     * 例外の処理をします。
     * 
     * @param request 現在のRequestオブジェクト。
     * @param t スローされた例外。
     * @return 処理結果のResponseオブジェクト。
     */
    Response processException(Request request, Throwable t);

    /**
     * コンテナに登録されているYmirProcessInterceptorを返します。
     * 
     * @return コンテナに登録されているYmirProcessInterceptor。nullを返すことはありません。
     */
    YmirProcessInterceptor[] getYmirProcessInterceptors();

    /**
     * 指定されたディスパッチャの処理を開始するために、現在のRequestオブジェクトの状態を更新します。
     *  
     * @param request 現在のRequestオブジェクト。
     * @param httpRequest 現在のHttpServletRequestオブジェクト。
     * @param dispatcher ディスパッチャ。
     */
    void updateRequest(Request request, HttpServletRequest httpRequest,
            Dispatcher dispatcher);

    /**
     * 指定されたパスとHTTPメソッドに対応するMatchedPathMappingオブジェクトを構築して返します。
     * <p>マッチするPathMappingが見つからなかった場合はnullを返します。
     * </p>
     * 
     * @param path パス。末尾に「/」がついていてもついていなくても構いません。
     * @param method HTTPメソッド。
     * @return 構築したMatchedPathMappingオブジェクト。
     */
    MatchedPathMapping findMatchedPathMapping(String path, String method);
}
