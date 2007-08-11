package org.seasar.ymir;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public interface Request extends Dispatch, AttributeContainer {
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
     * HTTPメソッドを返します。
     * <p>返されるメソッド名は全て大文字です。</p>
     *
     * @return メソッド名。
     */
    String getMethod();

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
     * リクエストパスに対応するPageコンポーネントを返します。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     * 
     * @return Pageコンポーネント。
     */
    PageComponent getPageComponent();

    /**
     * リクエストパスに対応するPageコンポーネントを設定します。
     * <p>このメソッドはリクエストパスに対応するPageコンポーネントを生成した段階でPageコンポーネントを
     * Requestオブジェクトにセットするために用いられます。
     * アプリケーションはこのメソッドを呼び出さないようにして下さい。
     * </p>
     * 
     * @param componentClass Pageコンポーネント。
     */
    void setPageComponent(PageComponent pageComponent);

    /**
     * リクエストを処理するアクションを返します。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return アクション。
     */
    Action getAction();

    /**
     * リクエストパスとHTTPメソッドに対応するアクションの名前を返します。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return アクション名。
     */
    String getActionName();

    /**
     * アクションを設定します。
     * <p>このメソッドは実際に呼び出されるアクションが決定した段階でアクションを
     * Requestオブジェクトにセットするために用いられます。
     * アプリケーションはこのメソッドを呼び出さないようにして下さい。
     * </p>
     * 
     * @param action アクション。
     */
    void setAction(Action action);

    /**
     * 現在のリクエストがどのロケールに基づいて処理されているかを返します。
     *
     * @return ロケール。nullを返すことはありません。
     */
    Locale getLocale();

    /**
     * 現在のdispatchの処理を開始します。
     * <p>このメソッドはフレームワークが現在のdispatchを表すDispatchオブジェクトを
     * Requestオブジェクトにセットするために用いられます。
     * アプリケーションはこのメソッドを呼び出さないようにして下さい。
     * </p>
     * 
     * @param dispatch Dispatchオブジェクト。
     */
    void enterDispatch(Dispatch dispatch);

    /**
     * 現在処理中のdispatchを表すDispatchオブジェクトを返します。
     * <p>1つのリクエストは1つまたは複数のdispatchによって構成されています。
     * このメソッドは現在処理中のdispatchを表すDispatchオブジェクトを返します。
     * </p>
     *
     * @return Dispatchオブジェクト。
     */
    Dispatch getCurrentDispatch();

    /**
     * 現在のdispatchの処理を終了します。
     * <p>このメソッドはフレームワークによって用いられます。
     * アプリケーションはこのメソッドを呼び出さないようにして下さい。
     * </p>
     */
    void leaveDispatch();
}
