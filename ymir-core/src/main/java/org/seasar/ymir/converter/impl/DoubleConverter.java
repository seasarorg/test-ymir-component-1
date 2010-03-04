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
            if (log_.isDebugEnabled()) {
                log_.debug("Conversion error occured."
                        + " You may add a constraint annotation"
                        + " to the corresponding property"
                        + " in order to notify validation error to a user: "
                        + value, ex);
            }
            throw new TypeConversionException(ex, value);
        }
    }
}
