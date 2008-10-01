package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

@SuppressWarnings("unchecked")
public class ClassConverter extends TypeConverterBase<Class> {
    public ClassConverter() {
        type_ = Class.class;
    }

    @Override
    protected Class doConvert(Object value, Annotation[] hint) {
        try {
            ClassLoader classLoader = Thread.currentThread()
                    .getContextClassLoader();
            if (classLoader == null) {
                classLoader = ClassConverter.class.getClassLoader();
            }
            return (classLoader.loadClass(value.toString()));
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
