package org.seasar.ymir.scaffold.maintenance.dto;

import java.util.Date;

import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.ymir.zpt.MutableTagElement;

import net.skirnir.freyja.TagEvaluatorUtils;

public class ColumnDto {
    private String html;

    private String name;

    public ColumnDto(ColumnInfo columnInfo, boolean readOnly) {
        name = columnInfo.getPropertyName();
        html = buildInputElement(columnInfo, readOnly);
    }

    public String getHtml() {
        return html;
    }

    public String getName() {
        return name;
    }

    private String buildInputElement(ColumnInfo columnInfo, boolean readOnly) {
        MutableTagElement element = MutableTagElement.newInstance();
        String name = columnInfo.getPropertyName();
        Class<?> type = columnInfo.getPropertyType();
        if (type == String.class && columnInfo.getColumnSize() >= 200) {
            element.setName("textarea");
        } else {
            element.setName("input");
            element.addAttribute("type", "text");
        }
        element.addAttribute("name", name);
        return buildInputElement(element, columnInfo);
    }

    private String buildInputElement(MutableTagElement element,
            ColumnInfo columnInfo) {
        String tagName = element.getName();
        String name = columnInfo.getPropertyName();
        Class<?> type = columnInfo.getPropertyType();
        if ("textarea".equals(tagName)) {
            element.addAttribute("tal:content", "param-self/" + name);
            if (element.getAttribute("cols") == null) {
                element.addAttribute("cols", 64);
            }
            if (element.getAttribute("rows") == null) {
                element.addAttribute("rows", 10);
            }
        } else if ("input".equals(tagName)) {
            element.addAttribute("tal:attributes", "value param-self/" + name);
            if ("text".equals(element.getDefilteredAttributeValue("type"))) {
                int size;
                if (type == String.class) {
                    size = columnInfo.getColumnSize() * 2;
                    if (element.getAttribute("maxlength") == null) {
                        element.addAttribute("maxlength", columnInfo
                                .getColumnSize());
                    }
                } else if (Date.class.isAssignableFrom(type)) {
                    size = 20;
                } else {
                    size = 32;
                }
                if (element.getAttribute("size") == null) {
                    element.addAttribute("size", size);
                }
            }
        }
        return element.toString();
    }
}
