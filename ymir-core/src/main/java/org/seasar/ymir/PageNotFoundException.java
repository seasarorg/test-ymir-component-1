package org.seasar.ymir;

/**
 * HTTPリクエストに対応するページが存在しなかった場合にスローされる例外クラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class PageNotFoundException extends Exception {
    private static final long serialVersionUID = 4300901393295361786L;

    private String path_;

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param path ページのパス。
     */
    public PageNotFoundException(String path) {
        path_ = path;
    }

    public String toString() {
        return "Page not found: " + path_;
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
