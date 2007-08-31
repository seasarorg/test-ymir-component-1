package org.seasar.ymir;

public class Attribute {
    private String name_;

    private Object value_;

    public Attribute(String name, Object value) {
        name_ = name;
        value_ = value;
    }

    public String getName() {
        return name_;
    }

    public Object getValue() {
        return value_;
    }
}
