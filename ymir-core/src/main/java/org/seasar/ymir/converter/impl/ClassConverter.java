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
            log_.debug("Conversion error occured."
                    + " You may add a constraint annotation"
                    + " to the corresponding property"
                    + " in order to notify validation error to a user: "
                    + value, ex);
            return defaultValue_;
        }
    }
}
