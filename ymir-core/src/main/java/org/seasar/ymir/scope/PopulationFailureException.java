package org.seasar.ymir.scope;

import org.seasar.ymir.converter.PropertyHandler;

/**
 * @since 1.0.7
 */
public class PopulationFailureException extends Exception {
    private static final long serialVersionUID = 1L;

    private String name_;

    private Object value_;

    private PropertyHandler propertyHandler_;

    public PopulationFailureException() {
    }

    public PopulationFailureException(Throwable cause) {
        super(cause);
    }

    public PopulationFailureException(String name, Object value,
            PropertyHandler handler) {
        name_ = name;
        value_ = value;
        propertyHandler_ = handler;
    }

    public PopulationFailureException(String name, Object value,
            PropertyHandler propertyHandler, Throwable cause) {
        super(cause);
        name_ = name;
        value_ = value;
        propertyHandler_ = propertyHandler;
    }

    public String getName() {
        return name_;
    }

    public Object getValue() {
        return value_;
    }

    public PropertyHandler getPropertyHandler() {
        return propertyHandler_;
    }
}
