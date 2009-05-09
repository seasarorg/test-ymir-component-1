package org.seasar.ymir.render.html;

import org.seasar.ymir.util.HTMLUtils;

/**
 * HTMLのoptionタグを扱うためのクラスです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public class Option extends Tag {
    private static final long serialVersionUID = 1L;

    private boolean disabled_;

    private boolean selected_;

    private String value_;

    private String label_;

    public Option() {
    }

    public Option(Object value) {
        this(value, value);
    }

    public Option(Object value, Object content) {
        value_ = value != null ? value.toString() : null;
        setContent(content);
    }

    protected void writeName(StringBuilder sb) {
        sb.append("option");
    }

    protected void writeAttributes(StringBuilder sb) {
        super.writeAttributes(sb);
        if (label_ != null) {
            sb.append(" label=\"").append(HTMLUtils.filter(label_))
                    .append("\"");
        }
        if (value_ != null) {
            sb.append(" value=\"").append(HTMLUtils.filter(value_))
                    .append("\"");
        }
        if (selected_) {
            sb.append(" selected=\"selected\"");
        }
        if (disabled_) {
            sb.append(" disabled=\"disabled\"");
        }
    }

    /*
     * public scope methods
     */

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

    public Option setValue(Object value) {
        value_ = value != null ? value.toString() : null;
        return this;
    }

    public boolean isDisabled() {
        return disabled_;
    }

    public Option setDisabled(boolean disabled) {
        disabled_ = disabled;
        return this;
    }

    public boolean isSelected() {
        return selected_;
    }

    public Option setSelected(boolean selected) {
        selected_ = selected;
        return this;
    }

    public String getLabel() {
        return label_;
    }

    public Option setLabel(String label) {
        label_ = label;
        return this;
    }
}
