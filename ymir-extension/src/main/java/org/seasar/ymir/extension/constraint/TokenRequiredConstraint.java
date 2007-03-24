package org.seasar.ymir.extension.constraint;

import javax.servlet.http.HttpServletRequest;

import org.seasar.ymir.Constraint;
import org.seasar.ymir.ConstraintViolatedException;
import org.seasar.ymir.Note;
import org.seasar.ymir.Request;
import org.seasar.ymir.ValidationFailedException;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.util.TokenUtils;

public class TokenRequiredConstraint implements Constraint {

    private String name_;

    public TokenRequiredConstraint(String name) {
        name_ = name;
    }

    public void confirm(Object component, Request request)
            throws ConstraintViolatedException {

        if (!TokenUtils.isTokenValid(getHttpServletRequest(), name_, true)) {
            throw new ValidationFailedException().addNote(new Note(
                    PREFIX_MESSAGEKEY + "tokenRequired", new Object[0]));
        }
    }

    HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(HttpServletRequest.class);
    }
}
