package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.math.BigInteger;

import org.seasar.ymir.converter.TypeConversionException;

public class BigIntegerConverter extends TypeConverterBase<BigInteger> {
    public BigIntegerConverter() {
        type_ = BigInteger.class;
    }

    @Override
    protected BigInteger doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return BigInteger.valueOf(((Number) value).longValue());
        }

        try {
            return (new BigInteger(value.toString()));
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
