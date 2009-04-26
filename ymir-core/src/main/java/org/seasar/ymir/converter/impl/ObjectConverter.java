package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConverter;

public class ObjectConverter implements TypeConverter<Object> {
    public Class<Object> getType() {
        return Object.class;
    }

    public Object convert(Object value, Annotation[] hint) {
        return value;
    }

    public String convertToString(Object value, Annotation[] hint) {
        return value.toString();
    }
}
