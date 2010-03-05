package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;

public class ShortConverter extends TypeConverterBase<Short> {
    public ShortConverter() {
        type_ = Short.class;
    }

    @Override
    protected Short doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return Short.valueOf(((Number) value).shortValue());
        } else if (value instanceof Boolean) {
            return Short.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (Short.valueOf(value.toString()));
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
