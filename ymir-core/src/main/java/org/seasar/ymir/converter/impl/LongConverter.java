package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

public class LongConverter extends TypeConverterBase<Long> {
    public LongConverter() {
        type_ = Long.class;
    }

    @Override
    protected Long doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return Long.valueOf(((Number) value).longValue());
        } else if (value instanceof Boolean) {
            return Long.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (Long.valueOf(value.toString()));
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
