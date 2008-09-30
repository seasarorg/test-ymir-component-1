package org.seasar.ymir;

/**
 * Applicationオブジェクトを管理するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @see Application
 */
public interface ApplicationManager {
    /**
     * 指定されたApplicationを管理対象として追加します。
     * 
     * @param application Application。nullを指定してはいけません。
     */
    void addApplication(Application application);

    /**
     * 登録されているApplicationを返します。
     * 
     * @return Applicationの配列。登録されているApplicationが存在しない場合は空の配列を返します。
     */
    public Application[] getApplications();

    /**
     * 指定されたApplicationを現在のコンテキストに関連付けます。
     * <p>フレームワークはこのメソッドを使って、
     * 現在のコンテキストに対応するApplicationオブジェクトを設定します。
     * </p>
     * <p>関連付けられたApplicationは、{@link #getContextApplication()}または
     * {@link #findContextApplication()}で取得することができます。
     * </p>
     * <p>nullを指定することで、Applicationの関連付けを解除することができます。
     * </p>
     * 
     * @param application Application。nullを指定することもできます。
     * @see #getContextApplication()
     * @see #findContextApplication()
     */
    void setContextApplication(Application application);

    /**
     * 現在のコンテキストに関連付けられたApplicationを返します。
     * <p>関連付けられていない場合はnullを返します。
     * </p>
     * <p>アプリケーションコードの中からApplicationオブジェクトを取り出す場合は、
     * 通常は{@link #findContextApplication()}を使用して下さい。
     * </p>
     * 
     * @return 現在のコンテキストに関連付けられたApplication。
     * @see #findContextApplication()
     */
    Application getContextApplication();

    /**
     * 現在のコンテキストに関連付けられたApplicationを返します。
     * <p>関連付けられていない場合はベースとなるApplicationを返します。
     * </p>
     * 
     * @return 現在のコンテキストに関連付けられたApplication。
     * 関連付けられていなければベースとなるApplication。
     * @see #getContextApplication()
     */
    Application findContextApplication();

    /**
     * ベースとなるApplicationを設定します。
     * <p>現在のコンテキストにApplicationが関連付けられていない場合に利用するApplicationを設定します。
     * 
     * @param application Application。
     */
    void setBaseApplication(Application application);
}
