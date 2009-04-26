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
            return (Short.valueOf(value.toString()));
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
