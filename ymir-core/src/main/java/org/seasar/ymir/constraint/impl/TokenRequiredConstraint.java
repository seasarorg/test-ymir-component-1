package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.TokenRequired;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.token.TokenManager;

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
        if (!tokenManager_.isTokenValid(tokenKey, true)) {
            throw new ValidationFailedException().addNote(new Note(
                    PREFIX_MESSAGEKEY + "tokenRequired", new Object[0]));
        }
    }
}
