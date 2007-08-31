package org.seasar.ymir.constraint;

import org.seasar.ymir.Notes;

public class PermissionDeniedException extends ConstraintViolatedException {

    private static final long serialVersionUID = -7495239316902981080L;

    public PermissionDeniedException() {
    }

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(Throwable cause) {
        super(cause);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionDeniedException(Notes notes) {
        super(notes);
    }
}
