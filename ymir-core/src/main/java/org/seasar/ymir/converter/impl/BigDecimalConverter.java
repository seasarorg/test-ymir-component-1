package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;

public class BigDecimalConverter extends TypeConverterBase<BigDecimal> {
    public BigDecimalConverter() {
        type_ = BigDecimal.class;
    }

    @Override
    protected BigDecimal doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }

        try {
            return (new BigDecimal(value.toString()));
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
