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
package org.seasar.ymir;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import junit.framework.TestCase;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.mock.servlet.MockHttpServletResponse;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletContext;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.impl.HttpServletRequestAttributeContainer;
import org.seasar.ymir.servlet.YmirListener;

abstract public class PageTestCase<P> extends TestCase {
    /** リクエストの処理が開始された直後であることを表す定数です。 */
    protected static final int STATUS_STARTED = 0;

    /** リクエストに関してメインの処理をするための準備が完了していることを表す定数です。 */
    protected static final int STATUS_PREPARED = 1;

    /** リクエストの処理が完了していることを表す定数です。 */
    protected static final int STATUS_PROCESSED = 2;

    private YmirListener ymirListener_;

    private S2Container container_;

    private Ymir ymir_;

    private MockServletContext application_;

    private int status_;

    private Locale locale_ = new Locale("");

    /**
     * テスト対象であるPageクラスのクラスオブジェクトを返します。
     * <p>テスト作成者はこのメソッドをオーバライドして適切な値を返すようにして下さい。
     * </p>
     *
     * @return テスト対象であるPageクラスのクラスオブジェクト。
     */
    abstract protected Class<P> getPageClass();

    /**
     * テストに使用されるServletContextオブジェクトを返します。
     *
     * @return ServletContextオブジェクト。
     */
    protected ServletContext getServletContext() {
        return application_;
    }

    /**
     * テストに使用されるS2Containerを返します。
     *
     * @return S2Containerオブジェクト。
     */
    protected S2Container getContainer() {
        return container_;
    }

    /**
     * リクエストを処理するためのロケールを返します。
     * <p>デフォルトでは、返されるロケールはデフォルトロケールです。
     * </p>
     *
     * @return ロケール。
     */
    protected Locale getLocale() {
        return locale_;
    }

    /**
     * リクエストを処理するためのロケールを設定します。
     * <p>リクエストを処理するためのロケールを変更したい場合は、<code>prepareForProcessing</code>
     * メソッドを呼び出す前にこのメソッドを呼び出してロケールを設定して下さい。
     * </p>
     *
     * @param locale ロケール。
     */
    protected void setLocale(Locale locale) {
        locale_ = locale;
    }

    /**
     * テスト時のコンテキストパスを返します。
     * <p>テスト時のコンテキストパスを変更したい場合はこのメソッドをオーバライドして下さい。
     * </p>
     *
     * @return コンテキストパス。
     */
    protected String getContextPath() {
        return "/context";
    }

    /**
     * テストメソッドを呼び出す前の処理を行ないます。
     * <p>このメソッドをオーバライドする場合は必ずsuperメソッドを呼び出して下さい。
     * </p>
     */
    public void setUp() {
        application_ = new MockServletContextImpl(getContextPath());
        application_.setInitParameter(YmirListener.CONFIG_PATH_KEY,
                "ymir.dicon");

        ymirListener_ = new YmirListener();
        ymirListener_.contextInitialized(new ServletContextEvent(application_));

        container_ = SingletonS2ContainerFactory.getContainer();
        ymir_ = (Ymir) container_.getComponent(Ymir.class);
    }

