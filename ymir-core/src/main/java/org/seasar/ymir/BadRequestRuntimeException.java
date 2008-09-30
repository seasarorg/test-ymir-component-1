package org.seasar.ymir;

/**
 * 不正なリクエストを検出した場合にフレームワークからスローされる例外です。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
public class BadRequestRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String path_;

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param path ページのパス。
     */
    public BadRequestRuntimeException(String path) {
        path_ = path;
    }

    public String toString() {
        return "Bad request: " + path_;
    }

    /**
     * ページのパスを返します。
     * 
     * @return ページのパス。
     */
    public String getPath() {
        return path_;
    }
}
