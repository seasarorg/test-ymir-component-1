package org.seasar.ymir.beanutils;

import org.apache.commons.beanutils.Converter;
import org.seasar.ymir.FormFile;

/**
 * {@link FormFile}の配列型のためのコンバータクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class FormFileArrayConverter implements Converter {
    @SuppressWarnings("unchecked")
    public Object convert(Class type, Object value) {
        if (value instanceof FormFile[]) {
            return value;
        } else if (value instanceof FormFile) {
            return new FormFile[] { (FormFile) value };
        } else {
            return new FormFile[0];
        }
    }
}
