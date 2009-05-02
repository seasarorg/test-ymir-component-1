package org.seasar.ymir.render;

public abstract class AbstractCandidate implements Candidate {
    protected String value_;

    protected boolean selected_;

    public String getValue() {
        return value_;
    }

    public boolean isSelected() {
        return selected_;
    }

    public void setSelected(boolean selected) {
        selected_ = selected;
    }

    public void setValue(String value) {
        value_ = value;
    }
}
