package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class SqlTimeConverter extends DateConverterBase<Time> {
    public static final String PATTERN = "HH:mm:ss";

    public SqlTimeConverter() {
        type_ = Time.class;
        pattern_ = PATTERN;
    }

    @Override
    protected Time doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return new Time(((Number) value).longValue());
        } else if (value instanceof java.util.Date) {
            return new Time(((java.util.Date) value).getTime());
        }

        try {
            return new Time(new SimpleDateFormat(getPattern(hint)).parse(
                    value.toString()).getTime());
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
