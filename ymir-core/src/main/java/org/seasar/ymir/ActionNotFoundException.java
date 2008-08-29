package org.seasar.ymir;

/**
 * HTTPリクエストに対応するPageクラスのアクションメソッドが見つからなかった場合にスローされる例外クラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class ActionNotFoundException extends PageNotFoundException {
    private static final long serialVersionUID = 5885040967668460745L;

    private String method_;

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param path ページのパス。
     */
    public ActionNotFoundException(String path) {
        super(path);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param path ページのパス。
     * @param method HTTPメソッド名。
     */
    public ActionNotFoundException(String path, String method) {
        super(path);
        setMethod(method);
    }

    /**
     * HTTPメソッド名を返します。
     * 
     * @return HTTPメソッド名。
     */
    public String getMethod() {
        return method_;
    }

    /**
     * HTTPメソッド名を設定します。
     * 
     * @param method HTTPメソッド名。
     * @return このオブジェクト自身。
     */
    public ActionNotFoundException setMethod(String method) {
        method_ = method;
        return this;
    }
}
