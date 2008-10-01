package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

public class DoubleConverter extends TypeConverterBase<Double> {
    public DoubleConverter() {
        type_ = Double.class;
    }

    @Override
    protected Double doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            return Double.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (new Double(value.toString()));
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
