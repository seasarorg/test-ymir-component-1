package org.seasar.ymir.testing;

import static org.seasar.ymir.Ymir.ATTR_RESPONSE;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
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
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.Path;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.Response;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
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
    private static final String DEFAULT_WEBAPPROOT = "src/main/webapp";

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

    /**
     * テストメソッドを呼び出す前の処理を行ないます。
     * <p>このメソッドをオーバライドする場合は必ずsuperメソッドを呼び出して下さい。
     * </p>
     */
    public void setUp() {
        servletContext_ = new MockServletContextImpl(getContextPath());
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
     * <p>このメソッドは<code>prepareForProcessing</code>メソッドの呼び出し後に呼び出して下さい。
     * </p>
     * 
     * @return 現在のHttpServletRequestオブジェクト。
     */
    public MockHttpServletRequest getHttpServletRequest() {
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
    public MockHttpSession getHttpSession() {
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
    public MockHttpSession getHttpSession(boolean create) {
        return (MockHttpSession) getHttpServletRequest().getSession(create);
    }

    /**
     * 現在のHttpServletResponseオブジェクトを返します。
     * <p>このメソッドは<code>prepareForProcessing</code>メソッドの呼び出し後に呼び出して下さい。
     * </p>
     * 
     * @return 現在のHttpServletResponseオブジェクト。
     */
    public MockHttpServletResponse getHttpServletResponse() {
        return httpResponse_;
    }

    public void process(ServletContext servletContext,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Dispatcher dispatcher, String path, HttpMethod method,
            Map<String, FormFile[]> fileParameterMap, FilterChain chain)
            throws IOException, ServletException {
        ymir_.process(servletContext, httpRequest, httpResponse, dispatcher,
                path, method, fileParameterMap, chain);
    }

    public FilterChain process(String path, Object... params)
            throws IOException, ServletException {
        return process(path, HttpMethod.GET, params);
    }

    public FilterChain process(String path, HttpMethod method, Object... params)
            throws IOException, ServletException {
        Initializer initializer = null;
        MockFilterChain chain = new MockFilterChainImpl();
        if (params != null) {
            boolean paramStarted = false;
            List<Object> paramList = new ArrayList<Object>();
            for (Object param : params) {
                if (param instanceof Initializer) {
                    if (initializer != null) {
                        throw new IllegalClientCodeRuntimeException(
                                "Multiple Initializer specified");
                    } else if (paramStarted) {
                        throw new IllegalClientCodeRuntimeException(
                                "Initializer must be specified before parameters");
                    }
                    initializer = (Initializer) param;
                } else if (param instanceof MockFilterChain) {
                    if (chain != null) {
                        throw new IllegalClientCodeRuntimeException(
                                "Multiple MockFilterChain specified");
                    } else if (paramStarted) {
                        throw new IllegalClientCodeRuntimeException(
                                "MockFilterChain must be specified before parameters");
                    }
                    chain = (MockFilterChain) param;
                } else if (param instanceof FilterChain) {
                    throw new IllegalClientCodeRuntimeException(
                            "Can't specify FilterChain. Specify MockFilterChain instead");
                } else {
                    paramStarted = true;
                    paramList.add(param);
                }
            }
            params = paramList.toArray();
        }
        if (chain == null) {
            chain = new MockFilterChainImpl();
        }

        Map<String, String[]> parameterMap = new LinkedHashMap<String, String[]>();
        Map<String, FormFile[]> fileParameterMap = new LinkedHashMap<String, FormFile[]>();
        if (params != null) {
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

    public FilterChain process(Class<?> pageClass, Object... params)
            throws IOException, ServletException {
        return process(pageClass, HttpMethod.GET, params);
    }

    public FilterChain process(Class<?> pageClass, HttpMethod method,
            Object... params) throws IOException, ServletException {
        return process(getPathOfPageClass(pageClass), method, params);
    }

    public void acceptRequest(String path, HttpMethod method,
            Initializer initializer) {
        MockHttpSession session = null;
        if (httpRequest_ != null) {
            session = (MockHttpSession) httpRequest_.getSession(false);
        }
        httpRequest_ = newHttpServletRequest(servletContext_, path, session);
        try {
            httpRequest_.setCharacterEncoding(getCharacterEncoding());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        httpRequest_.setMethod(method.name());
        httpRequest_.setLocale(getLocale());
        httpRequest_
                .setRequestDispatcherFactory(new RequestDispatcherFactory() {
                    public RequestDispatcher newInstance(String path) {
                        return new MockRequestDispatcherImpl(path) {
                            @Override
                            public void forward(ServletRequest request,
                                    ServletResponse response)
                                    throws ServletException, IOException {
                                super.forward(request, response);

                                process(servletContext_, httpRequest_,
                                        httpResponse_, Dispatcher.FORWARD,
                                        getPath(), HttpMethod
                                                .enumOf(httpRequest_
                                                        .getMethod()), null,
                                        new FilterChain() {
                                            public void doFilter(
                                                    ServletRequest request,
                                                    ServletResponse response)
                                                    throws IOException,
                                                    ServletException {
                                            }
                                        });
                            }

                            @Override
                            public void include(ServletRequest request,
                                    ServletResponse response)
                                    throws ServletException, IOException {
                                super.include(request, response);

                                process(servletContext_, httpRequest_,
                                        httpResponse_, Dispatcher.INCLUDE,
                                        getPath(), HttpMethod
                                                .enumOf(httpRequest_
                                                        .getMethod()), null,
                                        new FilterChain() {
                                            public void doFilter(
                                                    ServletRequest request,
                                                    ServletResponse response)
                                                    throws IOException,
                                                    ServletException {
                                            }
                                        });
                            }
                        };
                    }
                });
        httpResponse_ = newHttpServletResponse(httpRequest_);

        ContainerUtils.setRequest(container_, httpRequest_);
        ContainerUtils.setResponse(container_, httpResponse_);

        if (initializer != null) {
            initializer.initialize();
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
            MockServletContext servletContext, String path,
            MockHttpSession session) {
        return new MockHttpServletRequestImpl(servletContext, path, session);
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

    public Response getResponse() {
        return (Response) httpRequest_.getAttribute(ATTR_RESPONSE);
    }
}
