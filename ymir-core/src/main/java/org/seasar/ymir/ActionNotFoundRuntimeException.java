package org.seasar.ymir;

/**
 * HTTPリクエストに対応するPageクラスのアクションメソッドが見つからなかった場合にスローされる例外クラスです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
public class ActionNotFoundRuntimeException extends BadRequestRuntimeException {
    private static final long serialVersionUID = 1L;

    private String method_;

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param path ページのパス。
     */
    public ActionNotFoundRuntimeException(String path) {
        super(path);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param path ページのパス。
     * @param method HTTPメソッド名。
     */
    public ActionNotFoundRuntimeException(String path, String method) {
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
    public ActionNotFoundRuntimeException setMethod(String method) {
        method_ = method;
        return this;
    }
}
