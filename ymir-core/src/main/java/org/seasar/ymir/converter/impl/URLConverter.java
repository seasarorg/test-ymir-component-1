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
