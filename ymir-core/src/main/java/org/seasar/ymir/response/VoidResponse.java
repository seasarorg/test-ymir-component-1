package org.seasar.ymir.response;

import java.io.InputStream;

import org.seasar.ymir.ResponseType;

/**
 * フレームワークにレスポンスを出力して欲しくないことを表すResponseオブジェクトです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class VoidResponse extends ResponseBase {
    public static final VoidResponse INSTANCE = new VoidResponse();

    private VoidResponse() {
    }

    public ResponseType getType() {
        return ResponseType.VOID;
    }

    public boolean isSubordinate() {
        return false;
    }

    @Override
    public void setContentType(String contentType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDateHeader(String name, long value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setInputStream(InputStream is) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIntHeader(String name, int value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPath(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatus(int status) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSubordinate(boolean subordinate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setType(ResponseType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addDateHeader(String name, long value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addHeader(String name, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addIntHeader(String name, int value) {
        throw new UnsupportedOperationException();
    }
}
