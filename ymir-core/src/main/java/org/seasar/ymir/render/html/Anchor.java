package org.seasar.ymir.render.html;

import org.seasar.ymir.util.HTMLUtils;

/**
 * HTMLのaタグを扱うためのクラスです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public class Anchor extends Tag {
    private static final long serialVersionUID = 1L;

    private String href_;

    public Anchor() {
    }

    protected void writeName(StringBuilder sb) {
        sb.append("a");
    }

    protected void writeAttributes(StringBuilder sb) {
        super.writeAttributes(sb);
        if (href_ != null) {
            sb.append(" href=\"").append(HTMLUtils.filter(href_)).append("\"");
        }
    }

    public Anchor(String href, Object content) {
        super(content);
        href_ = href;
    }

    public String getHref() {
        return href_;
    }

    public Anchor setHref(String href) {
        href_ = href;
        return this;
    }
}
