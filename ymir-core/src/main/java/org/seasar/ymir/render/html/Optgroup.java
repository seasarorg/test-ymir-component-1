package org.seasar.ymir.render.html;

import java.util.List;

import org.seasar.ymir.util.HTMLUtils;

/**
 * HTMLのoptgroupタグを扱うためのクラスです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public class Optgroup extends Tag {
    private static final long serialVersionUID = 1L;

    private static final String LS = System.getProperty("line.separator");

    private String label_;

    public Optgroup() {
    }

    public Optgroup(String label, List<Option> options) {
        this(label, options.toArray(new Option[0]));
    }

    public Optgroup(String label, Option[] options) {
        setLabel(label);
        setContent(options);
    }

    protected void writeName(StringBuilder sb) {
        sb.append("optgroup");
    }

    protected void writeAttributes(StringBuilder sb) {
        super.writeAttributes(sb);
        if (label_ != null) {
            sb.append(" label=\"").append(HTMLUtils.filter(label_))
                    .append("\"");
        }
    }

    protected void writeContent(StringBuilder sb) {
        Option[] options = (Option[]) getContent();
        if (options != null && options.length > 0) {
            sb.append(">").append(LS);
            for (int i = 0; i < options.length; i++) {
                sb.append("  ").append(options[i]).append(LS);
            }
            sb.append("</");
            writeName(sb);
        } else {
            sb.append(" /");
        }
    }

    /*
     * public scope methods
     */

    public String getLabel() {
        return label_;
    }

    public Optgroup setLabel(String label) {
        label_ = label;
        return this;
    }

    public Option[] getOptions() {
        return (Option[]) getContent();
    }
}