    /**
     * テストメソッドを呼び出した後の処理を行ないます。
     * <p>このメソッドをオーバライドする場合は必ずsuperメソッドを呼び出して下さい。
     * </p>
     */
    public void tearDown() {
        ymirListener_.contextDestroyed(new ServletContextEvent(application_));
        // TODO s2-pluggable-0.0.7以降は不要。
        SingletonS2ContainerFactory.setContainer(null);
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     * <p>リクエストパラメータとfileタイプのリクエストパラメータの指定がないとみなされます。
     * </p>
     *
     * @param path リクエストパス（コンテキストパス相対）。
     * @param httpMethod HTTPメソッド。Request.METHOD_XXXのいずれかを指定して下さい。
     * @return 構築されたRequestオブジェクト。
     */
    protected Request prepareForPrecessing(String path, String httpMethod) {
        return prepareForPrecessing(path, httpMethod,
                new HashMap<String, String[]>(),
                new HashMap<String, FormFile[]>());
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     * <p>fileタイプのリクエストパラメータの指定がないとみなされます。
     * </p>
     *
     * @param path リクエストパス（コンテキストパス相対）。
     * @param queryString クエリ文字列。「<code>a=b&c=d</code>」のように記述して下さい。
     * @param httpMethod HTTPメソッド。Request.METHOD_XXXのいずれかを指定して下さい。
     * @return 構築されたRequestオブジェクト。
     */
    protected Request prepareForPrecessing(String path, String httpMethod,
            String queryString) {
        return prepareForPrecessing(path, httpMethod,
                parseQueryString(queryString),
                new HashMap<String, FormFile[]>());
    }

    /**
     * クエリ文字列からMapを構築して返します。
     *
     * @param queryString クエリ文字列。nullの場合は空のMapを返します。
     * @return 構築したMap。
     */
    protected Map<String, String[]> parseQueryString(String queryString) {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        if (queryString != null) {
            int pre = 0;
            if (queryString.startsWith("?")) {
                pre = 1/*= "?".length() */;
            }
            int idx;
            while ((idx = queryString.indexOf('&', pre)) >= 0) {
                String segment = queryString.substring(pre, idx);
                int equal = segment.indexOf('=');
                if (equal >= 0) {
                    String name = segment.substring(0, equal);
                    String value = segment.substring(equal + 1);
                    String[] values = parameterMap.get(name);
                    if (values == null) {
                        values = new String[] { value };
                    } else {
                        values = (String[]) ArrayUtil.add(values, value);
                    }
                    parameterMap.put(name, values);
                }
                pre = idx + 1;
            }
            if (pre < queryString.length()) {
                String segment = queryString.substring(pre);
                int equal = segment.indexOf('=');
                if (equal >= 0) {
                    String name = segment.substring(0, equal);
                    String value = segment.substring(equal + 1);
                    String[] values = parameterMap.get(name);
                    if (values == null) {
                        values = new String[] { value };
                    } else {
                        values = (String[]) ArrayUtil.add(values, value);
                    }
                    parameterMap.put(name, values);
                }
            }
        }
        return parameterMap;
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     * <p>fileタイプのリクエストパラメータの指定がないとみなされます。
     * </p>
     *
     * @param path リクエストパス（コンテキストパス相対）。
     * @param parameterMap リクエストパラメータが格納されているMap。
     * @param httpMethod HTTPメソッド。Request.METHOD_XXXのいずれかを指定して下さい。
     * @return 構築されたRequestオブジェクト。
     */
    protected Request prepareForPrecessing(String path, String httpMethod,
            Map<String, String[]> parameterMap) {
        return prepareForPrecessing(path, httpMethod, parameterMap,
                new HashMap<String, FormFile[]>());
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     *
     * @param path リクエストパス（コンテキストパス相対）。
     * @param parameterMap リクエストパラメータが格納されているMap。
     * @param fileParameterMap fileタイプのリクエストパラメータが格納されているMap。
     * @param httpMethod HTTPメソッド。Request.METHOD_XXXのいずれかを指定して下さい。
     * @return 構築されたRequestオブジェクト。
     */
    protected Request prepareForPrecessing(String path, String httpMethod,
            Map<String, String[]> parameterMap,
            Map<String, FormFile[]> fileParameterMap) {
        MockHttpServletRequest httpRequest = new MockHttpServletRequestImpl(
                application_, path);
        MockHttpServletResponse httpResponse = new MockHttpServletResponseImpl(
                httpRequest);

        ExternalContext externalContext = container_.getExternalContext();
        externalContext.setRequest(httpRequest);
        externalContext.setResponse(httpResponse);

        status_ = STATUS_PREPARED;

        return ymir_.prepareForProcessing(getContextPath(), path, httpMethod,
                Request.DISPATCHER_REQUEST, parameterMap, fileParameterMap,
                new HttpServletRequestAttributeContainer(httpRequest),
                getLocale());
    }

    @SuppressWarnings("unchecked")
    protected P getPageComponent() {
        checkStatus(STATUS_PREPARED);
        return (P) container_.getComponent(getPageClass());
    }

    private void checkStatus(int status) {
        if (status_ < status) {
            throw new IllegalStateException("status " + status
                    + " is expected but the current status is " + status_);
        }
    }

    /**
     * 実際にリクエストを処理します。
     * <p>このメソッドを呼び出す前に必ず<code>prepareForProcessing</code>
     * メソッドを呼び出しておいて下さい。
     * </p>
     *
     * @param request Requestオブジェクト。
     * @return 処理結果を表すResponseオブジェクト。
     * @throws PermissionDeniedException 権限エラーが発生した場合。
     * @throws PageNotFoundException 指定されたリクエストパスに直接アクセスすることが禁止されている場合。
     */
    protected Response processRequest(Request request)
            throws PermissionDeniedException, PageNotFoundException {
        checkStatus(STATUS_PREPARED);

        status_ = STATUS_PROCESSED;

        return ymir_.processRequest(request);
    }

    /**
     * バリデーションエラー情報が格納されているNotesオブジェクトを返します。
     * <p>このメソッドは{@link #processRequest(Request)}メソッドの呼び出し後に
     * 呼び出して下さい。
     * </p>
     *
     * @param request Requestオブジェクト。
     * @return Notesオブジェクト。バリデーションエラーが発生しなかった場合はnullを返します。
     */
    protected Notes getNotes(Request request) {
        checkStatus(STATUS_PROCESSED);
        return (Notes) request.getAttribute(RequestProcessor.ATTR_NOTES);
    }
}
