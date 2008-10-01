package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;
import java.net.URL;

public class URLConverter extends TypeConverterBase<URL> {
    public URLConverter() {
        type_ = URL.class;
    }

    @Override
    protected URL doConvert(Object value, Annotation[] hint) {
        try {
            return new URL(value.toString());
        } catch (Exception ex) {
            return defaultValue_;
        }
    }
}
