package org.seasar.ymir;

/**
 * HTTPリクエストに対応するPageクラスのアクションメソッドが見つからなかった場合にスローされる例外クラスです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
public class ActionNotFoundRuntimeException extends BadRequestRuntimeException {
    private static final long serialVersionUID = 1L;

    private HttpMethod method_;

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
     * @param method HTTPメソッド。
     */
    public ActionNotFoundRuntimeException(String path, HttpMethod method) {
        super(path);
        setMethod(method);
    }

    public String toString() {
        return "Action not found for: path=" + getPath() + ", method="
                + method_;
    }

    /**
     * HTTPメソッド名を返します。
     * 
     * @return HTTPメソッド名。
     */
    public HttpMethod getMethod() {
        return method_;
    }

    /**
     * HTTPメソッド名を設定します。
     * 
     * @param method HTTPメソッド名。
     * @return このオブジェクト自身。
     */
    public ActionNotFoundRuntimeException setMethod(HttpMethod method) {
        method_ = method;
        return this;
    }
}
