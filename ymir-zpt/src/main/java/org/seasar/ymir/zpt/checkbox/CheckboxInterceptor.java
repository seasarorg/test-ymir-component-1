package org.seasar.ymir.zpt.checkbox;

import java.util.Map;

import org.seasar.ymir.YmirContext;
import org.seasar.ymir.checkbox.Globals;
import org.seasar.ymir.zpt.TagRenderingInterceptor;
import org.seasar.ymir.zpt.TagRenderingInterceptorChain;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.TemplateContext;

public class CheckboxInterceptor implements TagRenderingInterceptor {
    private static final String[] SPECIALTAGPATTERNSTRINGS = new String[] { "input" };

    private static final String ATTR_TYPE = "type";

    private static final String ATTR_NAME = "name";

    public String[] getSpecialAttributePatternStrings() {
        return null;
    }

    public String[] getSpecialTagPatternStrings() {
        return SPECIALTAGPATTERNSTRINGS;
    }

    public String render(TemplateContext context, String name,
            Attribute[] attributes, String body,
            TagRenderingInterceptorChain chain) {
        @SuppressWarnings("unchecked")
        Map<String, Attribute> attr = TagEvaluatorUtils.toMap(attributes);
        do {
            Attribute typeAttr = attr.get(ATTR_TYPE);
            if (typeAttr == null
                    || !"checkbox".equalsIgnoreCase(typeAttr.getValue())) {
                break;
            }

            Attribute nameAttr = attr.get(ATTR_NAME);
            if (nameAttr == null) {
                break;
            }

            return chain.render(context, name, attributes, body)
                    + "<input type=\"hidden\" name=\""
                    + TagEvaluatorUtils.filter(getKey()) + "\" value=\""
                    + nameAttr.getValue() + "\" />";
        } while (false);
        return chain.render(context, name, attributes, body);
    }

    String getKey() {
        return YmirContext.getYmir().getApplication().getProperty(
                Globals.APPKEY_CORE_CHECKBOX_KEY,
                Globals.DEFAULT_CORE_CHECKBOX_KEY);
    }
}
