package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;

@SuppressWarnings("unchecked")
public class ClassConverter extends TypeConverterBase<Class> {
    public ClassConverter() {
        type_ = Class.class;
    }

    @Override
    protected Class doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        try {
            ClassLoader classLoader = Thread.currentThread()
                    .getContextClassLoader();
            if (classLoader == null) {
                classLoader = ClassConverter.class.getClassLoader();
            }
            return (classLoader.loadClass(value.toString()));
        } catch (Exception ex) {
            throw new TypeConversionException(ex, value, getType());
        }
    }
}
