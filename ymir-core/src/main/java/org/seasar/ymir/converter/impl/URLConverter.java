package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.net.URL;

import org.seasar.ymir.converter.TypeConversionException;

public class URLConverter extends TypeConverterBase<URL> {
    public URLConverter() {
        type_ = URL.class;
    }

    @Override
    protected URL doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        try {
            return new URL(value.toString());
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
