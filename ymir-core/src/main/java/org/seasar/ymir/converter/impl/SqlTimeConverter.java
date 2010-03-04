package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.sql.Time;
import java.text.SimpleDateFormat;

import org.seasar.ymir.converter.TypeConversionException;

public class SqlTimeConverter extends DateConverterBase<Time> {
    public static final String PATTERN = "HH:mm:ss";

    public SqlTimeConverter() {
        type_ = Time.class;
        pattern_ = PATTERN;
    }

    @Override
    protected Time doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return new Time(((Number) value).longValue());
        } else if (value instanceof java.util.Date) {
            return new Time(((java.util.Date) value).getTime());
        }

        try {
            return new Time(new SimpleDateFormat(getPattern(hint)).parse(
                    value.toString()).getTime());
        } catch (Exception ex) {
            if (log_.isDebugEnabled()) {
                log_.debug("Conversion error occured."
                        + " You may add a constraint annotation"
                        + " to the corresponding property"
                        + " in order to notify validation error to a user: "
                        + value, ex);
            }
            throw new TypeConversionException(ex, value);
        }
    }
}
