package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

public class ByteConverter extends TypeConverterBase<Byte> {
    public ByteConverter() {
        type_ = Byte.class;
    }

    @Override
    protected Byte doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return Byte.valueOf(((Number) value).byteValue());
        } else if (value instanceof Boolean) {
            return Byte.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            return (new Byte(value.toString()));
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
