package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;

import org.seasar.ymir.converter.TypeConversionException;

public class BigDecimalConverter extends TypeConverterBase<BigDecimal> {
    public BigDecimalConverter() {
        type_ = BigDecimal.class;
    }

    @Override
    protected BigDecimal doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }

        try {
            return new BigDecimal(value.toString());
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
