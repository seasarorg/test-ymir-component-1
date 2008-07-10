package org.seasar.ymir.response;

import org.seasar.ymir.ResponseType;

/**
 * フレームワークにレスポンスを出力して欲しくないことを表すResponseオブジェクトです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class VoidResponse extends ResponseBase {
    public static final VoidResponse INSTANCE = new VoidResponse();

    public ResponseType getType() {
        return ResponseType.VOID;
    }

    public boolean isSubordinate() {
        return false;
    }
}
