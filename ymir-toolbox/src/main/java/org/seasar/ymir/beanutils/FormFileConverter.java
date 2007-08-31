package org.seasar.ymir.beanutils;

import org.apache.commons.beanutils.Converter;
import org.seasar.ymir.FormFile;

public class FormFileConverter implements Converter {

    public Object convert(Class type, Object value) {

        if (value instanceof FormFile) {
            return value;
        } else if (value instanceof FormFile[]) {
            FormFile[] formFiles = (FormFile[]) value;
            if (formFiles.length > 0) {
                return formFiles[0];
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
