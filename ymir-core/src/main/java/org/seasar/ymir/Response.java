package org.seasar.ymir;

import java.io.InputStream;

public interface Response {
    int STATUS_UNDEFINED = -1;

    ResponseType getType();

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
