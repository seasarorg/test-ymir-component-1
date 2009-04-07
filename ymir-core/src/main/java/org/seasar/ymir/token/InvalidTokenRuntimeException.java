package org.seasar.ymir.token;

import org.seasar.ymir.token.constraint.annotation.TokenRequired;

/**
 * トランザクショントークンが不正である場合にスローされる例外です。
 * <p>この例外は、{@link TokenRequired}アノテーションのthrowException要素の値がtrueのケースで、
 * トランザクショントークンが不正であった場合にスローされます。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 * @see TokenRequired#throwException()
 */
public class InvalidTokenRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 3814979643452122075L;

    public InvalidTokenRuntimeException() {
    }

    public InvalidTokenRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenRuntimeException(String message) {
        super(message);
    }

    public InvalidTokenRuntimeException(Throwable cause) {
        super(cause);
    }
}
