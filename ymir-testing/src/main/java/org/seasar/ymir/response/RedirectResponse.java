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
public class RedirectResponse extends TransitionResponse {
    public RedirectResponse() {
    }

    public RedirectResponse(String path) {
        super(path);
    }

    public boolean isSubordinate() {
        return false;
    }

    public String toString() {
        return "redirect:" + getPath();
    }

    public ResponseType getType() {
        return ResponseType.REDIRECT;
    }
}
