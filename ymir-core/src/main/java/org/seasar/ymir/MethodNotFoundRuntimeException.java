package org.seasar.ymir;

import java.lang.reflect.Method;

/**
 * メソッドが存在しない場合にスローされる例外です。
 * 
 * @author YOKOTA Takehiko
 */
public class MethodNotFoundRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Method method_;

    public MethodNotFoundRuntimeException() {
    }

    public MethodNotFoundRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotFoundRuntimeException(String message) {
        super(message);
    }

    public MethodNotFoundRuntimeException(Throwable cause) {
        super(cause);
    }

    public Method getMethod() {
        return method_;
    }

    public MethodNotFoundRuntimeException setMethod(Method method) {
        method_ = method;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getMessage() != null) {
            sb.append(getMessage());
        } else {
            sb.append("Method not found");
        }
        sb.append(": ").append(method_);
        return sb.toString();
    }
}
