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
    }

    @Override
    public void setDateHeader(String name, long value) {
    }

    @Override
    public void setHeader(String name, String value) {
    }

    @Override
    public void setInputStream(InputStream is) {
    }

    @Override
    public void setIntHeader(String name, int value) {
    }

    @Override
    public void setPath(String path) {
    }

    @Override
    public void setStatus(int status) {
    }

    @Override
    public void setSubordinate(boolean subordinate) {
    }

    @Override
    public void setType(ResponseType type) {
    }

    @Override
    public void addDateHeader(String name, long value) {
    }

    @Override
    public void addHeader(String name, String value) {
    }

    @Override
    public void addIntHeader(String name, int value) {
    }
}
