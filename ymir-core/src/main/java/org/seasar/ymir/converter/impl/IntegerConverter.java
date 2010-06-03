package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;

public class IntegerConverter extends TypeConverterBase<Integer> {
    public IntegerConverter() {
        type_ = Integer.class;
    }

    @Override
    protected Integer doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        } else if (value instanceof Boolean) {
            return Integer.valueOf((byte) (((Boolean) value).booleanValue() ? 1
                    : 0));
        }

        try {
            // 2010-06-03: Double.parseDouble()は先頭に空白がついていてもparseできるが
            // Integerのparseではできない。そのため@NumericでOKでもここで
            // エラーになるというケースがあった（例：「 1」）ため、trimするようにしている。
            // Thanks sue
            return (Integer.valueOf(value.toString().trim()));
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
