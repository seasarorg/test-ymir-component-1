package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.seasar.ymir.converter.TypeConversionException;

public class SqlTimestampConverter extends DateConverterBase<Timestamp> {
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss.fffffffff";

    private static final String[] PATTERNS = new String[] { PATTERN,
        "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", };

    public SqlTimestampConverter() {
        type_ = Timestamp.class;
        pattern_ = PATTERN;
    }

    @Override
    protected Timestamp doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return new Timestamp(((Number) value).longValue());
        } else if (value instanceof java.util.Date) {
            return new Timestamp(((java.util.Date) value).getTime());
        }

        String pattern = getPatternOrNull(hint);
        if (pattern == null) {
            Throwable cause = null;
            for (String ptn : PATTERNS) {
                try {
                    return convertToTimestamp(value, ptn);
                } catch (Exception ex) {
                    cause = ex;
                }
            }
            throw new TypeConversionException(cause, value, getType());
        } else {
            try {
                return convertToTimestamp(value, pattern);
            } catch (Exception ex) {
                throw new TypeConversionException(ex, value, getType());
            }
        }
    }

    private Timestamp convertToTimestamp(Object value, String pattern)
            throws IllegalArgumentException, ParseException {
        if (pattern.equals(PATTERN)) {
            return Timestamp.valueOf(value.toString());
        } else {
            return new Timestamp(new SimpleDateFormat(pattern).parse(
                    value.toString()).getTime());
        }
    }

    @Override
    public String convertToString(Timestamp value, Annotation[] hint) {
        String pattern = getPattern(hint);
        if (pattern.equals(PATTERN)) {
            return value.toString();
        } else {
            return new SimpleDateFormat(getPattern(hint)).format(value);
        }
    }
}
