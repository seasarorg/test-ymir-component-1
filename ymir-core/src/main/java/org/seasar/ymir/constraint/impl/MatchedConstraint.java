package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.annotation.Matched;

public class MatchedConstraint extends AbstractRegexConstraint<Matched> {
    @Override
    protected String getConstraintKey() {
        return "matched";
    }

    public void confirm(Object component, Request request, Matched annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        String pattern = annotation.pattern();
        if (pattern.length() == 0) {
            pattern = annotation.value();
        }
        if (pattern.length() == 0) {
            throw new IllegalArgumentException(
                    "Please specify either 'pattern' or 'value' property");
        }
        confirm(request, annotation, element, annotation.property(), pattern,
                annotation.messageKey(), annotation.namePrefixOnNote());
    }
}
