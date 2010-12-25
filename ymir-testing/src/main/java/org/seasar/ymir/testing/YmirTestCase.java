package org.seasar.ymir.testing;

import static org.seasar.ymir.Ymir.ATTR_REQUEST;
import static org.seasar.ymir.Ymir.ATTR_RESPONSE;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.TransactionManager;

import junit.framework.TestCase;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.cms.pluggable.impl.ConfigurationImpl;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.kvasir.util.ClassUtils;
import org.seasar.kvasir.util.io.impl.FileResource;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Path;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.id.action.Action;
import org.seasar.ymir.id.action.GetAction;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.mock.servlet.MockFilterChain;
import org.seasar.ymir.mock.servlet.MockFilterChainImpl;
import org.seasar.ymir.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.mock.servlet.MockHttpServletResponse;
import org.seasar.ymir.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.ymir.mock.servlet.MockHttpSession;
import org.seasar.ymir.mock.servlet.MockRequestDispatcherImpl;
import org.seasar.ymir.mock.servlet.MockServletContext;
import org.seasar.ymir.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.mock.servlet.RequestDispatcherFactory;
import org.seasar.ymir.servlet.YmirListener;
import org.seasar.ymir.util.ContainerUtils;
import org.seasar.ymir.util.ServletUtils;

/**
 * YmirのPageクラスをテストするためのTestCaseのベースとなるクラスです。
 * <p>Pageクラスをテストする場合はこのクラスのサブクラスとしてTestCaseを作成すると便利かもしれません。
 */
abstract public class YmirTestCase extends TestCase {
    private static final String DEFAULT_WEBAPPROOT = "src/main/webapp";

    private static final Object[] EMPTY_PARAMS = new Object[0];

    private String characterEncoding_ = "UTF-8";

    private String contextPath_ = "/context";

    private Locale locale_ = new Locale("");

    private String webappRoot_ = DEFAULT_WEBAPPROOT;

    private String[] additionalConfigPaths_;

    private S2Container container_;

    private YmirListener ymirListener_;

    private MockServletContext servletContext_;

    private Ymir ymir_;

    private MockHttpServletRequest httpRequest_;

    private MockHttpServletResponse httpResponse_;

    /**
     * テストの実行環境を設定します。
     * <p>テストの実行環境を変更したい場合は、このメソッドをオーバライドして変更のための処理を記述して下さい。
     * </p>
     */
    public void setUpEnvironment() {
    }

    /**
     * リクエストの文字エンコーディングを返します。
     * <p>デフォルトでは、返される文字エンコーディングはUTF-8です。
     * </p>
     *
     * @return 文字エンコーディング。
     */
    public String getCharacterEncoding() {
        return characterEncoding_;
    }

    /**
     * リクエストの文字エンコーディングを設定します。
     * <p>このメソッドは{@link #setUpEnvironment()}の中で呼び出して下さい。
     * </p>
     *
     * @param characterEncoding 文字エンコーディング。
     */
    public void setCharacterEncoding(String characterEncoding) {
        characterEncoding_ = characterEncoding;
    }

    /**
     * テスト時のコンテキストパスを返します。
     *
     * @return コンテキストパス。
     */
    public String getContextPath() {
        return contextPath_;
    }

    /**
     * テスト時のコンテキストパスを設定します。
     * <p>このメソッドは{@link #setUpEnvironment()}の中で呼び出して下さい。
     * </p>
     *
     * @param contextPath コンテキストパス。
     */
    public void setContextPath(String contextPath) {
        contextPath_ = contextPath;
    }

    /**
     * リクエストを処理するためのロケールを返します。
     * <p>デフォルトでは、返されるロケールは空のロケールです。
     * </p>
     *
     * @return ロケール。
     */
    public Locale getLocale() {
        return locale_;
    }

    /**
     * リクエストを処理するためのロケールを設定します。
     * <p>このメソッドは{@link #setUpEnvironment()}の中で呼び出して下さい。
     * </p>
     *
     * @param locale ロケール。
     */
    public void setLocale(Locale locale) {
        locale_ = locale;
    }

    /**
     * プロジェクトを開発時にWebアプリケーションサーバにデプロイする際のルートディレクトリを設定します。
     * <p>デフォルトは「src/main/webapp」です。
     * </p>
     * <p>このメソッドは{@link #setUpEnvironment()}の中で呼び出して下さい。
     * </p>
     * 
     * @param webappRoot Webアプリケーションとして動作させるためのルートディレクトリ。
     */
    public void setWebappRoot(String webappRoot) {
        webappRoot_ = webappRoot;
    }

