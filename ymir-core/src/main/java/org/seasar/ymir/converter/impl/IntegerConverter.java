package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

public class IntegerConverter extends TypeConverterBase<Integer> {
    public IntegerConverter() {
        type_ = Integer.class;
    }

    @Override
    protected Integer doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        } else if (value instanceof Boolean) {
            return Integer.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (new Integer(value.toString()));
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
