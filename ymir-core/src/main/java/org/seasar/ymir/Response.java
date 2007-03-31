package org.seasar.ymir;

import java.io.InputStream;

public interface Response {
    int STATUS_UNDEFINED = -1;

    ResponseType getType();

    void setType(ResponseType type);

    String getPath();

    void setPath(String path);

    InputStream getInputStream();

    void setInputStream(InputStream is);

    String getContentType();

    void setContentType(String contentType);

    String getCharacterEncoding();

    int getStatus();

    void setStatus(int status);
}
