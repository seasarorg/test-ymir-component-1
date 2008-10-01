package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.seasar.ymir.constraint.impl.DateConstraint;

abstract public class DateConverterBase<T extends Date> extends
        TypeConverterBase<T> {
    protected String pattern_;

    public DateConverterBase() {
    }

    public void setPattern(String pattern) {
        pattern_ = pattern;
    }

    protected String getPattern(Annotation[] hint) {
        String pattern = pattern_;
        for (int i = 0; i < hint.length; i++) {
            if (hint[i].annotationType() == org.seasar.ymir.constraint.annotation.Date.class) {
                org.seasar.ymir.constraint.annotation.Date annotation = (org.seasar.ymir.constraint.annotation.Date) hint[i];
                if (annotation.pattern().length() > 0) {
                    pattern = annotation.pattern();
                } else if (annotation.value().length() > 0) {
                    pattern = annotation.value();
                } else {
                    pattern = DateConstraint.PATTERN;
                }
                break;
            }
        }
        return pattern;
    }

    public String convertToString(T value, Annotation[] hint) {
        return new SimpleDateFormat(getPattern(hint)).format(value);
    }
}
