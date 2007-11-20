package org.seasar.ymir;

public class ResponseHeader {
    private String name_;

    private Object value_;

    private boolean add_;

    public ResponseHeader(String name, Object value) {
        this(name, value, false);
    }

    public ResponseHeader(String name, Object value, boolean add) {
        name_ = name;
        value_ = value;
        add_ = add;
    }

    @Override
    public String toString() {
        return name_ + "=" + value_ + " (add=" + add_;
    }

    public boolean isAdd() {
        return add_;
    }

    public String getName() {
        return name_;
    }

    public Object getValue() {
        return value_;
    }
}
