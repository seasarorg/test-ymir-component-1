package org.seasar.ymir;

/**
 * クライアントコードが適切にフレームワークを利用していない場合にスローされる例外です。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
public class IllegalClientCodeRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IllegalClientCodeRuntimeException() {
    }

    public IllegalClientCodeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalClientCodeRuntimeException(String message) {
        super(message);
    }

    public IllegalClientCodeRuntimeException(Throwable cause) {
        super(cause);
    }
}
