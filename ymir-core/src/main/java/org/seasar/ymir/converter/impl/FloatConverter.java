package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;

public class FloatConverter extends TypeConverterBase<Float> {
    public FloatConverter() {
        type_ = Float.class;
    }

    @Override
    protected Float doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
        } else if (value instanceof Boolean) {
            return Float.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (Float.valueOf(value.toString()));
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
