package org.seasar.ymir.response;

import org.seasar.ymir.ResponseType;

/**
 * 遷移先を指定せずサーブレットコンテナの処理に任せることを表すResponseオブジェクトです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class PassthroughResponse extends ResponseBase {
    public String toString() {
        return "(passthrough)";
    }

    public ResponseType getType() {
        return ResponseType.PASSTHROUGH;
    }

    public boolean isSubordinate() {
        return true;
    }
}
