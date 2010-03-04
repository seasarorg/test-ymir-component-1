package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;
import org.seasar.ymir.converter.TypeConverter;

public class ObjectConverter implements TypeConverter<Object> {
    public Class<Object> getType() {
        return Object.class;
    }

    public Object convert(Object value, Annotation[] hint) {
        try {
            return tryToConvert(value, hint);
        } catch (TypeConversionException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }
    }

    public Object tryToConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        return value;
    }

    public String convertToString(Object value, Annotation[] hint) {
        return value.toString();
    }
}
