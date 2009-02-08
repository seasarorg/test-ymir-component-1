package org.seasar.ymir.testing;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;

import junit.framework.TestCase;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.cms.pluggable.ThreadContext;
import org.seasar.cms.pluggable.impl.ConfigurationImpl;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.kvasir.util.ClassUtils;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.kvasir.util.io.impl.FileResource;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.PageNotFoundRuntimeException;
import org.seasar.ymir.Path;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.impl.HttpServletRequestAttributeContainer;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.mock.servlet.MockHttpServletResponse;
import org.seasar.ymir.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.ymir.mock.servlet.MockHttpSession;
import org.seasar.ymir.mock.servlet.MockServletContext;
import org.seasar.ymir.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.servlet.YmirListener;
import org.seasar.ymir.util.ContainerUtils;
import org.seasar.ymir.util.ServletUtils;

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
abstract public class YmirTestCase extends TestCase {
    /** リクエストの処理が開始された直後であることを表す定数です。 */
    protected static final int STATUS_STARTED = 0;

    /** リクエストに関してメインの処理をするための準備が完了していることを表す定数です。 */
    protected static final int STATUS_PREPARED = 1;

    /** リクエストの処理が完了していることを表す定数です。 */
    protected static final int STATUS_PROCESSED = 2;

    private static final String DEFAULT_WEBAPPROOT = "src/main/webapp";

    private YmirListener ymirListener_;

    private S2Container container_;

    private Ymir ymir_;

    private MockServletContext application_;

    private int status_;

    private Locale locale_ = new Locale("");

    private String characterEncoding_ = "UTF-8";

    private MockHttpServletRequest httpRequest_;

    private MockHttpServletResponse httpResponse_;

    private Request request_;

    private String webappRoot_ = DEFAULT_WEBAPPROOT;

    /**
     * テストに使用されるYmirオブジェクトを返します。
     *
     * @return Ymirオブジェクト。
     * @since 1.0.2
     */
    protected Ymir getYmir() {
        return ymir_;
    }

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
     * <p>返されるコンテナは、コンテナグラフのルートコンテナです。
     * </p>
     *
     * @return S2Containerオブジェクト。
     */
    protected S2Container getContainer() {
        return container_;
    }

    /**
     * テストに使用されるS2Containerのうち、指定されたパスに対応するものを返します。
     * <p>既に構築済みのコンテナグラフのノードのうち、指定されたパスに対応するコンテナを返します。
     * コンテナグラフに含まれていないコンテナを返すことはありません。
     * </p>
     * 
     * @param path パス。nullを指定してはいけません。
     * @return S2Containerオブジェクト。
     * @throws IllegalArgumentException S2Containerが見つからなかった場合。
     */
    protected S2Container getContainer(String path)
            throws IllegalArgumentException {
        S2Container container = findContainer(path, getContainer());
        if (container == null) {
            throw new IllegalArgumentException("Can't find container: " + path);
        }
        return container;
    }

