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

    /**
     * コンテキストパスを返します。
     *
     * @return コンテキストパス。
     */
    String getContextPath();

    /**
     * リクエストパスを返します。
     * <p>返されるパスはコンテキストパス相対です。</p>
     *
     * @return リクエストパス。
     */
    String getPath();

    /**
     * リクエストパスの絶対パスを返します。
     * <p>返されるパスはコンテキストパスとパスを連結したものです。</p>
     *
     * @return リクエストパスの絶対パス。
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
     * 現在のリクエストがどのロケールに基づいて処理されているかを返します。
     *
     * @return ロケール。nullを返すことはありません。
     */
    Locale getLocale();

    /**
     * 現在のディスパッチの処理を開始します。
     * <p>このメソッドはフレームワークが現在のディスパッチを表すDispatchオブジェクトを
     * Requestオブジェクトにセットするために用いられます。
     * アプリケーションはこのメソッドを呼び出さないようにして下さい。
     * </p>
     * 
     * @param dispatch Dispatchオブジェクト。
     */
    void enterDispatch(Dispatch dispatch);

    /**
     * requestディスパッチを表すDispatchオブジェクトを返します。
     * <p>元々のリクエストパスなどを取得したい場合は、
     * このメソッドが返すDispatchオブジェクトから取得することができます。
     * </p>
     * 
     * @return requestディスパッチを表すDispatchオブジェクト。
     */
    Dispatch getRequestDispatch();

    /**
     * 現在処理中のディスパッチを表すDispatchオブジェクトを返します。
     * <p>1つのリクエストは1つまたは複数のディスパッチによって構成されています。
     * このメソッドは現在処理中のディスパッチを表すDispatchオブジェクトを返します。
     * </p>
     *
     * @return 現在処理中のディスパッチを表すDispatchオブジェクト。
     */
    Dispatch getCurrentDispatch();

    /**
     * 現在のディスパッチの処理を終了します。
     * <p>このメソッドはフレームワークによって用いられます。
     * アプリケーションはこのメソッドを呼び出さないようにして下さい。
     * </p>
     */
    void leaveDispatch();

    /**
     * 現在のディスパッチにおける、パスとHTTPメソッドに対応するアクションの名前を返します。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return アクション名。
     */
    String getActionName();
}
