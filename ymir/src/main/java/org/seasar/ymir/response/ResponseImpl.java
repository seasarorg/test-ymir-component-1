package org.seasar.ymir.response;

import java.io.InputStream;

import org.seasar.ymir.ResponseType;

public class ResponseImpl extends ResponseBase {
    private ResponseType type_ = ResponseType.VOID;

    private String path_;

    private String contentType_;

    private InputStream inputStream_;

    public String getContentType() {

        return contentType_;
    }

    public void setContentType(String contentType) {

        contentType_ = contentType;
    }

    public InputStream getInputStream() {

        return inputStream_;
    }

    public void setInputStream(InputStream inputStream) {

        inputStream_ = inputStream;
    }

    public String getPath() {

        return path_;
    }

    public void setPath(String path) {

        path_ = path;
    }

    public ResponseType getType() {

        return type_;
    }

    public void setType(ResponseType type) {

        type_ = type;
    }
}
