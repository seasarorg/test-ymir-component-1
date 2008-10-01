package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

public class ShortConverter extends TypeConverterBase<Short> {
    public ShortConverter() {
        type_ = Short.class;
    }

    @Override
    protected Short doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return Short.valueOf(((Number) value).shortValue());
        } else if (value instanceof Boolean) {
            return Short.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (new Short(value.toString()));
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
