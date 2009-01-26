package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

public class FloatConverter extends TypeConverterBase<Float> {
    public FloatConverter() {
        type_ = Float.class;
    }

    @Override
    protected Float doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
        } else if (value instanceof Boolean) {
            return Float.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (new Float(value.toString()));
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
