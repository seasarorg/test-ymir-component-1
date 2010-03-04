package org.seasar.ymir.converter;

/**
 * 型変換に失敗した場合にスローされる例外です。
 * 
 * @since 1.0.7
 */
public class TypeConversionException extends Exception {
    private static final long serialVersionUID = 1L;

    private Object value_;

    public TypeConversionException() {
        this(null);
    }

    public TypeConversionException(Object value) {
        value_ = value;
    }

    public TypeConversionException(Throwable cause) {
        this(cause, null);
    }

    public TypeConversionException(Throwable cause, Object value) {
        super(cause);
        value_ = value;
    }

    public Object getValue() {
        return value_;
    }
}
