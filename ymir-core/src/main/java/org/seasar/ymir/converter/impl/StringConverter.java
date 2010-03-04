package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;
import org.seasar.ymir.converter.TypeConverter;

public class StringConverter implements TypeConverter<String> {
    public Class<String> getType() {
        return String.class;
    }

    public String convert(Object value, Annotation[] hint) {
        try {
            return tryToConvert(value, hint);
        } catch (TypeConversionException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }
    }

    public String tryToConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    public String convertToString(String value, Annotation[] hint) {
        return convert(value, hint);
    }
}
