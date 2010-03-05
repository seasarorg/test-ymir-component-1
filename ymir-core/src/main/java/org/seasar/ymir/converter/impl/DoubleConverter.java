package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;

public class DoubleConverter extends TypeConverterBase<Double> {
    public DoubleConverter() {
        type_ = Double.class;
    }

    @Override
    protected Double doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            return Double.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (Double.valueOf(value.toString()));
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
