package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;

public class CharacterConverter extends TypeConverterBase<Character> {
    public CharacterConverter() {
        type_ = Character.class;
    }

    @Override
    protected Character doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value instanceof Number) {
            return Character.valueOf((char) ((Number) value).shortValue());
        } else if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? '1' : '0';
        }

        try {
            return (new Character(value.toString().charAt(0)));
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
