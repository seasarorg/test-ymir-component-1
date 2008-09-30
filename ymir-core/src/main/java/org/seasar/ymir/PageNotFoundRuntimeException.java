package org.seasar.ymir;

/**
 * HTTPリクエストに対応するページが存在しなかった場合にスローされる例外クラスです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
public class PageNotFoundRuntimeException extends BadRequestRuntimeException {
    private static final long serialVersionUID = 1L;

    public PageNotFoundRuntimeException(String path) {
        super(path);
    }
}
