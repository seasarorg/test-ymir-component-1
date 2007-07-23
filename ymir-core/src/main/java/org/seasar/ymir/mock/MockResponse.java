package org.seasar.ymir.mock;

import java.io.InputStream;

import org.seasar.ymir.ResponseType;
import org.seasar.ymir.response.ResponseBase;

public class MockResponse extends ResponseBase {
    private String contentType_;

    private InputStream inputStream_;

    private String path_;

    private ResponseType type_;

    private String characterEncoding_;

    public String getCharacterEncoding() {
        return characterEncoding_;
    }

    public String getContentType() {
        return contentType_;
    }

    public InputStream getInputStream() {
        return inputStream_;
    }

    public String getPath() {
        return path_;
    }

    public ResponseType getType() {
        return type_;
    }

    public void setCharacterEncoding(String characterEncoding) {
        characterEncoding_ = characterEncoding;
    }

    public void setContentType(String contentType) {
        contentType_ = contentType;
    }

    public void setInputStream(InputStream inputStream) {
        inputStream_ = inputStream;
    }

    public void setPath(String path) {
        path_ = path;
    }

    public void setType(ResponseType type) {
        type_ = type;
    }
}
