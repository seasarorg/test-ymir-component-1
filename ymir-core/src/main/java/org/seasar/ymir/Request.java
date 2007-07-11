package org.seasar.ymir;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public interface Request extends AttributeContainer {

    String METHOD_CONNECT = "CONNECT";

    String METHOD_DELETE = "DELETE";

    String METHOD_GET = "GET";

    String METHOD_HEAD = "HEAD";

    String METHOD_LINK = "LINK";

    String METHOD_OPTIONS = "OPTIONS";

    String METHOD_PATCH = "PATCH";

    String METHOD_POST = "POST";

    String METHOD_PUT = "PUT";

    String METHOD_TRACE = "TRACE";

    String METHOD_UNLINK = "UNLINK";

    String DISPATCHER_REQUEST = "REQUEST";

    String DISPATCHER_FORWARD = "FORWARD";

    String DISPATCHER_INCLUDE = "INCLUDE";

    String DISPATCHER_ERROR = "ERROR";

    /**
     * コンテキストパスを返します。
     *
     * @return コンテキストパス。
     */
    String getContextPath();

    /**
     * パスを返します。
     * <p>返されるパスはコンテキストパス相対です。</p>
     *
     * @return パス。
     */
    String getPath();

    /**
     * 絶対パスを返します。
     * <p>返されるパスはコンテキストパスとパスを連結したものです。</p>
     *
     * @return
     */
    String getAbsolutePath();

    /**
     * HTTPメソッドを返します。
     * <p>返されるメソッド名は全て大文字です。</p>
     *
     * @return メソッド名。
     */
    String getMethod();

    /**
     * ディスパッチャ名を返します。
     * <p>返されるディスパッチャ名は{@link Request#DISPATCHER_REQUEST}、
     * {@link Request#DISPATCHER_FORWARD}、
     * {@link Request#DISPATCHER_INCLUDE}、
     * {@link Request#DISPATCHER_ERROR}のいずれかです。
     * </p>
     *
     * @return ディスパッチャ名。
     */
    String getDispatcher();

    /**
     * 指定された名前のリクエストパラメータの値を返します。
     * <p>指定された名前のリクエストパラメータが存在しない場合はnullを返します。
     * </p>
     * <p>ファイルパラメータは対象外です。</p>
     *
     * @param name リクエストパラメータ名。
     * @return 値。
     */
    String getParameter(String name);

    /**
     * 指定された名前のリクエストパラメータの値を返します。
     * <p>指定された名前のリクエストパラメータが存在しない場合は
     * <code>defaultValue</code>を返します。
     * </p>
     * <p>ファイルパラメータは対象外です。</p>
     *
     * @param name リクエストパラメータ名。
     * @param defaultValue デフォルトの値。
     * @return 値。
     */
    String getParameter(String name, String defaultValue);

    /**
     * 指定された名前のリクエストパラメータの値を返します。
     * <p>指定された名前のリクエストパラメータが存在しない場合はnullを返します。
     * </p>
     * <p>ファイルパラメータは対象外です。</p>
     *
     * @param name リクエストパラメータ名。
     * @return 値。
     */
    String[] getParameterValues(String name);

    /**
     * 指定された名前のリクエストパラメータの値を返します。
     * <p>指定された名前のリクエストパラメータが存在しない場合は
     * <code>defaultValues</code>を返します。
     * </p>
     * <p>ファイルパラメータは対象外です。</p>
     *
     * @param name リクエストパラメータ名。
     * @param defaultValues デフォルトの値。
     * @return 値。
     */
    String[] getParameterValues(String name, String[] defaultValues);

    /**
     * 全てのリクエストパラメータの名前のIteratorを返します。
     * <p>ファイルパラメータは対象外です。</p>
     *
     * @return Iteratorオブジェクト。
     */
    Iterator<String> getParameterNames();

    /**
     * 全てのリクエストパラメータを格納しているMapを返します。
     * <p>ファイルパラメータは対象外です。</p>
     *
     * @return Mapオブジェクト。
     */
    Map<String, String[]> getParameterMap();

    /**
     * &lt;input type="file" /&gt;タグで指定されたファイルパラメータのうち、
     * 指定された名前を持つものを返します。
     * <p>指定された名前のファイルパラメータが存在しない場合はnullを返します。
     * </p>
     *
     * @param name ファイルパラメータ名。
     * @return 値。
     */
    FormFile getFileParameter(String name);

    /**
     * &lt;input type="file" /&gt;タグで指定されたファイルパラメータのうち、
     * 指定された名前を持つものを返します。
     * <p>指定された名前のファイルパラメータが存在しない場合はnullを返します。
     * </p>
     *
     * @param name ファイルパラメータ名。
     * @return 値。
     */
    FormFile[] getFileParameterValues(String name);

    /**
     * 全てのファイルパラメータの名前のIteratorを返します。
     *
     * @return Iteratorオブジェクト。
     */
    Iterator<String> getFileParameterNames();

    /**
     * 全てのファイルパラメータを格納しているMapを返します。
     *
     * @return Mapオブジェクト。
     */
    Map<String, FormFile[]> getFileParameterMap();

    /**
     * リクエストスコープのオブジェクトを保持するためのAttributeContainerを返します。
     *
     * @return AttributeContainerオブジェクト。
     */
    AttributeContainer getAttributeContainer();

    /**
     * リクエストパスに対応するコンポーネントの名前を返します。
     * <p>パスに対応するコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return コンポーネント名。
     */
    String getComponentName();

    /**
     * リクエストパスに対応するコンポーネントのクラスを返します。
     * <p>パスに対応するコンポーネントが存在しない場合はnullを返します。</p>
     * 
     * @return コンポーネントのクラス。
     */
    Class<?> getComponentClass();

    /**
     * リクエストパスに対応するコンポーネントのクラスを設定します。
     * <p>このメソッドはリクエストパスに対応するコンポーネントを生成した段階でコンポーネントクラスを
     * Requestオブジェクトにセットするために用いられます。
     * アプリケーションはこのメソッドを呼び出さないようにして下さい。
     * </p>
     * 
     * @param componentClass コンポーネントのクラス。
     */
    void setComponentClass(Class<?> componentClass);

    /**
     * リクエストパスとHTTPメソッドに対応するアクションの名前を返します。
     * <p>パスに対応するコンポーネントが存在しない場合や、
     * リクエストパスとHTTPメソッドに対応するアクションが存在しない場合はnullを返します。</p>
     *
     * @return アクション名。
     */
    String getActionName();

    /**
     * アクション名を設定します。
     * <p>このメソッドは実際に呼び出されるアクション名が決定した段階でアクション名を
     * Requestオブジェクトにセットするために用いられます。
     * アプリケーションはこのメソッドを呼び出さないようにして下さい。
     * </p>
     * 
     * @param actionName アクション名。
     */
    void setActionName(String actionName);

    /**
     * リクエストパスに連結されているpathInfo情報を返します。
     *
     * @return pathInfo情報。PathMappingルールによってはnullが返されることもあります。
     */
    String getPathInfo();

    /**
     * 現在のリクエストがどのロケールに基づいて処理されているかを返します。
     *
     * @return ロケール。nullを返すことはありません。
     */
    Locale getLocale();

    /**
     * リクエストパスがパスマッピングにマッチしたかどうかを返します。
     *
     * @return リクエストパスがパスマッピングにマッチしたかどうか。
     */
    boolean isMatched();

    /**
     * リクエストパスへのリクエストを拒否すべきかどうかを返します。
     *
     * @return リクエストパスへのリクエストを拒否すべきかどうか。
     */
    boolean isDenied();

    MatchedPathMapping getMatchedPathMapping();
}
