package org.seasar.ymir.conversation;

public class IllegalTransitionRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 3084715814456978818L;

    public IllegalTransitionRuntimeException() {
    }

    public IllegalTransitionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTransitionRuntimeException(String message) {
        super(message);
    }

    public IllegalTransitionRuntimeException(Throwable cause) {
        super(cause);
    }
}
