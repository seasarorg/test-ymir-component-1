package org.seasar.ymir.render;

import static org.seasar.ymir.util.StringUtils.asString;

import java.lang.reflect.Array;

import org.seasar.ymir.util.StringUtils;

public abstract class AbstractCandidate implements Candidate {
    private static final long serialVersionUID = 1L;

    protected boolean selected_;

    protected String value_;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append('(');
        sb.append("selected=").append(selected_).append(", ");
        sb.append("value=").append(value_);
        sb.append(')');
        return sb.toString();
    }

    public boolean isSelected() {
        return selected_;
    }

    public void setSelected(boolean selected) {
        selected_ = selected;
    }

    public String getValue() {
        return value_;
    }

    public Integer getValueAsInteger() {
        if (value_ == null) {
            return null;
        } else {
            return Integer.valueOf(value_);
        }
    }

    public void setValue(String value) {
        value_ = value;
    }

    public void setValueObject(Object value) {
        value_ = asString(value);
    }
}
