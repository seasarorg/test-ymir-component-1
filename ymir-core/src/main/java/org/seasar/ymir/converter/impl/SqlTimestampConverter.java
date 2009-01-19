package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SqlTimestampConverter extends DateConverterBase<Timestamp> {
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    public SqlTimestampConverter() {
        type_ = Timestamp.class;
        pattern_ = PATTERN;
    }

    @Override
    protected Timestamp doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return new Timestamp(((Number) value).longValue());
        } else if (value instanceof java.util.Date) {
            return new Timestamp(((java.util.Date) value).getTime());
        }

        String pattern = getPattern(hint);
        try {
            return new Timestamp(new SimpleDateFormat(pattern).parse(
                    value.toString()).getTime());
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
