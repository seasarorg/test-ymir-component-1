package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;

public class ByteConverter extends TypeConverterBase<Byte> {
    public ByteConverter() {
        type_ = Byte.class;
    }

    @Override
    protected Byte doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return Byte.valueOf(((Number) value).byteValue());
        } else if (value instanceof Boolean) {
            return Byte.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (Byte.valueOf(value.toString()));
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
