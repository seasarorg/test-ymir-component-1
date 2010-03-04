package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;

public class IntegerConverter extends TypeConverterBase<Integer> {
    public IntegerConverter() {
        type_ = Integer.class;
    }

    @Override
    protected Integer doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        } else if (value instanceof Boolean) {
            return Integer.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (Integer.valueOf(value.toString()));
        } catch (Exception ex) {
            log_.debug("Conversion error occured."
                    + " You may add a constraint annotation"
                    + " to the corresponding property"
                    + " in order to notify validation error to a user: "
                    + value, ex);
            throw new TypeConversionException(ex, value);
        }
    }
}
