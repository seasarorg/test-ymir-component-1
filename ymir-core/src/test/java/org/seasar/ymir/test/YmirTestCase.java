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
package org.seasar.ymir.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.cms.pluggable.ThreadContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockHttpSession;
import org.seasar.framework.mock.servlet.MockServletContext;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.Notes;
import org.seasar.ymir.PageNotFoundException;
import org.seasar.ymir.Path;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.impl.HttpServletRequestAttributeContainer;
import org.seasar.ymir.servlet.YmirListener;
import org.seasar.ymir.test.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.test.mock.servlet.MockHttpServletResponse;
import org.seasar.ymir.test.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.ymir.test.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.util.ContainerUtils;

abstract public class YmirTestCase extends TestCase {
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

    private MockHttpServletRequest httpRequest_;

    private MockHttpServletResponse httpResponse_;

    private Request request_;

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
     * テストに使用されるS2Containerからコンポーネントを取り出して返します。
     *
     * @return コンポーネント。
     */
    protected Object getComponent(Object componentKey) {
        return getContainer().getComponent(componentKey);
    }

    /**
     * テストに使用されるS2Containerからコンポーネントを取り出して返します。
     *
     * @return コンポーネント。
     */
    @SuppressWarnings("unchecked")
    protected <T> T getComponent(Class<T> componentClass) {
        return (T) getContainer().getComponent(componentClass);
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
     * 主に単一画面のテスト用に、一時的に@Beginアノテーションのチェックを無効にします。
     * <p>conversationに参加している画面をテストする場合、その画面に@Beginがないと
     * テスト時に不正遷移エラーとみなされてしまいます。
     * これを避けるためにこのメソッドを呼び出して一時的に@Beginアノテーションのチェックを無効にして下さい。
     * このメソッドが呼び出されてから呼び出したテストメソッドが終了するまで@Beginアノテーションは無効になります。
     * </p>
     */
    protected void disableBeginCheck() {
        ((Configuration) getContainer().getComponent(Configuration.class))
                .setProperty(
                        org.seasar.ymir.conversation.Globals.APPKEY_DISABLEBEGINCHECK,
                        String.valueOf(true));
    }

    /**
     * 主に単一画面のテスト用に、ConversationScopeの代わりにSessionScopeを使用するようにします。
     * <p>conversationに参加している画面をテストする場合、その画面がconversationの途中だと
     * テスト時にconversation scopeが存在せずオブジェクトの保存や取り出しがうまくいかないことがあります。
     * これを避けるためにこのメソッドを呼び出して一時的にconversation scopeの代わりにsession scopeを使うようにして下さい。
     * このメソッドが呼び出されてから呼び出したテストメソッドが終了するまでは、
     * conversation scopeへのアクセスは全てsession scopeにスルーされるようになります。
     * </p>
     */
    protected void useSessionScopeAsConversationScope() {
        ((Configuration) getContainer().getComponent(Configuration.class))
                .setProperty(
                        org.seasar.ymir.conversation.Globals.APPKEY_USESESSIONSCOPEASCONVERSATIONSCOPE,
                        String.valueOf(true));
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
     * 現在のHttpServletRequestオブジェクトを返します。
     * <p>このメソッドは<code>prepareForProcessing</code>メソッドの呼び出し後に呼び出して下さい。
     * </p>
     * 
     * @return 現在のHttpServletRequestオブジェクト。
     */
    protected MockHttpServletRequest getHttpServletRequest() {
        return httpRequest_;
    }

    /**
     * 現在のHttpSessionオブジェクトを返します。
     * <p>セッションが作成されていない場合はnullを返します。
     * </p>
     * <p>このメソッドは<code>prepareForProcessing</code>メソッドの呼び出し後に呼び出して下さい。
     * </p>
     * 
     * @return 現在のHttpSessionオブジェクト。
     */
    protected MockHttpSession getHttpSession() {
        return (MockHttpSession) getHttpServletRequest().getSession(false);
    }

    /**
     * 現在のHttpServletResponseオブジェクトを返します。
     * <p>このメソッドは<code>prepareForProcessing</code>メソッドの呼び出し後に呼び出して下さい。
     * </p>
     * 
     * @return 現在のHttpServletResponseオブジェクト。
     */
    protected MockHttpServletResponse getHttpServletResponse() {
        return httpResponse_;
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
    protected Request prepareForProcessing(String path, String httpMethod) {
        return prepareForProcessing(path, httpMethod,
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
    protected Request prepareForProcessing(String path, String httpMethod,
            String queryString) {
        return prepareForProcessing(path, httpMethod,
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
    protected Request prepareForProcessing(String path, String httpMethod,
            Map<String, String[]> parameterMap) {
        return prepareForProcessing(path, httpMethod,
                parameterMap != null ? parameterMap
                        : new HashMap<String, String[]>(),
                new HashMap<String, FormFile[]>());
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     *
     * @param path リクエストパス（コンテキストパス相対）。
     * @param httpMethod HTTPメソッド。Request.METHOD_XXXのいずれかを指定して下さい。
     * @param dispatcher ディスパッチャ。通常はDispatcher.REQUESTを指定して下さい。
     * @param parameterMap リクエストパラメータが格納されているMap。
     * @param fileParameterMap fileタイプのリクエストパラメータが格納されているMap。
     * @return 構築されたRequestオブジェクト。
     */
    @SuppressWarnings("unchecked")
    protected Request prepareForProcessing(String path, String httpMethod,
            Map<String, String[]> parameterMap,
            Map<String, FormFile[]> fileParameterMap) {
        if (parameterMap == null) {
            parameterMap = new HashMap<String, String[]>();
        }
        if (fileParameterMap == null) {
            fileParameterMap = new HashMap<String, FormFile[]>();
        }

        MockHttpSession session = null;
        if (httpRequest_ != null) {
            session = (MockHttpSession) httpRequest_.getSession(false);
        }
        httpRequest_ = new MockHttpServletRequestImpl(application_, path,
                session);
        httpRequest_.getParameterMap().putAll(parameterMap);
        httpRequest_.setLocale(getLocale());
        httpResponse_ = new MockHttpServletResponseImpl(httpRequest_);

        ContainerUtils.setRequest(container_, httpRequest_);
        ContainerUtils.setResponse(container_, httpResponse_);

        status_ = STATUS_PREPARED;

        request_ = ymir_.prepareForProcessing(getContextPath(), httpMethod,
                "UTF-8", parameterMap, fileParameterMap,
                new HttpServletRequestAttributeContainer(httpRequest_),
                getLocale());

        String queryString = null;
        if (Request.METHOD_GET.equals(httpMethod) && !parameterMap.isEmpty()) {
            queryString = new Path(path, parameterMap).getQueryString();
        }
        ymir_.enterDispatch(request_, path, queryString, Dispatcher.REQUEST);
        return request_;
    }

    @SuppressWarnings("unchecked")
    protected void prepareForProcessing(String path, Dispatcher dispatcher) {
        checkStatus(STATUS_PROCESSED);

        ymir_.updateRequest(request_, httpRequest_, dispatcher);

        ContainerUtils.setRequest(container_, httpRequest_);
        ContainerUtils.setResponse(container_, httpResponse_);

        status_ = STATUS_PREPARED;

        ymir_.enterDispatch(request_, path, null, dispatcher);
    }

    protected void checkStatus(int status) {
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

        ThreadContext threadContext = (ThreadContext) container_
                .getComponent(ThreadContext.class);
        try {
            threadContext.setComponent(Request.class, request);

            return ymir_.processRequest(request);
        } finally {
            ymir_.leaveDispatch(request);

            for (int i = 0; i < ymir_.getYmirProcessInterceptors().length; i++) {
                ymir_.getYmirProcessInterceptors()[i].leavingRequest(request);
            }
            threadContext.setComponent(Request.class, null);
        }
    }

    /**
     * 実際にリクエストを処理します。
     * <p>リクエストを処理してレスポンスを生成し、生成したレスポンスを処理してHttpServletResponseFilterを生成して返します。
     * </p>
     * <p>このメソッドを呼び出す前に必ず<code>prepareForProcessing</code>
     * メソッドを呼び出しておいて下さい。
     * </p>
     *
     * @param request Requestオブジェクト。
     * @return 処理結果を表すHttpServletResponseFilterオブジェクト。
     * @throws PermissionDeniedException 権限エラーが発生した場合。
     * @throws PageNotFoundException 指定されたリクエストパスに直接アクセスすることが禁止されている場合。
     * @throws ServletException レスポンスの処理中にスローされることがあります。
     * @throws IOException レスポンスの処理中にスローされることがあります。
     */
    protected HttpServletResponseFilter process(Request request)
            throws PermissionDeniedException, PageNotFoundException,
            IOException, ServletException {
        checkStatus(STATUS_PREPARED);

        status_ = STATUS_PROCESSED;

        ThreadContext threadContext = (ThreadContext) container_
                .getComponent(ThreadContext.class);
        try {
            threadContext.setComponent(Request.class, request);

            return ymir_.processResponse(application_, httpRequest_,
                    httpResponse_, request, ymir_.processRequest(request));
        } finally {
            ymir_.leaveDispatch(request);

            for (int i = 0; i < ymir_.getYmirProcessInterceptors().length; i++) {
                ymir_.getYmirProcessInterceptors()[i].leavingRequest(request);
            }
            threadContext.setComponent(Request.class, null);
        }
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
