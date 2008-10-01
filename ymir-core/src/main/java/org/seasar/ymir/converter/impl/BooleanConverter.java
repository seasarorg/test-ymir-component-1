package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

public class BooleanConverter extends TypeConverterBase<Boolean> {
    public BooleanConverter() {
        type_ = Boolean.class;
    }

    @Override
    protected Boolean doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return Boolean.valueOf(((Number) value).doubleValue() != 0);
        }

        String stringValue = value.toString();
        if (stringValue.equalsIgnoreCase("yes")
                || stringValue.equalsIgnoreCase("y")
                || stringValue.equalsIgnoreCase("true")
                || stringValue.equalsIgnoreCase("on")
                || stringValue.equalsIgnoreCase("1")) {
            return Boolean.TRUE;
        } else if (stringValue.equalsIgnoreCase("no")
                || stringValue.equalsIgnoreCase("n")
                || stringValue.equalsIgnoreCase("false")
                || stringValue.equalsIgnoreCase("off")
                || stringValue.equalsIgnoreCase("0")) {
            return (Boolean.FALSE);
        } else {
            return defaultValue_;
        }
    }
}
