package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.seasar.ymir.converter.TypeConversionException;

public class DateConverter extends DateConverterBase<Date> {
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public DateConverter() {
        type_ = Date.class;
        pattern_ = PATTERN;
    }

    @Override
    protected Date doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }

        try {
            return new SimpleDateFormat(getPattern(hint)).parse(value
                    .toString());
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
