package org.seasar.ymir.render.html;

import org.seasar.ymir.util.HTMLUtils;

/**
 * HTMLのinputタグを扱うためのクラスです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public class Input extends Tag {
    private static final long serialVersionUID = 1L;

    public static final String TYPE_BUTTON = "button";

    public static final String TYPE_CHECKBOX = "checkbox";

    public static final String TYPE_FILE = "file";

    public static final String TYPE_HIDDEN = "hidden";

    public static final String TYPE_IMAGE = "image";

    public static final String TYPE_PASSWORD = "password";

    public static final String TYPE_RADIO = "radio";

    public static final String TYPE_RESET = "reset";

    public static final String TYPE_SUBMIT = "submit";

    public static final String TYPE_TEXT = "text";

    private String type_;

    private String name_;

    private String value_;

    private boolean checked_;

    private boolean disabled_;

    public Input() {
    }

    public Input(Object value) {
        this(value, value);
    }

    public Input(Object value, Object content) {
        super(content);
        value_ = value != null ? value.toString() : null;
    }

    protected void writeName(StringBuilder sb) {
        sb.append("input");
    }

    protected void writeAttributes(StringBuilder sb) {
        super.writeAttributes(sb);
        if (type_ != null) {
            sb.append(" type=\"").append(HTMLUtils.filter(type_)).append("\"");
        }
        if (name_ != null) {
            sb.append(" name=\"").append(HTMLUtils.filter(name_)).append("\"");
        }
        if (value_ != null) {
            sb.append(" value=\"").append(HTMLUtils.filter(value_))
                    .append("\"");
        }
        if (checked_) {
            sb.append(" checked=\"checked\"");
        }
        if (disabled_) {
            sb.append(" disabled=\"disabled\"");
        }
    }

    /*
     * public scope methods
     */

    public String getType() {
        return type_;
    }

    public Input setType(String type) {
        type_ = type;
        return this;
    }

    public String getName() {
        return name_;
    }

    public Input setName(String name) {
        name_ = name;
        return this;
    }

    public String[] getValue() {
        return new String[] { getInputValue() };
    }

    /**
     * JavaBeansプロパティとしてアクセスできるように用意している。
     */
    public void setValue(String[] value) {
        if (TYPE_CHECKBOX.equals(type_) || TYPE_RADIO.equals(type_)) {
            checked_ = false;
            for (int i = 0; i < value.length; i++) {
                if (value[i].equals(value_)) {
                    checked_ = true;
                    break;
                }
            }
        } else {
            setInputValue(value.length > 0 ? value[0] : null);
        }
    }

    public String getInputValue() {
        return value_;
    }

    public Input setInputValue(Object value) {
        value_ = value != null ? value.toString() : null;
        return this;
    }

    public boolean isDisabled() {
        return disabled_;
    }

    public Input setDisabled(boolean disabled) {
        disabled_ = disabled;
        return this;
    }

    public boolean isChecked() {
        return checked_;
    }

    public Input setChecked(boolean checked) {
        checked_ = checked;
        return this;
    }
}
