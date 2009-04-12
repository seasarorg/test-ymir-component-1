package org.seasar.ymir.render.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.seasar.ymir.util.HTMLUtils;

/**
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 *
 * @author YOKOTA Takehiko
 */
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String LS = System.getProperty("line.separator");

    private Object content_;

    private String id_;

    private String styleClass_;

    private String style_;

    /*
     * public scope methods
     */

    public Tag() {
    }

    public Tag(Object content) {
        setContent(content);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        writeName(sb);
        writeAttributes(sb);
        writeContent(sb);
        sb.append(">");
        return sb.toString();
    }

    protected void writeName(StringBuilder sb) {
    }

    protected void writeAttributes(StringBuilder sb) {
        if (id_ != null) {
            sb.append(" id=\"").append(HTMLUtils.filter(id_)).append("\"");
        }
        if (styleClass_ != null) {
            sb.append(" class=\"").append(HTMLUtils.filter(styleClass_))
                    .append("\"");
        }
        if (style_ != null) {
            sb.append(" style=\"").append(HTMLUtils.filter(style_))
                    .append("\"");
        }
    }

    protected void writeContent(StringBuilder sb) {
        if (content_ != null) {
            sb.append(">");
            if (content_ instanceof Tag) {
                sb.append(LS).append("  ").append(content_).append(LS);
            } else if (content_ instanceof Tag[]) {
                Tag[] tags = (Tag[]) content_;
                if (tags.length > 0) {
                    for (int i = 0; i < tags.length; i++) {
                        sb.append(LS).append("  ").append(tags[i]);
                    }
                    sb.append(LS);
                }
            } else {
                sb.append(content_);
            }
            sb.append("</");
            writeName(sb);
        } else {
            sb.append(" /");
        }
    }

    final protected String addIndent(Object obj) {
        if (obj == null) {
            return null;
        }
        String text = obj.toString();
        if (text.length() == 0) {
            return text;
        }

        BufferedReader br = new BufferedReader(new StringReader(text));
        StringBuilder sb = new StringBuilder();
        String line;
        String delim = "";
        String idt = "";
        try {
            while ((line = br.readLine()) != null) {
                sb.append(delim).append(idt).append(line);
                delim = LS;
                idt = "  ";
            }
        } catch (IOException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }

        char lastChar = text.charAt(text.length() - 1);
        if (lastChar == '\n' || lastChar == '\r') {
            sb.append(LS);
        }

        return sb.toString();
    }

    public Object getContent() {
        return content_;
    }

    public Tag setContent(Object content) {
        content_ = content;
        return this;
    }

    public String getContentAsString() {
        if (content_ != null) {
            return content_.toString();
        } else {
            return null;
        }
    }

    public String getId() {
        return id_;
    }

    public Tag setId(String id) {
        id_ = id;
        return this;
    }

    public String getStyleClass() {
        return styleClass_;
    }

    public Tag setStyleClass(String styleClass) {
        styleClass_ = styleClass;
        return this;
    }

    public String getStyle() {
        return style_;
    }

    public Tag setStyle(String style) {
        style_ = style;
        return this;
    }
}
