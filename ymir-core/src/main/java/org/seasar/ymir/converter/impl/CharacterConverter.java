package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

public class CharacterConverter extends TypeConverterBase<Character> {
    public CharacterConverter() {
        type_ = Character.class;
    }

    @Override
    protected Character doConvert(Object value, Annotation[] hint) {
        if (value instanceof Number) {
            return Character.valueOf((char) ((Number) value).shortValue());
        } else if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? '1' : '0';
        }

        try {
            return (new Character(value.toString().charAt(0)));
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
