package org.seasar.ymir.converter.impl;

import java.io.File;
import java.lang.annotation.Annotation;

import org.seasar.ymir.converter.TypeConversionException;

public class FileConverter extends TypeConverterBase<File> {
    public FileConverter() {
        type_ = File.class;
    }

    @Override
    protected File doConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        return new File(value.toString());
    }
}
