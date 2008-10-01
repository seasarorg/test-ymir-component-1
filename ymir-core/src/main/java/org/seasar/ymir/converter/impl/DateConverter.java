package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter extends DateConverterBase<Date> {
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public DateConverter() {
        type_ = Date.class;
        pattern_ = PATTERN;
    }

    @Override
    protected Date doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }

        try {
            return new SimpleDateFormat(getPattern(hint)).parse(value
                    .toString());
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
