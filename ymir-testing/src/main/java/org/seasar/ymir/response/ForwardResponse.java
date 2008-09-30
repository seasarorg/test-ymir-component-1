package org.seasar.ymir.response;

import org.seasar.ymir.ResponseType;

/**
 * フォワードすることを表すResponseオブジェクトです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class ForwardResponse extends TransitionResponse {
    public ForwardResponse() {
    }

    public ForwardResponse(String path) {
        super(path);
    }

    public boolean isSubordinate() {
        return true;
    }

    @Override
    public String toString() {
        return "forward:" + getPath();
    }

    public ResponseType getType() {
        return ResponseType.FORWARD;
    }
}
