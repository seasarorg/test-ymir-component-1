package org.seasar.ymir.token;

import org.seasar.ymir.Globals;

/**
 * トランザクショントークンを管理するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface TokenManager {
    /**
     * トークンに関するアプリケーションプロパティのキーの接頭辞です。
     */
    String APPKEYPREFIX_CORE_TOKEN = Globals.APPKEYPREFIX_CORE + "token.";

    /**
     * トークンのキーを指定するためのプロパティのキーです。
     */
    String APPKEY_CORE_TOKEN_KEY = APPKEYPREFIX_CORE_TOKEN + "key";

    /**
     * トークンのキーのデフォルト値です。
     */
    String DEFAULT_CORE_TOKEN_KEY = Globals.IDPREFIX + "token";

    /**
     * 新しいTokenオブジェクトを生成します。
     * <p>生成した時点ではトークンはセッションに保持されません。
     * </p>
     * 
     * @return 生成したTokenオブジェクト。
     */
    Token newToken();

    /**
     * トークンのキーを返します。
     * <p>トークンのキーは、トークンを持つリクエストパラメータの名前やトークンをセッション中に保持する際の属性名として使われます。
     * これは{@link Token#getName()}の値と同じです。
     * </p>
     * 
     * @return トークンのキー。
     * @see Token#getName()
     */
    String getTokenKey();

    /**
     * 新たなトークンを生成します。
     * 
     * @return 生成したトークン。
     */
    String generateToken();

    /**
     * 指定されたキーに対応するトークンをセッションから取り出して返します。
     * <p>キーに対応するトークンが存在しない場合はnullを返します。
     * </p>
     * 
     * @param tokenKey キー。
     * @return トークン。
     */
    String getToken(String tokenKey);

    /**
     * 指定されたキーについて、
     * リクエストパラメータとして送信されてきたトークンとセッション中のトークンが一致するかどうかを返します。
     * <p>このメソッドは<code>isTokenValid(tokenKey, true)</code>と同じです。
     * </p>
     * 
     * @param tokenKey キー。
     * @return リクエストパラメータとして送信されてきたトークンとセッション中のトークンが一致するかどうか。
     */
    boolean isTokenValid(String tokenKey);

    /**
     * 指定されたキーについて、
     * リクエストパラメータとして送信されてきたトークンとセッション中のトークンが一致するかどうかを返します。
     * <p>resetがtrueの場合は、一致の確認後にセッションからトークンを取り除きます。
     * </p>
     * 
     * @param tokenKey キー。
     * @param reset 一致の確認後にセッションからトークンを取り除くかどうか。
     * @return リクエストパラメータとして送信されてきたトークンとセッション中のトークンが一致するかどうか。
     */
    boolean isTokenValid(String tokenKey, boolean reset);

    /**
     * 指定されたキーに対応するトークンをセッションから取り除きます。
     * 
     * @param tokenKey キー。
     */
    void resetToken(String tokenKey);

    /**
     * トークンを生成して、指定されたキーでセッションに保存します。
     * <p>このメソッドは<code>saveToken(tokenKey, true)</code>と同じです。
     * </p>
     * 
     * @param tokenKey キー。
     */
    void saveToken(String tokenKey);

    /**
     * トークンを生成して、指定されたキーでセッションに保存します。
     * <p>forceがtrueの場合は、既にセッションに同じキーでトークンが存在する場合に強制的に置き換えます。
     * falseの場合は、既にセッションに同じキーでトークンが存在する場合に何もしません。
     * </p>
     * 
     * @param tokenKey キー。
     * @param force 既にセッションに同じキーでトークンが存在する場合に強制的に置き換えるかどうか。
     */
    void saveToken(String tokenKey, boolean force);
}
