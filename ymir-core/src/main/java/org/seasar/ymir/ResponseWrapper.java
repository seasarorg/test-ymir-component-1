package org.seasar.ymir;

import java.io.InputStream;

/**
 * {@link Response}インタフェースのラッパクラスです。
 * <p>Responseオブジェクトをラップしたい場合はこのクラスまたはこのクラスのサブクラスを使うようにして下さい。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class ResponseWrapper implements Response {
    private Response response_;

    public ResponseWrapper(Response response) {
        response_ = response;
    }

    public Response getResponse() {
        return response_;
    }

    public void addDateHeader(String name, long value) {
        response_.addDateHeader(name, value);
    }

    public void addHeader(String name, String value) {
        response_.addHeader(name, value);
    }

    public void addIntHeader(String name, int value) {
        response_.addIntHeader(name, value);
    }

    public boolean containsHeader(String name) {
        return response_.containsHeader(name);
    }

    public String getCharacterEncoding() {
        return response_.getCharacterEncoding();
    }

    public String getContentType() {
        return response_.getContentType();
    }

    public InputStream getInputStream() {
        return response_.getInputStream();
    }

    public String getPath() {
        return response_.getPath();
    }

    public ResponseHeader[] getResponseHeaders() {
        return response_.getResponseHeaders();
    }

    public int getStatus() {
        return response_.getStatus();
    }

    public ResponseType getType() {
        return response_.getType();
    }

    public void setContentType(String contentType) {
        response_.setContentType(contentType);
    }

    public void setDateHeader(String name, long value) {
        response_.setDateHeader(name, value);
    }

    public void setHeader(String name, String value) {
        response_.setHeader(name, value);
    }

    public void setInputStream(InputStream is) {
        response_.setInputStream(is);
    }

    public void setIntHeader(String name, int value) {
        response_.setIntHeader(name, value);
    }

    public void setPath(String path) {
        response_.setPath(path);
    }

    public void setStatus(int status) {
        response_.setStatus(status);
    }

    public boolean isSubordinate() {
        return response_.isSubordinate();
    }
}