    /**
     * テスト時に追加で読み込ませたい設定ファイルのパスを返します。
     *
     * @return 設定ファイルのパスの配列。
     */
    public String[] getAdditionalConfigPaths() {
        return additionalConfigPaths_;
    }

    /**
     * テスト時に追加で読み込ませたい設定ファイルのパスを設定します。
     * <p>このメソッドは{@link #setUpEnvironment()}の中で呼び出して下さい。
     * </p>
     *
     * @param additionalConfigPaths 設定ファイルのパスの配列。
     */
    public void setAdditionalConfigPaths(String... additionalConfigPaths) {
        additionalConfigPaths_ = additionalConfigPaths;
    }

    /**
     * テストのための設定を追加します。
     * <p>テストのための設定を追加したい場合はこのメソッドをオーバライドして下さい。
     * </p>
     * 
     * @param configuration Configurationオブジェクト。
     */
    public void setUpConfiguration(Configuration configuration) {
    }

    /**
     * 主に単一画面のテスト用に、一時的に@Beginアノテーションのチェックを無効にします。
     * <p>conversationに参加している画面をテストする場合、その画面に@Beginがないと
     * テスト時に不正遷移エラーとみなされてしまいます。
     * これを避けるためにこのメソッドを呼び出して一時的に@Beginアノテーションのチェックを無効にして下さい。
     * このメソッドが呼び出されてから呼び出したテストメソッドが終了するまで@Beginアノテーションは無効になります。
     * </p>
     * <p>このメソッドは{@link #setUpConfiguration(Configuration)}の中で呼び出して下さい。
     * </p>
     */
    public void disableBeginCheck(Configuration configuration) {
        configuration
                .setProperty(
                        org.seasar.ymir.conversation.Globals.APPKEY_CORE_CONVERSATION_DISABLEBEGINCHECK,
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
     * <p>このメソッドは{@link #setUpConfiguration(Configuration)}の中で呼び出して下さい。
     * </p>
     */
    public void useSessionScopeAsConversationScope(Configuration configuration) {
        configuration
                .setProperty(
                        org.seasar.ymir.conversation.Globals.APPKEY_CORE_CONVERSATION_USESESSIONSCOPEASCONVERSATIONSCOPE,
                        String.valueOf(true));
    }

    @Override
    public void runBare() throws Throwable {
        setUpYmir();
        try {
            setUp();
            try {
                runTest();
            } finally {
                tearDown();
            }
        } finally {
            tearDownYmir();
        }
    }

    @Override
    protected void runTest() throws Throwable {
        TransactionManager tm = null;
        if (needTransaction()) {
            try {
                tm = getComponent(TransactionManager.class);
                tm.begin();
            } catch (Throwable t) {
                System.err.println(t);
            }
        }
        try {
            super.runTest();
        } finally {
            if (tm != null) {
                tm.rollback();
            }
        }
    }

    protected boolean needTransaction() {
        return getName().endsWith("Tx");
    }

    /**
     * テストメソッドを呼び出す前の処理を行ないます。
     */
    protected void setUpYmir() {
        servletContext_ = new MockServletContextImpl(getContextPath());
        servletContext_
                .setRequestDispatcherFactory(new InternalRequestDispatcherFactory());
        URL resource = getClass().getClassLoader()
                .getResource("app.properties");
        if (resource == null) {
            throw new RuntimeException("'app.properties' not found.");
        }
        servletContext_.setRoot(new FileResource(findWebappRoot(ClassUtils
                .getFileOfResource(resource).getParentFile())));
        servletContext_.setInitParameter(YmirListener.CONFIG_PATH_KEY,
                "ymir.dicon");

        ymirListener_ = new YmirListener() {
            @Override
            public void preInit(ServletContextEvent sce) {
                ConfigurationImpl configuration = (ConfigurationImpl) SingletonS2ContainerFactory
                        .getContainer().getComponent(ConfigurationImpl.class);
                if (additionalConfigPaths_ != null) {
                    for (String configPath : additionalConfigPaths_) {
                        configuration.load(configPath);
                    }
                }
                setUpConfiguration(configuration);

                super.preInit(sce);
            }
        };
        ymirListener_.contextInitialized(new ServletContextEvent(
                servletContext_));

        container_ = SingletonS2ContainerFactory.getContainer();
        ymir_ = YmirContext.getYmir();
    }

    public void setUp() {
        // 後方互換性のためこうしている。
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
     */
    protected void tearDownYmir() {
        ymirListener_
                .contextDestroyed(new ServletContextEvent(servletContext_));
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
    public S2Container getContainer(String path)
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
    public Object getComponent(Object componentKey) {
        return getContainer().getComponent(componentKey);
    }

    /**
     * テストに使用されるS2Containerからコンポーネントを取り出して返します。
     *
     * @return コンポーネント。
     */
    @SuppressWarnings("unchecked")
    public <T> T getComponent(Class<T> componentClass) {
        return (T) getContainer().getComponent(componentClass);
    }

    /**
     * テストに使用されるS2Containerを返します。
     * <p>返されるコンテナは、コンテナグラフのルートコンテナです。
     * </p>
     *
     * @return S2Containerオブジェクト。
     */
    public S2Container getContainer() {
        return container_;
    }

    /**
     * テストに使用されるYmirオブジェクトを返します。
     *
     * @return Ymirオブジェクト。
     * @since 1.0.2
     */
    public Ymir getYmir() {
        return ymir_;
    }

    /**
     * テストに使用されるServletContextオブジェクトを返します。
     *
     * @return ServletContextオブジェクト。
     */
    public ServletContext getServletContext() {
        return servletContext_;
    }

    /**
     * 現在のHttpServletRequestオブジェクトを返します。
     * <p>このメソッドは<code>process</code>メソッドの引数に渡す
     * {@link RequestInitializer}の<code>initialize</code>
     * メソッドの中、または<code>process</code>メソッドの呼び出し後に呼び出して下さい。
     * </p>
     * 
     * @return 現在のHttpServletRequestオブジェクト。
     */
    public MockHttpServletRequest getHttpServletRequest() {
        return httpRequest_;
    }

    /**
     * 現在のHttpSessionオブジェクトを返します。
     * <p>このメソッドは<code>getHttpSession(true)</code>と同じです。
     * </p>
     * 
     * @return 現在のHttpSessionオブジェクト。
     * @see #getHttpSession(boolean)
     */
    public MockHttpSession getHttpSession() {
        return getHttpSession(true);
    }

    /**
     * 現在のHttpSessionオブジェクトを返します。
     * <p><code>create</code>がtrueの場合、セッションが作成されていなければ作成します。
     * </p>
     * <p>このメソッドは<code>process</code>メソッドの引数に渡す
     * {@link RequestInitializer}の<code>initialize</code>
     * メソッドの中、または<code>process</code>メソッドの呼び出し後に呼び出して下さい。
     * </p>
     * 
     * @param create セッションを作成するかどうか。
     * @return 現在のHttpSessionオブジェクト。
     */
    public MockHttpSession getHttpSession(boolean create) {
        return (MockHttpSession) getHttpServletRequest().getSession(create);
    }

    /**
     * 現在のHttpServletResponseオブジェクトを返します。
     * <p>このメソッドは<code>process</code>メソッドの引数に渡す
     * {@link RequestInitializer}の<code>initialize</code>
     * メソッドの中、または<code>process</code>メソッドの呼び出し後に呼び出して下さい。
     * </p>
     * 
     * @return 現在のHttpServletResponseオブジェクト。
     */
    public MockHttpServletResponse getHttpServletResponse() {
        return httpResponse_;
    }

    /**
     * Ymirにリクエストを処理させます。
     * <p>テストケースのテストメソッドからこのメソッドを呼び出すことで、
     * Ymirにリクエストを処理させることができます。
     * </p>
     * <p>通常の（文字列型の）リクエストパラメータは引数で指定できません。
     * 予め{@link #acceptRequest(String, HttpMethod, RequestInitializer)}
     * を使ってHttpServletRequestに設定しておいて下さい。
     * </p>
     * <p>dispatcherとして{@link Dispatcher#REQUEST}以外を指定することで、
     * クライアントからのリクエストだけでなくforwardやincludeといった
     * サーブレットコンテナの中でのディスパッチを処理させることもできます。
     * </p>
     * 
     * @param servletContext ServletContextオブジェクト。
     * @param httpRequest HttpServletRequestオブジェクト。
     * @param httpResponse HttpServletResponseオブジェクト。
     * @param dispatcher Dispatcherオブジェクト。
     * @param path コンテキスト相対のリクエストパス。リクエストパラメータを付与することはできません。
     * @param method HTTPメソッド。
     * @param fileParameterMap ファイル型のリクエストパラメータを持つMap。
     * nullを指定することもできます。
     * @param chain Ymirの処理が終わった後に処理を渡すための{@link FilterChain}オブジェクト。
     * nullを指定してはいけません。
     * @throws IOException I/Oエラーが発生した場合。
     * @throws ServletException サーブレットエラーが発生した場合。
     */
    public void process(ServletContext servletContext,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Dispatcher dispatcher, String path, HttpMethod method,
            Map<String, FormFile[]> fileParameterMap, FilterChain chain)
            throws IOException, ServletException {
        ymir_.process(servletContext, httpRequest, httpResponse, dispatcher,
                path, method, fileParameterMap, chain);
    }

    public FilterChain process(String path) throws IOException,
            ServletException {
        return process(path, HttpMethod.GET, (RequestInitializer) null, null,
                (String) null, EMPTY_PARAMS);
    }

    public FilterChain process(String path, String param, Object... params)
            throws IOException, ServletException {
        return process(path, HttpMethod.GET, (RequestInitializer) null, null,
                param, params);
    }

    public FilterChain process(String path,
            Class<? extends GetAction> actionInterface, Object... params)
            throws IOException, ServletException {
        return process(path, HttpMethod.GET, (RequestInitializer) null, null,
                actionInterface, params);
    }

    public FilterChain process(String path, HttpMethod method)
            throws IOException, ServletException {
        return process(path, method, (RequestInitializer) null, null,
                (String) null, EMPTY_PARAMS);
    }

    public FilterChain process(String path, HttpMethod method, String param,
            Object... params) throws IOException, ServletException {
        return process(path, method, (RequestInitializer) null, null, param,
                params);
    }

    public FilterChain process(String path, HttpMethod method,
            Class<? extends GetAction> actionInterface, Object... params)
            throws IOException, ServletException {
        return process(path, method, (RequestInitializer) null, null,
                actionInterface, params);
    }

    public FilterChain process(String path, RequestInitializer initializer)
            throws IOException, ServletException {
        return process(path, HttpMethod.GET, initializer, null, (String) null,
                EMPTY_PARAMS);
    }

    public FilterChain process(String path, RequestInitializer initializer,
            String param, Object... params) throws IOException,
            ServletException {
        return process(path, HttpMethod.GET, initializer, null, param, params);
    }

    public FilterChain process(String path, RequestInitializer initializer,
            Class<? extends GetAction> actionInterface, Object... params)
            throws IOException, ServletException {
        return process(path, HttpMethod.GET, initializer, null,
                actionInterface, params);
    }

    public FilterChain process(String path, MockFilterChain chain)
            throws IOException, ServletException {
        return process(path, HttpMethod.GET, (RequestInitializer) null, chain,
                (String) null, EMPTY_PARAMS);
    }

    public FilterChain process(String path, MockFilterChain chain,
            String param, Object... params) throws IOException,
            ServletException {
        return process(path, HttpMethod.GET, (RequestInitializer) null, chain,
                param, params);
    }

    public FilterChain process(String path, MockFilterChain chain,
            Class<? extends GetAction> actionInterface, Object... params)
            throws IOException, ServletException {
        return process(path, HttpMethod.GET, (RequestInitializer) null, chain,
                actionInterface, params);
    }

    public FilterChain process(String path, HttpMethod method,
            RequestInitializer initializer, MockFilterChain chain)
            throws IOException, ServletException {
        return process(path, method, initializer, chain, (String) null,
                EMPTY_PARAMS);
    }

    /**
     * Ymirにリクエストを処理させます。
     * <p>テストケースのテストメソッドからこのメソッドを呼び出すことで、
     * Ymirにリクエストを処理させることができます。
     * </p>
     * 
     * @param path コンテキスト相対のリクエストパス。クエリストリングを付与することもできます。
     * @param method HTTPメソッド。
     * @param initializer RequestInitializerオブジェクト。
     * nullを指定することもできます。
     * リクエストを受け付けた直後になんらかの初期化処理をしたい場合はinitializerを指定して下さい。
     * @param chain Ymirの処理が終わった後に処理を渡すための{@link FilterChain}オブジェクト。
     * nullを指定してはいけません。
     * @param param 1つめのリクエストパラメータの名前。
     * @param params 1つめのリクエストパラメータの値、2つめのリクエストパラメータの名前、
     * 2つめのリクエストパラメータの値、…の配列。
     * @return Ymirの処理の後FilterChainが呼び出された場合、引数で与えたFilterChainオブジェクト。
     * Ymirがレスポンスを自前で処理した場合などFilterChainが呼び出されなかった場合、null。
     * @throws IOException I/Oエラーが発生した場合。
     * @throws ServletException サーブレットエラーが発生した場合。
     */
    public FilterChain process(String path, HttpMethod method,
            RequestInitializer initializer, MockFilterChain chain,
            String param, Object... params) throws IOException,
            ServletException {
        if (chain == null) {
            chain = new MockFilterChainImpl();
        }

        List<Object> paramList = new ArrayList<Object>();
        if (param != null) {
            paramList.add(param);
        }
        if (params != null) {
            paramList.addAll(Arrays.asList(params));
        }
        params = paramList.toArray();

        Map<String, String[]> parameterMap = new LinkedHashMap<String, String[]>();
        Map<String, FormFile[]> fileParameterMap = new LinkedHashMap<String, FormFile[]>();
        for (int i = 0; i < params.length; i += 2) {
            Object value;
            if (i + 1 < params.length) {
                value = params[i + 1];
            } else {
                value = "";
            }
            ServletUtils.addParameter(params[i], value, parameterMap,
                    fileParameterMap);
        }

        Path p = new Path(path, getCharacterEncoding());
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            for (String value : entry.getValue()) {
                p.addParameter(entry.getKey(), value);
            }
        }
        String actualPath = p.asString();

        acceptRequest(actualPath, method, initializer);

        process(getServletContext(), getHttpServletRequest(),
                getHttpServletResponse(), Dispatcher.REQUEST, ServletUtils
                        .getNativePath(getHttpServletRequest()), method,
                fileParameterMap, chain);
        if (chain.isCalled()) {
            return chain;
        } else {
            return null;
        }
    }

    public FilterChain process(String path, HttpMethod method,
            RequestInitializer initializer, MockFilterChain chain,
            Class<? extends GetAction> actionInterface, Object... params)
            throws IOException, ServletException {
        Object[] pms = new Object[1 + params.length];
        pms[0] = "";
        System.arraycopy(params, 0, pms, 1, params.length);

        try {
            return process(path, method, initializer, chain,
                    (String) actionInterface.getField(Action.FIELD_KEY).get(
                            null), pms);
        } catch (Throwable t) {
            throw new RuntimeException(
                    "Cannot get action key from action method '"
                            + actionInterface
                            + "'. Try to re-generate Page class.", t);
        }
    }

    public FilterChain toTheEndOf(FilterChain chain) throws IOException,
            ServletException {
        if (chain != null) {
            return chain;
        }
        MockHttpServletResponse httpResponse = getHttpServletResponse();
        if (httpResponse == null) {
            // まだprocessが呼ばれていない。
            return chain;
        }
        String redirectPath = httpResponse.getRedirectPath();
        if (redirectPath == null) {
            return chain;
        }
        if (!redirectPath.startsWith(getContextPath() + "/")) {
            return chain;
        }
        return toTheEndOf(process(redirectPath.substring(getContextPath()
                .length())));
    }

    public FilterChain process(Class<?> pageClass) throws IOException,
            ServletException {
        return process(getPathOfPageClass(pageClass));
    }

    public FilterChain process(Class<?> pageClass, String param,
            Object... params) throws IOException, ServletException {
        return process(getPathOfPageClass(pageClass), param, params);
    }

    public FilterChain process(Class<?> pageClass,
            Class<? extends GetAction> actionInterface, Object... params)
            throws IOException, ServletException {
        return process(getPathOfPageClass(pageClass), actionInterface, params);
    }

    public FilterChain process(Class<?> pageClass, HttpMethod method)
            throws IOException, ServletException {
        return process(getPathOfPageClass(pageClass), method);
    }

    public FilterChain process(Class<?> pageClass, HttpMethod method,
            String param, Object... params) throws IOException,
            ServletException {
        return process(getPathOfPageClass(pageClass), method, param, params);
    }

    public FilterChain process(Class<?> pageClass,
            RequestInitializer initializer) throws IOException,
            ServletException {
        return process(getPathOfPageClass(pageClass), initializer);
    }

    public FilterChain process(Class<?> pageClass,
            RequestInitializer initializer, String param, Object... params)
            throws IOException, ServletException {
        return process(getPathOfPageClass(pageClass), initializer, param,
                params);
    }

    public FilterChain process(Class<?> pageClass, MockFilterChain chain)
            throws IOException, ServletException {
        return process(getPathOfPageClass(pageClass), chain);
    }

    public FilterChain process(Class<?> pageClass, MockFilterChain chain,
            String param, Object... params) throws IOException,
            ServletException {
        return process(getPathOfPageClass(pageClass), chain, param, params);
    }

    public FilterChain process(Class<?> pageClass, HttpMethod method,
            RequestInitializer initializer, MockFilterChain chain)
            throws IOException, ServletException {
        return process(getPathOfPageClass(pageClass), method, initializer,
                chain);
    }

    public FilterChain process(Class<?> pageClass, HttpMethod method,
            RequestInitializer initializer, MockFilterChain chain,
            String param, Object... params) throws IOException,
            ServletException {
        return process(getPathOfPageClass(pageClass), method, initializer,
                chain, param, params);
    }

    /**
     * リクエストを受け付けます。
     * <p>引数で指定されたリクエストを受け付け、
     * HttpServletRequestとHttpServletResponseを構築して
     * オブジェクト内部に設定します。
     * 設定されたHttpServletRequestとHttpServletResponseはそれぞれ
     * {@link #getHttpServletRequest()}と{@link #getHttpServletResponse()}
     * で取り出すことができます。
     * </p>
     * <p>リクエストを受け付けた直後になんらかの初期化処理をしたい場合は
     * requestInitializerを指定して下さい。
     * </p>
     * 
     * @param path コンテキスト相対のリクエストパス。クエリストリングを付与することもできます。
     * @param method HTTPメソッド。
     * @param requestInitializer RequestInitializerオブジェクト。
     * nullを指定することもできます。
     */
    public void acceptRequest(String path, HttpMethod method,
            RequestInitializer requestInitializer) {
        MockHttpSession session = null;
        if (httpRequest_ != null) {
            session = (MockHttpSession) httpRequest_.getSession(false);
        }
        httpRequest_ = newHttpServletRequest(servletContext_, method, path,
                session);
        try {
            httpRequest_.setCharacterEncoding(getCharacterEncoding());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        httpRequest_.setLocale(getLocale());
        httpRequest_
                .setRequestDispatcherFactory(new InternalRequestDispatcherFactory());
        httpResponse_ = newHttpServletResponse(httpRequest_);

        ContainerUtils.setRequest(container_, httpRequest_);
        ContainerUtils.setResponse(container_, httpResponse_);

        if (requestInitializer != null) {
            requestInitializer.initialize();
        }
    }

    protected class InternalRequestDispatcherFactory implements
            RequestDispatcherFactory {
        public RequestDispatcher newInstance(String path,
                HttpServletRequest request) {
            return new InternalRequestDispatcher(path, request);
        }
    }

    protected class InternalRequestDispatcher extends MockRequestDispatcherImpl {
        public InternalRequestDispatcher(String path, HttpServletRequest request) {
            super(path, request);
        }

        @Override
        public void forward(ServletRequest request, ServletResponse response)
                throws ServletException, IOException {
            super.forward(request, response);

            process(servletContext_, httpRequest_, httpResponse_,
                    Dispatcher.FORWARD, ServletUtils
                            .getNativePath(getHttpServletRequest()), HttpMethod
                            .enumOf(httpRequest_.getMethod()), null,
                    new MockFilterChainImpl());
        }

        @Override
        public void include(ServletRequest request, ServletResponse response)
                throws ServletException, IOException {
            super.include(request, response);

            process(servletContext_, httpRequest_, httpResponse_,
                    Dispatcher.INCLUDE, ServletUtils
                            .getNativePath(getHttpServletRequest()), HttpMethod
                            .enumOf(httpRequest_.getMethod()), null,
                    new MockFilterChainImpl());
        }
    }

    final public Class<?> getPageClass(String path) {
        Class<?> pageClass = ymir_.getPageClassOfPath(path);
        if (pageClass == null) {
            throw new IllegalArgumentException(
                    "Can't find page class corresponding path (" + path + ")");
        }
        return pageClass;
    }

    final public String getPathOfPageClass(Class<?> pageClass) {
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
            MockServletContext servletContext, HttpMethod method, String path,
            MockHttpSession session) {
        return new MockHttpServletRequestImpl(servletContext, method.name(),
                path, session);
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
    public Notes getNotes() {
        return (Notes) httpRequest_.getAttribute(RequestProcessor.ATTR_NOTES);
    }

    public Request getRequest() {
        return (Request) httpRequest_.getAttribute(ATTR_REQUEST);
    }

    public Response getResponse() {
        return (Response) httpRequest_.getAttribute(ATTR_RESPONSE);
    }

    protected <P> P getPage(Class<? extends P> pageClass) {
        return getComponent(pageClass);
    }
}
