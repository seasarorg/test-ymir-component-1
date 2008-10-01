package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SqlTimestampConverter extends DateConverterBase<Timestamp> {
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss.fffffffff";

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
            if (pattern.equals(PATTERN)) {
                return Timestamp.valueOf(value.toString());
            } else {
                return new Timestamp(new SimpleDateFormat(getPattern(hint))
                        .parse(value.toString()).getTime());
            }
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
