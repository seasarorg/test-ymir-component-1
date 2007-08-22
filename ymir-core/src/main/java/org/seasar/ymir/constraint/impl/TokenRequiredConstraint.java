package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Note;
import org.seasar.ymir.Request;
import org.seasar.ymir.TokenManager;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.TokenRequired;

public class TokenRequiredConstraint extends AbstractConstraint<TokenRequired> {
    private TokenManager tokenManager_;

    public void setTokenManager(TokenManager tokenManager) {
        tokenManager_ = tokenManager;
    }

    public void confirm(Object component, Request request,
            TokenRequired annotation, AnnotatedElement element)
            throws ConstraintViolatedException {
        if (!tokenManager_.isTokenValid(annotation.value(), true)) {
            throw new ValidationFailedException().addNote(new Note(
                    PREFIX_MESSAGEKEY + "tokenRequired", new Object[0]));
        }
    }
}
