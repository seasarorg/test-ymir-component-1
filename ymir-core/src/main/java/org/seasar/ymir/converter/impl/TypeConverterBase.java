package org.seasar.ymir.converter.impl;

import java.lang.annotation.Annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.ymir.converter.TypeConversionException;
import org.seasar.ymir.converter.TypeConverter;

/**
 * TypeConverterを作成する際のベースとすることのできる抽象クラスです。
 * 
 * @author yokota
 * @since 1.0.0
 */
abstract public class TypeConverterBase<T> implements TypeConverter<T> {
    protected Class<T> type_;

    protected T defaultValue_;

    protected final Log log_ = LogFactory.getLog(getClass());

    public Class<T> getType() {
        return type_;
    }

    public void setType(Class<T> type) {
        type_ = type;
    }

    public void setDefaultValue(T defaultValue) {
        defaultValue_ = defaultValue;
    }

    @SuppressWarnings("unchecked")
    public T convert(Object value, Annotation[] hint) {
        try {
            return tryToConvert(value, hint);
        } catch (TypeConversionException ex) {
            return defaultValue_;
        }
    }

    @SuppressWarnings("unchecked")
    public T tryToConvert(Object value, Annotation[] hint)
            throws TypeConversionException {
        if (value == null) {
            return defaultValue_;
        } else if (getType().isAssignableFrom(value.getClass())) {
            return (T) value;
        } else if (value.toString().length() == 0) {
            return defaultValue_;
        } else {
            return doConvert(value, hint);
        }
    }

    /**
     * 指定された任意の型のオブジェクトをこのインタフェースに対応付けられている型のオブジェクトに変換します。
     * <p>valueはnullでも{@link TypeConverter#getType()}のインスタンスでもありません。
     * </p>
     * 
     * @param value 変換元のオブジェクト。
     * @param hint 変換のためのヒント。nullを指定することはできません。
     * 指定したくない場合は空の配列を指定して下さい。
     * @return 変換結果。
     * @throws TypeConversionException 変換できなかった場合。
     */
    abstract protected T doConvert(Object value, Annotation[] hint)
            throws TypeConversionException;

    public String convertToString(T value, Annotation[] hint) {
        return value.toString();
    }
}
