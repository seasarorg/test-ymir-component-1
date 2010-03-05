package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;

public class LongConverter extends TypeConverterBase<Long> {
    public LongConverter() {
        type_ = Long.class;
    }

    @Override
    protected Long doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return Long.valueOf(((Number) value).longValue());
        } else if (value instanceof Boolean) {
            return Long.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (Long.valueOf(value.toString()));
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
