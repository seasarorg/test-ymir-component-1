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
            return (new Long(value.toString()));
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
