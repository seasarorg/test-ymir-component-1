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
            if (log_.isDebugEnabled()) {
                log_.debug("Conversion error occured."
                        + " You may add a constraint annotation"
                        + " to the corresponding property"
                        + " in order to notify validation error to a user: "
                        + value, ex);
            }
            return defaultValue_;
        }
    }
}
