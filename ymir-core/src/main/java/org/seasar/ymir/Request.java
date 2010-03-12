package org.seasar.ymir;

import java.util.Iterator;
import java.util.Map;

import org.seasar.ymir.interceptor.YmirProcessInterceptor;
import org.seasar.ymir.util.ServletUtils;

/**
 * HTTPリクエストを抽象化したインタフェースです。
 * <p>HttpServletRequestとほぼ対応しますが、1回のリクエスト中に行なわれたインクルードやフォワードなどの
 * ディスパッチを抽象化して扱えるように工夫されています。
 * ディスパッチに関する情報は{@link Dispatch}インタフェースで表され、
 * {@link #getCurrentDispatch()}などのメソッドで取得することができます。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @see Dispatch
 */
public interface Request extends AttributeContainer {
    /**
     * コンテキストパスを返します。
     * <p>返されるパスは末尾に「/」がつかない形に正規化されています。
     * </p>
     *
     * @return コンテキストパス。
     */
    String getContextPath();

    /**
     * リクエストパスを返します。
     * <p>返されるパスはコンテキストパス相対です。</p>
     * <p>リクエストされた際のオリジナルのパスの末尾に「/」がついている場合は「/」がついたまま返されます。
     * 「/」がつかない形にしたい場合は{@link ServletUtils#normalizePath(String)}を使って下さい。
     * </p>
     *
     * @return リクエストパス。
     */
    String getPath();

    /**
     * リクエストパスの絶対パスを返します。
     * <p>返されるパスはコンテキストパスとパスを連結したものです。</p>
     * <p>リクエストされた際のオリジナルのパスの末尾に「/」がついている場合は「/」がついたまま返されます。
     * 「/」がつかない形にしたい場合は{@link ServletUtils#normalizePath(String)}を使って下さい。
     * </p>
     *
     * @return リクエストパスの絶対パス。
     */
    String getAbsolutePath();

    /**
     * HTTPメソッドを返します。
     *
     * @return HTTPメソッド。
     */
    HttpMethod getMethod();

    /**
     * リクエストの文字エンコーディングを返します。
     * @return リクエストの文字エンコーディング。
     */
    String getCharacterEncoding();

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
     * 指定された名前の拡張リクエストパラメータの値を返します。
     * <p>指定された名前の拡張リクエストパラメータが存在しない場合はnullを返します。
     * </p>
     *
     * @param name 拡張リクエストパラメータ名。
     * @return 値。
     * @since 1.0.0
     */
    Object getExtendedParameter(String name);

    /**
     * 指定された名前の拡張リクエストパラメータの値を返します。
     * <p>指定された名前の拡張リクエストパラメータが存在しない場合は
     * <code>defaultValue</code>を返します。
     * </p>
     *
     * @param name 拡張リクエストパラメータ名。
     * @param defaultValue デフォルトの値。
     * @return 値。
     * @since 1.0.0
     */
    Object getExtendedParameter(String name, Object defaultValue);

    /**
     * 全ての拡張リクエストパラメータの名前のIteratorを返します。
     *
     * @return Iteratorオブジェクト。
     */
    Iterator<String> getExtendedParameterNames();

    /**
     * 全ての拡張リクエストパラメータを格納しているMapを返します。
     *
     * @return Mapオブジェクト。
     */
    Map<String, Object> getExtendedParameterMap();

    /**
     * リクエストスコープのオブジェクトを保持するためのAttributeContainerを返します。
     *
     * @return AttributeContainerオブジェクト。
     */
    AttributeContainer getAttributeContainer();

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
     * 現在処理中のディスパッチがインクルード由来のものかどうかを返します。
     * <p>例えばrequest→forward→include→forwardのようにディスパッチされている場合は
     * trueを返します。
     * </p>
     * 
     * @return インクルード由来のディスパッチを処理中かどうか。
     * @since 1.0.7
     */
    boolean isIncluded();

    /**
     * 現在のディスパッチの処理を終了します。
     * <p>このメソッドはフレームワークによって用いられます。
     * アプリケーションはこのメソッドを呼び出さないようにして下さい。
     * </p>
     */
    void leaveDispatch();

    /**
     * 現在のディスパッチにおける、元々のアクションの名前を返します。
     * <p>このメソッドが返すアクションの名前は、
     * リクエストされたパスに対応するアクションの名前です。
     * 実際に実行されるアクションは{@link YmirProcessInterceptor}等によって
     * 変更されることがあるため、このメソッドが返すアクションの名前が
     * 実際に実行されるアクションの名前と一致するとは限りません。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     * 
     * @return 元々のアクションの名前。
     * @see #getActionName()
     * @since 1.0.7
     */
    String getOriginalActionName();

    /**
     * 現在のディスパッチにおける、最終的なアクションの名前返します。
     * <p>このメソッドが返すアクションの名前は、
     * {@link YmirProcessInterceptor}等によって
     * 変更された後の最終的なアクションの名前です。
     * </p>
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return 最終的なアクションの名前。
     * @see #getOriginalActionName()
     */
    String getActionName();
}
