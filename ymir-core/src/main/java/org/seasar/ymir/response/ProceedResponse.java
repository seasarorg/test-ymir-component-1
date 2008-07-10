package org.seasar.ymir.response;

import org.seasar.ymir.ResponseType;

/**
 * リダイレクトとほぼ同じような特徴を持つが内部的にはフォワードを使うような遷移を表すResponseオブジェクトです。
 * <p>携帯アプリケーション等でリダイレクトを多用したくない場合はリダイレクトの代わりに利用します。
 * </p>
 * <p>forwardレスポンスでは、パスに指定されたリクエストパラメータと元のリクエストに指定されたリクエストパラメータの両方が遷移先に渡されます。
 * また遷移先から見える「HTTPメソッド」は元のリクエストに指定されたものになります。
 * これに対してproceedレスポンスでは、元のリクエストに指定されたリクエストパラメータは遷移先に渡されません。
 * また遷移先から見える「現在のHTTPメソッド」が強制的にGETメソッドになります。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class ProceedResponse extends TransitionResponse {
    public ProceedResponse() {
    }

    public ProceedResponse(String path) {
        super(path);
    }

    public boolean isSubordinate() {
        return false;
    }

    @Override
    public String toString() {
        return "proceed:" + getPath();
    }

    public ResponseType getType() {
        return ResponseType.FORWARD;
    }
}
