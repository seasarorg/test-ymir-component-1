package org.seasar.ymir.render;

import java.lang.reflect.Array;

public abstract class AbstractCandidate implements Candidate {
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

    public void setValue(String value) {
        value_ = value;
    }
}
