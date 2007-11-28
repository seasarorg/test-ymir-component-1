package org.seasar.ymir;

import java.io.InputStream;

public interface Response {
    int STATUS_UNDEFINED = -1;

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

    String getPath();

    void setPath(String path);

    InputStream getInputStream();

    void setInputStream(InputStream is);

    String getContentType();

    void setContentType(String contentType);

    String getCharacterEncoding();

    int getStatus();

    void setStatus(int status);

    void setHeader(String name, String value);

    void setDateHeader(String name, long value);

    void setIntHeader(String name, int value);

    void addHeader(String name, String value);

    void addDateHeader(String name, long value);

    void addIntHeader(String name, int value);

    boolean containsHeader(String name);

    ResponseHeader[] getResponseHeaders();
}
