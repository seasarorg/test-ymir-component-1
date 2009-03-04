package org.seasar.ymir;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.kvasir.util.el.VariableResolver;
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
     * {@link Request}に属性としてResponseオブジェクトをバインドする際のキー文字列です。
     */
    String ATTR_RESPONSE = Globals.IDPREFIX + ".response";

    /**
     * このオブジェクトを初期化します。
     */
    void init();

    /**
     * このオブジェクトを破棄するための処理を行ないます。
     */
    void destroy();

    void process(ServletContext servletContext, HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, Dispatcher dispatcher,
            String path, HttpMethod method,
            Map<String, FormFile[]> fileParameterMap, FilterChain chain)
            throws IOException, ServletException;

    /**
     * 指定されたパスとHTTPメソッドに対応するMatchedPathMappingオブジェクトを構築して返します。
     * <p>マッチするPathMappingが見つからなかった場合はnullを返します。
     * </p>
     * 
     * @param path パス。末尾に「/」がついていてもついていなくても構いません。
     * @param method HTTPメソッド。
     * @return 構築したMatchedPathMappingオブジェクト。
     */
    MatchedPathMapping findMatchedPathMapping(String path, HttpMethod method);

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
     * コンテナに登録されているYmirProcessInterceptorを返します。
     * 
     * @return コンテナに登録されているYmirProcessInterceptor。nullを返すことはありません。
     */
    YmirProcessInterceptor[] getYmirProcessInterceptors();

    /**
     * Pageクラスに対応するコンテキスト相対のパスを返します。
     * <p>Pageクラスに対応するパスが存在しない場合や、
     * Pageクラスからパスを特定できない場合はnullを返します。
     * </p>
     * 
     * @param pageClass Pageクラス。
     * @return コンテキスト相対パス。
     * @since 1.0.2
     */
    String getPathOfPageClass(Class<?> pageClass);

    /**
     * Pageクラス名に対応するコンテキスト相対のパスを返します。
     * <p>Pageクラス名に対応するパスが存在しない場合や、
     * Pageクラス名からパスを特定できない場合はnullを返します。
     * </p>
     * 
     * @param pageClassName Pageクラス名。
     * @return コンテキスト相対パス。
     * @since 1.0.2
     */
    String getPathOfPageClass(String pageClassName);

    /**
     * パスに対応するPageクラスを返します。
     * <p>パスに対応するPageクラスが存在しない場合はnullを返します。
     * </p>
     * 
     * @param path コンテキスト相対パス。
     * @param Pageクラス。
     * @since 1.0.2
     */
    Class<?> getPageClassOfPath(String path);

    /**
     * パスに対応するPageクラス名を返します。
     * <p>パスに対応するPageクラス名が存在しない場合はnullを返します。
     * </p>
     * 
     * @param path コンテキスト相対パス。
     * @param Pageクラス名。
     * @since 1.0.2
     */
    String getPageClassNameOfPath(String path);
}
