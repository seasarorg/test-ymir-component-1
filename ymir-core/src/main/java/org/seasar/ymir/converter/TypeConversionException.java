package org.seasar.ymir.converter;

/**
 * 型変換に失敗した場合にスローされる例外です。
 * 
 * @since 1.0.7
 */
public class TypeConversionException extends Exception {
    private static final long serialVersionUID = 1L;

    private Object value_;

    private Class<?> type_;

    public TypeConversionException() {
        this(null, null);
    }

    public TypeConversionException(Object value, Class<?> type) {
        value_ = value;
        type_ = type;
    }

    public TypeConversionException(Throwable cause) {
        this(cause, null, null);
    }

    public TypeConversionException(Throwable cause, Object value, Class<?> type) {
        super(cause);
        value_ = value;
        type_ = type;
    }

    public Object getValue() {
        return value_;
    }

    public Class<?> getType() {
        return type_;
    }
}
