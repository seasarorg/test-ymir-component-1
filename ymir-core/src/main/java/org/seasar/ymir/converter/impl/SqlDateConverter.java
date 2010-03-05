package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.seasar.ymir.converter.TypeConversionException;

public class SqlDateConverter extends DateConverterBase<Date> {
    public static final String PATTERN = "yyyy-MM-dd";

    public SqlDateConverter() {
        type_ = Date.class;
        pattern_ = PATTERN;
    }

    @Override
    protected Date doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        } else if (value instanceof java.util.Date) {
            return new Date(((java.util.Date) value).getTime());
        }

        try {
            return new Date(new SimpleDateFormat(getPattern(hint)).parse(
                    value.toString()).getTime());
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
