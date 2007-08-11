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
     * @return
     */
    String getAbsolutePath();

    /**
     * ディスパッチャ名を返します。
     * <p>返されるディスパッチャ名は{@link Dispatch#DISPATCHER_REQUEST}、
     * {@link Dispatch#DISPATCHER_FORWARD}、
     * {@link Dispatch#DISPATCHER_INCLUDE}、
     * {@link Dispatch#DISPATCHER_ERROR}のいずれかです。
     * </p>
     *
     * @return ディスパッチャ名。
     */
    String getDispatcher();

    /**
     * リクエストパスに対応するPageコンポーネントの名前を返します。
     * <p>パスに対応するPageコンポーネントが存在しない場合はnullを返します。</p>
     *
     * @return Pageコンポーネント名。
     */
    String getPageComponentName();

    /**
     * リクエストパスに連結されているpathInfo情報を返します。
     *
     * @return pathInfo情報。PathMappingルールによってはnullが返されることもあります。
     */
    String getPathInfo();

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
