package org.seasar.ymir.converter.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;

public class StringConverter implements Converter {
    @SuppressWarnings("unchecked")
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Date) {
            return new SimpleDateFormat(DateConverter.DEFAULT_FORMAT)
                    .format((Date) value);
        } else {
            return value.toString();
        }
    }
}
