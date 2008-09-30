package org.seasar.ymir.converter.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public class DateConverter implements Converter {
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private Object defaultValue_;

    private boolean useDefault_;

    private String format_ = DEFAULT_FORMAT;

    public DateConverter() {
        defaultValue_ = null;
        useDefault_ = false;

    }

    public DateConverter(Object defaultValue) {
        defaultValue_ = defaultValue;
        useDefault_ = true;
    }

    @SuppressWarnings("unchecked")
    public Object convert(Class type, Object value) {
        if (value == null) {
            if (useDefault_) {
                return defaultValue_;
            } else {
                throw new ConversionException("No value specified");
            }
        }

        if (value instanceof Date) {
            return value;
        } else if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }

        try {
            return new SimpleDateFormat(format_).parse(value.toString());
        } catch (Exception ex) {
            if (useDefault_) {
                return defaultValue_;
            } else {
                throw new ConversionException(ex);
            }
        }
    }
}