    private S2Container findContainer(String path, S2Container container) {
        if (container.getPath().endsWith("/" + path)) {
            return container;
        }

        int size = container.getChildSize();
        for (int i = 0; i < size; i++) {
            S2Container c = findContainer(path, container.getChild(i));
            if (c != null) {
                return c;
            }
        }
        return null;
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
     * <p>デフォルトでは、返されるロケールは空のロケールです。
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
     * リクエストの文字エンコーディングを返します。
     * <p>デフォルトでは、返される文字エンコーディングはUTF-8です。
     * </p>
     *
     * @return 文字エンコーディング。
     */
    protected String getCharacterEncoding() {
        return characterEncoding_;
    }

    /**
     * リクエストの文字エンコーディングを設定します。
     * <p>リクエストの文字エンコーディングを変更したい場合は、<code>prepareForProcessing</code>
     * メソッドを呼び出す前にこのメソッドを呼び出して文字エンコーディングを設定して下さい。
     * </p>
     *
     * @param characterEncoding 文字エンコーディング。
     */
    protected void setCharacterEncoding(String characterEncoding) {
        characterEncoding_ = characterEncoding;
    }

    protected void setWebappRoot(String webappRoot) {
        webappRoot_ = webappRoot;
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
     * テスト時に追加で読み込ませたい設定ファイルのパスを返します。
     * <p>テスト時に設定ファイルを追加で読み込ませたい場合はこのメソッドをオーバライドして下さい。
     * </p>
     *
     * @return 設定ファイルのパスの配列。nullを返してはいけません。
     */
    protected String[] getAdditionalConfigPaths() {
        return new String[0];
    }

    /**
     * テストのための設定を追加します。
     * <p>テストのための設定を追加したい場合はこのメソッドをオーバライドして下さい。
     * </p>
     * <p>このメソッドの呼び出しは{@link #getAdditionalConfigPaths()}が返す
     * 追加の設定ファイルの読み込みよりも後です。
     * </p>
     * 
     * @param configuration Configurationオブジェクト。
     * @see #getAdditionalConfigPaths()
     */
    protected void setUpConfiguration(Configuration configuration) {
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
     * <p>このメソッドは<code>getHttpSession(false)</code>と同じです。
     * </p>
     * 
     * @return 現在のHttpSessionオブジェクト。
     * @see #getHttpSession(boolean)
     */
    protected MockHttpSession getHttpSession() {
        return getHttpSession(false);
    }

    /**
     * 現在のHttpSessionオブジェクトを返します。
     * <p><code>create</code>がtrueの場合、セッションが作成されていなければ作成します。
     * </p>
     * <p>このメソッドは<code>prepareForProcessing</code>メソッドの呼び出し後に呼び出して下さい。
     * </p>
     * 
     * @param create セッションを作成するかどうか。
     * @return 現在のHttpSessionオブジェクト。
     */
    protected MockHttpSession getHttpSession(boolean create) {
        return (MockHttpSession) getHttpServletRequest().getSession(create);
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
        URL resource = getClass().getClassLoader()
                .getResource("app.properties");
        if (resource == null) {
            throw new RuntimeException("'app.properties' not found.");
        }
        application_.setRoot(new FileResource(findWebappRoot(ClassUtils
                .getFileOfResource(resource).getParentFile())));
        application_.setInitParameter(YmirListener.CONFIG_PATH_KEY,
                "ymir.dicon");

        ymirListener_ = new YmirListener() {
            @Override
            protected void preInit(ServletContextEvent sce) {
                ConfigurationImpl configuration = (ConfigurationImpl) SingletonS2ContainerFactory
                        .getContainer().getComponent(ConfigurationImpl.class);
                for (String configPath : getAdditionalConfigPaths()) {
                    configuration.load(configPath);
                }
                setUpConfiguration(configuration);

                super.preInit(sce);
            }
        };
        ymirListener_.contextInitialized(new ServletContextEvent(application_));

        container_ = SingletonS2ContainerFactory.getContainer();
        ymir_ = (Ymir) container_.getComponent(Ymir.class);
    }

    File findWebappRoot(File dir) {
        return new File(findProjectRoot(dir), webappRoot_);
    }

    File findProjectRoot(final File file) {
        File f = file;
        do {
            if (new File(f, "pom.xml").exists()) {
                return f;
            }
        } while ((f = f.getParentFile()) != null);
        return file;
    }

    /**
     * テストメソッドを呼び出した後の処理を行ないます。
     * <p>このメソッドをオーバライドする場合は必ずsuperメソッドを呼び出して下さい。
     * </p>
     */
    public void tearDown() {
        ymirListener_.contextDestroyed(new ServletContextEvent(application_));
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     * <p><code>prepareForProcessing(path, HttpMethod.GET)</code>
     * と同じです。
     * </p>
     *
     * @param path リクエストパス（コンテキストパス相対）。
     * @return 構築されたRequestオブジェクト。
     */
    protected Request prepareForProcessing(String path) {
        return prepareForProcessing(path, HttpMethod.GET);
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     * <p>パスに「?a=b」のようにクエリ文字列を付与することでリクエストパラメータを指定することもできます。
     * </p>
     *
     * @param path リクエストパス（コンテキストパス相対）。
     * @param method HTTPメソッド。
     * @return 構築されたRequestオブジェクト。
     */
    protected Request prepareForProcessing(String path, HttpMethod method) {
        return prepareForProcessing(ServletUtils.getTrunk(path), method,
                ServletUtils.getQueryString(path));
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     * <p><code>prepareForProcessing(path, HttpMethod.GET, queryString)</code>
     * と同じです。
     * </p>
     *
     * @param path リクエストパス（コンテキストパス相対）。
     * @param queryString クエリ文字列。「<code>a=b&c=d</code>」のように記述して下さい。
     * @return 構築されたRequestオブジェクト。
     */
    protected Request prepareForProcessing(String path, String queryString) {
        return prepareForProcessing(path, HttpMethod.GET, queryString);
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     * <p>fileタイプのリクエストパラメータの指定がないとみなされます。
     * </p>
     *
     * @param path リクエストパス（コンテキストパス相対）。
     * @param method HTTPメソッド。
     * @param queryString クエリ文字列。「<code>a=b&c=d</code>」のように記述して下さい。
     * @return 構築されたRequestオブジェクト。
     */
    protected Request prepareForProcessing(String path, HttpMethod method,
            String queryString) {
        return prepareForProcessing(path, method,
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
                    String name;
                    String value;
                    try {
                        name = URLDecoder.decode(segment.substring(0, equal),
                                getCharacterEncoding());
                        value = URLDecoder.decode(segment.substring(equal + 1),
                                getCharacterEncoding());
                    } catch (UnsupportedEncodingException ex) {
                        throw new IORuntimeException(ex);
                    }
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
     * @param method HTTPメソッド。
     * @return 構築されたRequestオブジェクト。
     */
    protected Request prepareForProcessing(String path, HttpMethod method,
            Map<String, String[]> parameterMap) {
        return prepareForProcessing(path, method,
                parameterMap != null ? parameterMap
                        : new HashMap<String, String[]>(),
                new HashMap<String, FormFile[]>());
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     *
     * @param path リクエストパス（コンテキストパス相対）。
     * @param method HTTPメソッド。
     * @param dispatcher ディスパッチャ。通常はDispatcher.REQUESTを指定して下さい。
     * @param parameterMap リクエストパラメータが格納されているMap。
     * @param fileParameterMap fileタイプのリクエストパラメータが格納されているMap。
     * @return 構築されたRequestオブジェクト。
     */
    @SuppressWarnings("unchecked")
    protected Request prepareForProcessing(String path, HttpMethod method,
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
        httpRequest_ = newHttpServletRequest(application_, path, session);
        httpRequest_.getParameterMap().putAll(parameterMap);
        httpRequest_.setLocale(getLocale());
        httpResponse_ = newHttpServletResponse(httpRequest_);

        ContainerUtils.setRequest(container_, httpRequest_);
        ContainerUtils.setResponse(container_, httpResponse_);

        status_ = STATUS_PREPARED;

        request_ = ymir_.prepareForProcessing(getContextPath(), method,
                "UTF-8", parameterMap, fileParameterMap,
                new HttpServletRequestAttributeContainer(httpRequest_));

        String queryString = null;
        if (method == HttpMethod.GET && !parameterMap.isEmpty()) {
            queryString = new Path(path, parameterMap).getQueryString();
        }
        ymir_.enterDispatch(request_, path, queryString, Dispatcher.REQUEST);
        return request_;
    }

    final protected Class<?> getPageClass(String path) {
        Class<?> pageClass = ymir_.getPageClassOfPath(path);
        if (pageClass == null) {
            throw new IllegalArgumentException(
                    "Can't find page class corresponding path (" + path + ")");
        }
        return pageClass;
    }

    final protected String getPathOfPageClass(Class<?> pageClass) {
        String path = ymir_.getPathOfPageClass(pageClass);
        if (path == null) {
            throw new IllegalArgumentException(
                    "Can't find path corresponding page class ("
                            + pageClass.getName() + ")");
        }
        return path;
    }

    protected MockHttpServletResponse newHttpServletResponse(
            MockHttpServletRequest httpRequest) {
        return new MockHttpServletResponseImpl(httpRequest);
    }

    protected MockHttpServletRequest newHttpServletRequest(
            MockServletContext application, String path, MockHttpSession session) {
        return new MockHttpServletRequestImpl(application, path, session);
    }

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
     * <p>このメソッドは{@link #processRequest(Request, org.seasar.ymir.test.YmirTestCase.Test)}
     * でTestにnullを指定した場合と同じです。
     * </p>
     *
     * @param request Requestオブジェクト。
     * @return 処理結果を表すResponseオブジェクト。
     * @throws PermissionDeniedException 権限エラーが発生した場合。
     * @throws PageNotFoundRuntimeException 指定されたリクエストパスに直接アクセスすることが禁止されている場合。
     */
    protected Response processRequest(Request request)
            throws PermissionDeniedException, PageNotFoundRuntimeException {
        return processRequest(request, null);
    }

    /**
     * 実際にリクエストを処理します。
     * <p>このメソッドを呼び出す前に必ず<code>prepareForProcessing</code>
     * メソッドを呼び出しておいて下さい。
     * </p>
     *
     * @param request Requestオブジェクト。
     * @param test リクエストの処理中に実行するテスト。nullを指定することもできます。
     * @return 処理結果を表すResponseオブジェクト。
     * @throws PermissionDeniedException 権限エラーが発生した場合。
     * @throws PageNotFoundRuntimeException 指定されたリクエストパスに直接アクセスすることが禁止されている場合。
     */
    protected Response processRequest(Request request, Test test)
            throws PermissionDeniedException, PageNotFoundRuntimeException {
        checkStatus(STATUS_PREPARED);

        status_ = STATUS_PROCESSED;

        ThreadContext threadContext = (ThreadContext) container_
                .getComponent(ThreadContext.class);
        Response response = null;
        try {
            threadContext.setComponent(Request.class, request);

            response = ymir_.processRequest(request);

            if (test != null) {
                test.doTest();
            }

            return response;
        } finally {
            ymir_.leaveDispatch(request);

            if (response != null && response.getType() != ResponseType.FORWARD) {
                for (int i = 0; i < ymir_.getYmirProcessInterceptors().length; i++) {
                    ymir_.getYmirProcessInterceptors()[i]
                            .leavingRequest(request);
                }
            }
            threadContext.setComponent(Request.class, null);
        }
    }

    /**
     * 実際にリクエストを処理します。
     * <p>このメソッドは{@link #process(Request, org.seasar.ymir.test.YmirTestCase.Test)}
     * でTestにnullを指定した場合と同じです。
     * </p>
     *
     * @param request Requestオブジェクト。
     * @return 処理結果を表すHttpServletResponseFilterオブジェクト。
     * @throws PermissionDeniedException 権限エラーが発生した場合。
     * @throws PageNotFoundRuntimeException 指定されたリクエストパスに直接アクセスすることが禁止されている場合。
     * @throws ServletException レスポンスの処理中にスローされることがあります。
     * @throws IOException レスポンスの処理中にスローされることがあります。
     */
    protected HttpServletResponseFilter process(Request request)
            throws PermissionDeniedException, PageNotFoundRuntimeException,
            IOException, ServletException {
        return process(request, null);
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
     * @param test リクエストの処理中に実行するテスト。nullを指定することもできます。
     * @return 処理結果を表すHttpServletResponseFilterオブジェクト。
     * @throws PermissionDeniedException 権限エラーが発生した場合。
     * @throws PageNotFoundRuntimeException 指定されたリクエストパスに直接アクセスすることが禁止されている場合。
     * @throws ServletException レスポンスの処理中にスローされることがあります。
     * @throws IOException レスポンスの処理中にスローされることがあります。
     */
    protected HttpServletResponseFilter process(Request request, Test test)
            throws PermissionDeniedException, PageNotFoundRuntimeException,
            IOException, ServletException {
        checkStatus(STATUS_PREPARED);

        status_ = STATUS_PROCESSED;

        ThreadContext threadContext = (ThreadContext) container_
                .getComponent(ThreadContext.class);
        Response response = null;
        try {
            threadContext.setComponent(Request.class, request);

            response = ymir_.processRequest(request);
            HttpServletResponseFilter responseFilter = ymir_.processResponse(
                    application_, httpRequest_, httpResponse_, request,
                    response);

            if (test != null) {
                test.doTest();
            }

            return responseFilter;
        } catch (Throwable t) {
            ymir_.processResponse(application_, httpRequest_, httpResponse_,
                    request, ymir_.processException(request, t));
            return null;
        } finally {
            ymir_.leaveDispatch(request);

            if (response != null && response.getType() != ResponseType.FORWARD) {
                for (int i = 0; i < ymir_.getYmirProcessInterceptors().length; i++) {
                    ymir_.getYmirProcessInterceptors()[i]
                            .leavingRequest(request);
                }
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

    /**
     * テストを表す抽象クラスです。
     * <p>フレームワークがリクエストを処理している最中にテストを実施したい場合はこのクラスのサブクラスを作成して
     * 
     * @author YOKOTA Takehiko
     */
    abstract public class Test {
        protected final void doTest() {
            try {
                test();
            } catch (Error e) {
                throw e;
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        abstract protected void test() throws Throwable;
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     *
     * @param pageClass アクションを呼び出すPageクラス。
     * @param method HTTPメソッド。
     * @return 構築されたRequestオブジェクト。
     * @since 1.0.2
     */
    protected Request prepareForProcessing(Class<?> pageClass, HttpMethod method) {
        return prepareForProcessing(getPathOfPageClass(pageClass), method,
                (String) null);
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     *
     * @param pageClass アクションを呼び出すPageクラス。
     * @param method HTTPメソッド。
     * @param queryString クエリ文字列。「<code>a=b&c=d</code>」のように記述して下さい。
     * @return 構築されたRequestオブジェクト。
     * @since 1.0.2
     */
    protected Request prepareForProcessing(Class<?> pageClass,
            HttpMethod method, String queryString) {
        return prepareForProcessing(getPathOfPageClass(pageClass), method,
                queryString);
    }

    /**
     * Pageクラスのアクション呼び出しのための準備を行ないます。
     *
     * @param pageClass アクションを呼び出すPageクラス。
     * @param method HTTPメソッド。
     * @param param1Name 最初のリクエストパラメータの名前。
     * @param param1Value1 最初のリクエストパラメータの値。
     * StringかString[]かFormFileかFormFile[]の非null値を指定して下さい。
     * @param theOtherParams 残りのリクエストパラメータ。
     * 値は必ず偶数個（パラメータ名, パラメータ値のペア, ...）指定して下さい。
     * パラメータ名としてはStringの非null値を指定して下さい。
     * パラメータ値としてはStringかString[]かFormFileかFormFile[]の非null値を指定して下さい。
     * @return 構築されたRequestオブジェクト。
     * @since 1.0.2
     */
    protected Request prepareForProcessing(Class<?> pageClass,
            HttpMethod method, String param1Name, Object param1Value1,
            Object... theOtherParams) {
        Map<String, String[]> parameterMap = new LinkedHashMap<String, String[]>();
        Map<String, FormFile[]> fileParameterMap = new LinkedHashMap<String, FormFile[]>();
        addParameter(param1Name, param1Value1, parameterMap, fileParameterMap);
        if (theOtherParams != null) {
            if (theOtherParams.length % 2 == 1) {
                throw new IllegalArgumentException(
                        "The number of theOtherParams must be even");
            }
            for (int i = 0; i < theOtherParams.length; i += 2) {
                addParameter(theOtherParams[i], theOtherParams[i + 1],
                        parameterMap, fileParameterMap);
            }
        }
        return prepareForProcessing(getPathOfPageClass(pageClass), method,
                parameterMap, fileParameterMap);
    }

    private void addParameter(Object name, Object value,
            Map<String, String[]> parameterMap,
            Map<String, FormFile[]> fileParameterMap) {
        if (!(name instanceof String)) {
            throw new IllegalArgumentException(
                    "Parameter name must be a string: " + name);
        }
        String stringName = (String) name;
        if (value instanceof String) {
            String v = (String) value;
            addToMap(parameterMap, stringName, new String[] { v });
        } else if (value instanceof String[]) {
            String[] vs = (String[]) value;
            addToMap(parameterMap, stringName, vs);
        } else if (value instanceof FormFile) {
            FormFile v = (FormFile) value;
            addToMap(fileParameterMap, stringName, new FormFile[] { v });
        } else if (value instanceof FormFile[]) {
            FormFile[] vs = (FormFile[]) value;
            addToMap(fileParameterMap, stringName, vs);
        } else {
            throw new IllegalArgumentException(
                    "Parameter value must be a String, String[], FormFile or FormFile[]: "
                            + value);
        }
    }

    private <V> void addToMap(Map<String, V[]> map, String name, V[] values) {
        V[] vs = map.get(name);
        if (vs == null) {
            vs = values;
        } else {
            List<V> list = new ArrayList<V>();
            list.addAll(Arrays.asList(vs));
            list.addAll(Arrays.asList(values));
            @SuppressWarnings("unchecked")
            V[] newVs = list.toArray((V[]) Array.newInstance(vs.getClass()
                    .getComponentType(), 0));
            vs = newVs;
        }
        map.put(name, vs);
    }
}
