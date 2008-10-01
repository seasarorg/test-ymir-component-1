package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.math.BigInteger;

public class BigIntegerConverter extends TypeConverterBase<BigInteger> {
    public BigIntegerConverter() {
        type_ = BigInteger.class;
    }

    @Override
    protected BigInteger doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return BigInteger.valueOf(((Number) value).longValue());
        }

        try {
            return (new BigInteger(value.toString()));
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
