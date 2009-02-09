package org.seasar.ymir.zpt;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.webapp.ServletVariableResolver;

public class SessionIdEmbeddingInterceptor implements TagRenderingInterceptor {
    private static final String ATTR_ACTION = "action";

    private static final String ATTR_HREF = "href";

    private static final String ATTR_SRC = "src";

    private static final String[] SPECIALATTRIBUTEPATTERNSTRINGS = new String[] {
        ATTR_ACTION, ATTR_HREF, ATTR_SRC };

    public String[] getSpecialAttributePatternStrings() {
        return SPECIALATTRIBUTEPATTERNSTRINGS;
    }

    public String[] getSpecialTagPatternStrings() {
        return null;
    }

    public String render(TemplateContext context, String name,
            Attribute[] attributes, String body,
            TagRenderingInterceptorChain chain) {
        List<Attribute> list = new ArrayList<Attribute>();
        for (Attribute attribute : attributes) {
            String attrName = attribute.getName();
            if (ATTR_ACTION.equals(attrName) || ATTR_HREF.equals(attrName)
                    || ATTR_SRC.equals(attrName)) {
                String attrValue = TagEvaluatorUtils.defilter(attribute
                        .getValue());
                if (shouldAddSessionId(attrValue)) {
                    attribute = new Attribute(attrName, TagEvaluatorUtils
                            .filter(addSessionId(context, attrValue)),
                            attribute.getQuote());
                }
                list.add(attribute);
            }
        }
        return chain
                .render(context, name, list.toArray(new Attribute[0]), body);
    }

    protected boolean shouldAddSessionId(String url) {
        // XXX 相対URLだけを対象とすれば十分であると思われるため以下のようにしている。
        // これでまずい場合は対象を広げることを検討する。
        return (url.equals(".") || url.startsWith("./") || url.equals("..") || url
                .startsWith("../"))
                && url.indexOf(";jsessionid=") < 0;
    }

    private String addSessionId(TemplateContext context, String url) {
        HttpServletResponse response = (HttpServletResponse) context
                .getVariableResolver().getVariable(context,
                        ServletVariableResolver.VAR_RESPONSE);
        if (response != null) {
            return response.encodeURL(url);
        } else {
            return url;
        }
    }
}
