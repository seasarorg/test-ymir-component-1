package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import javax.servlet.http.HttpServletRequest;

import org.seasar.ymir.Note;
import org.seasar.ymir.Request;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.TokenRequired;
import org.seasar.ymir.util.TokenUtils;

public class TokenRequiredConstraint extends AbstractConstraint<TokenRequired> {
    public void confirm(Object component, Request request,
            TokenRequired annotation, AnnotatedElement element)
            throws ConstraintViolatedException {
        if (!TokenUtils.isTokenValid(getHttpServletRequest(), annotation
                .value(), true)) {
            throw new ValidationFailedException().addNote(new Note(
                    PREFIX_MESSAGEKEY + "tokenRequired", new Object[0]));
        }
    }

    HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(HttpServletRequest.class);
    }
}
