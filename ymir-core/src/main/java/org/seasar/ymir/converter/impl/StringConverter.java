package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConverter;

public class StringConverter implements TypeConverter<String> {
    public Class<String> getType() {
        return String.class;
    }

    public String convert(Object value, Annotation[] hint) {
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
