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
            log_.debug("Conversion error occured."
                    + " You may add a constraint annotation"
                    + " to the corresponding property"
                    + " in order to notify validation error to a user: "
                    + value, ex);
            return defaultValue_;
        }
    }
}
