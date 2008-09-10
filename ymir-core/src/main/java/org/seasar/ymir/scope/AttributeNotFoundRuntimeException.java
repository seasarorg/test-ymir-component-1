package org.seasar.ymir.scope;

import java.lang.reflect.Method;

public class AttributeNotFoundRuntimeException extends RuntimeException {
    private String name_;

    private Class<?> type_;

    private Method method_;

    private Object component_;

    private static final long serialVersionUID = 1L;

    public AttributeNotFoundRuntimeException() {
    }

    public AttributeNotFoundRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttributeNotFoundRuntimeException(String message) {
        super(message);
    }

    public AttributeNotFoundRuntimeException(Throwable cause) {
        super(cause);
    }

    public String getName() {
        return name_;
    }

    public AttributeNotFoundRuntimeException setName(String name) {
        name_ = name;
        return this;
    }

    public Class<?> getType() {
        return type_;
    }

    public AttributeNotFoundRuntimeException setType(Class<?> type) {
        type_ = type;
        return this;
    }

    public Method getMethod() {
        return method_;
    }

    public AttributeNotFoundRuntimeException setMethod(Method method) {
        method_ = method;
        return this;
    }

    public Object getComponent() {
        return component_;
    }

    public AttributeNotFoundRuntimeException setComponent(Object component) {
        component_ = component;
        return this;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (message == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Attribute (name=").append(name_).append(", type=")
                    .append(type_).append(") not found");

            String delim = ": ";
            if (method_ != null) {
                sb.append(delim).append("method=").append(method_);
                delim = ", ";
            }
            if (component_ != null) {
                sb.append(delim).append("component=").append(component_);
                delim = ", ";
            }
            message = sb.toString();
        }
        return message;
    }
}
