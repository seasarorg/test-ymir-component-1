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
            if (log_.isDebugEnabled()) {
                log_.debug("Conversion error occured."
                        + " You may add a constraint annotation"
                        + " to the corresponding property"
                        + " in order to notify validation error to a user: "
                        + value, ex);
            }
            throw new TypeConversionException(ex, value);
        }
    }
}
