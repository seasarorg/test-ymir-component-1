package org.seasar.ymir.constraint;

import org.seasar.ymir.Notes;

public class ValidationFailedException extends ConstraintViolatedException {

    private static final long serialVersionUID = -835167364152293726L;

    public ValidationFailedException() {
    }

    public ValidationFailedException(String message) {

        super(message);
    }

    public ValidationFailedException(Throwable cause) {

        super(cause);
    }

    public ValidationFailedException(String message, Throwable cause) {

        super(message, cause);
    }

    public ValidationFailedException(Notes notes) {

        super(notes);
    }
}
