package org.seasar.ymir;

import java.io.InputStream;

/**
 * HTTPレスポンスを抽象化したインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface Response {
    /**
     * ステータスが未定義であることを表します。
     */
    int STATUS_UNDEFINED = -1;

    /**
     * レスポンスのタイプを返します。
     * 
     * @return レスポンスのタイプを表す{@link ResponseType}オブジェクト。
     */
    ResponseType getType();

    /**
     * レスポンスが現在のリクエストに従属しているかどうかを返します。
     * <p>「レスポンスが現在のリクエストに従属している」とは、
     * 例えば現在のリクエストパラメータがレスポンスの表すディスパッチ先のリクエストに追加されるといったように、
     * レスポンスの表すディスパッチ先が現在のリクエストから独立していないことを表します。
     * </p>
     * <p>例えばServletAPIではフォワードレスポンスは従属で、リダイレクトレスポンスは独立です。</p>
     * 
     * @return 従属しているかどうか。
     */
    boolean isSubordinate();

    /**
     * レスポンスを表すパスを返します。
     * <p>このメソッドはレスポンスのタイプが{@link ResponseType#FORWARD}
     * または{@link ResponseType#REDIRECT}の場合だけ利用可能です。
     * </p>
     * 
     * @return レスポンスが表すパス。
     */
    String getPath();

    /**
     * レスポンスを表すパスを設定します。
     * <p>このメソッドはレスポンスのタイプが{@link ResponseType#FORWARD}
     * または{@link ResponseType#REDIRECT}の場合だけ利用可能です。
     * </p>
     * 
     * @param path レスポンスが表すパス。
     */
    void setPath(String path);

    /**
     * レスポンスボディを表すInputStreamを返します。
     * <p>このメソッドはレスポンスのタイプが{@link ResponseType#SELF_CONTAINED}
     * の場合だけ利用可能です。
     * </p>
     * 
     * @return レスポンスボディを表すInputStream。
     */
    InputStream getInputStream();

    /**
     * レスポンスボディを表すInputStreamを設定します。
     * <p>このメソッドはレスポンスのタイプが{@link ResponseType#SELF_CONTAINED}
     * の場合だけ利用可能です。
     * </p>
     * 
     * @param is レスポンスボディを表すInputStream。
     */
    void setInputStream(InputStream is);

    /**
     * レスポンスボディのコンテントタイプを返します。
     * <p>このメソッドはレスポンスのタイプが{@link ResponseType#SELF_CONTAINED}
     * の場合だけ利用可能です。
     * </p>
     * 
     * @return レスポンスボディのコンテントタイプ。
     */
    String getContentType();

    /**
     * レスポンスボディのコンテントタイプを設定します。
     * <p>このメソッドはレスポンスのタイプが{@link ResponseType#SELF_CONTAINED}
     * の場合だけ利用可能です。
     * </p>
     * 
     * @param contentType レスポンスボディのコンテントタイプ。
     */
    void setContentType(String contentType);

    /**
     * レスポンスボディの文字エンコーディングを返します。
     * <p>文字エンコーディングが指定されていない場合はnullを返します。
     * </p>
     * <p>このメソッドはレスポンスのタイプが{@link ResponseType#SELF_CONTAINED}
     * の場合だけ利用可能です。
     * </p>
     * 
     * @return レスポンスボディの文字エンコーディング。
     */
    String getCharacterEncoding();

    /**
     * レスポンスのHTTPステータスコードを返します。
     * 
     * @return HTTPステータスコード。
     */
    int getStatus();

    /**
     * レスポンスのHTTPステータスコードを設定します。
     * 
     * @param status HTTPステータスコード。
     */
    void setStatus(int status);

    /**
     * レスポンスヘッダを設定します。
     * <p>同じ名前のヘッダが既に設定されている場合は置き換えます。
     * </p>
     * 
     * @param name ヘッダ名。
     * @param value 値。
     */
    void setHeader(String name, String value);

    /**
     * 日付型のレスポンスヘッダを設定します。
     * <p>同じ名前のヘッダが既に設定されている場合は置き換えます。
     * </p>
     * 
     * @param name ヘッダ名。
     * @param value 値。
     */
    void setDateHeader(String name, long value);

    /**
     * 数値型のレスポンスヘッダを設定します。
     * <p>同じ名前のヘッダが既に設定されている場合は置き換えます。
     * </p>
     * 
     * @param name ヘッダ名。
     * @param value 値。
     */
    void setIntHeader(String name, int value);

    /**
     * レスポンスヘッダを追加設定します。
     * <p>同じ名前のヘッダが既に設定されている場合は追加されます。
     * </p>
     * 
     * @param name ヘッダ名。
     * @param value 値。
     */
    void addHeader(String name, String value);

    /**
     * 日付型のレスポンスヘッダを追加設定します。
     * <p>同じ名前のヘッダが既に設定されている場合は追加されます。
     * </p>
     * 
     * @param name ヘッダ名。
     * @param value 値。
     */
    void addDateHeader(String name, long value);

    /**
     * 数値型のレスポンスヘッダを追加設定します。
     * <p>同じ名前のヘッダが既に設定されている場合は追加されます。
     * </p>
     * 
     * @param name ヘッダ名。
     * @param value 値。
     */
    void addIntHeader(String name, int value);

    /**
     * 指定された名前のヘッダが設定済みかどうかを返します。
     * 
     * @param name ヘッダ名。
     * @return 指定された名前のヘッダが設定済みかどうか。
     */
    boolean containsHeader(String name);

    /**
     * 設定された全てのレスポンスヘッダを返します。
     * 
     * @return 設定された全てのレスポンスヘッダ。
     */
    ResponseHeader[] getResponseHeaders();
}
