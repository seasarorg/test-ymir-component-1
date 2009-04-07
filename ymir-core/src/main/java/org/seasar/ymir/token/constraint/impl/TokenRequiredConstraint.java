package org.seasar.ymir.token.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.impl.AbstractConstraint;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.token.InvalidTokenRuntimeException;
import org.seasar.ymir.token.TokenManager;
import org.seasar.ymir.token.constraint.annotation.TokenRequired;

public class TokenRequiredConstraint extends AbstractConstraint<TokenRequired> {
    private TokenManager tokenManager_;

    public void setTokenManager(TokenManager tokenManager) {
        tokenManager_ = tokenManager;
    }

    public void confirm(Object component, Request request,
            TokenRequired annotation, AnnotatedElement element)
            throws ConstraintViolatedException {
        String tokenKey = annotation.value();
        if (tokenKey.length() == 0) {
            // デフォルトのキーを使うようにする。
            tokenKey = tokenManager_.getTokenKey();
        }
        if (!tokenManager_.isTokenValid(tokenKey, annotation.reset())) {
            if (annotation.throwException()) {
                throw new InvalidTokenRuntimeException(PREFIX_MESSAGEKEY
                        + "token.tokenRequired");
            } else {
                throw new ValidationFailedException().addNote(new Note(
                        PREFIX_MESSAGEKEY + "token.tokenRequired",
                        new Object[0]));
            }
        }
    }
}
