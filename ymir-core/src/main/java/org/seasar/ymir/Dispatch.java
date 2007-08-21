package org.seasar.ymir;

public interface Dispatch {
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
     * @return 絶対パス。
     */
    String getAbsolutePath();

    /**
     * ディスパッチャ名を返します。
     *
     * @return ディスパッチャ名。
     */
    Dispatcher getDispatcher();

    /**
     * パスに対応するPageコンポーネントの名前を返します。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return Pageコンポーネント名。
     */
    String getPageComponentName();

    /**
     * パスに対応するPageコンポーネントを返します。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     * 
     * @return Pageコンポーネント。
     */
    PageComponent getPageComponent();

    /**
     * パスに対応するPageコンポーネントを設定します。
     * <p>このメソッドはパスに対応するPageコンポーネントを生成した段階でPageコンポーネントを
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
     * パスとHTTPメソッドに対応するアクションの名前を返します。
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
     * パスに連結されているpathInfo情報を返します。
     *
     * @return pathInfo情報。PathMappingルールによってはnullが返されることもあります。
     */
    String getPathInfo();

    /**
     * パスがパスマッピングにマッチしたかどうかを返します。
     *
     * @return パスがパスマッピングにマッチしたかどうか。
     */
    boolean isMatched();

    /**
     * パスへのリクエストを拒否すべきかどうかを返します。
     *
     * @return パスへのリクエストを拒否すべきかどうか。
     */
    boolean isDenied();

    MatchedPathMapping getMatchedPathMapping();
}
