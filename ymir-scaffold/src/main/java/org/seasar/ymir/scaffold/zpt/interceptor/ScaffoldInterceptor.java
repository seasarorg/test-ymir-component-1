package org.seasar.ymir.scaffold.zpt.interceptor;

import org.seasar.ymir.scaffold.Globals;
import org.seasar.ymir.zpt.MutableTagElement;
import org.seasar.ymir.zpt.TalAttributes;
import org.seasar.ymir.zpt.TemplateParsingInterceptor;
import org.seasar.ymir.zpt.TemplateParsingInterceptorChain;
import org.seasar.ymir.zpt.TalAttributes.Statement;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.Element;
import net.skirnir.freyja.IllegalSyntaxException;
import net.skirnir.freyja.TagElement;
import net.skirnir.freyja.TagEvaluatorUtils;

public class ScaffoldInterceptor implements TemplateParsingInterceptor, Globals {
    private static final String YS_FORM = PREFIX_YS + "form";

    private static final String YS_FIELD = PREFIX_YS + "field";

    private static final String[] SPECIALATTRIBUTEPATTERNSTRINGS = new String[] { "^"
            + PREFIX_YS };

    private static final String[] SPECIALTAGPATTERNSTRINGS = new String[0];

    private static final String ATTR_TAL_ATTRIBUTES = "tal:attributes";

    private static final String ATTR_ENCTYPE = "enctype";

    private static final String ATTR_CLASS = "class";

    private static final String ATTR_TAL_CONTENT = "tal:content";

    private static final String ATTR_TAL_REPLACE = "tal:replace";

    public String[] getSpecialAttributePatternStrings() {
        return SPECIALATTRIBUTEPATTERNSTRINGS;
    }

    public String[] getSpecialTagPatternStrings() {
        return SPECIALTAGPATTERNSTRINGS;
    }

    public Element[] tagElementCreated(TagElement tagElement,
            TemplateParsingInterceptorChain chain)
            throws IllegalSyntaxException {
        MutableTagElement element = MutableTagElement.toMutable(tagElement);
        element.removeAttributes(SPECIALATTRIBUTEPATTERNSTRINGS, YS_FORM,
                YS_FIELD);
        if (element.getOriginalAttribute(YS_FORM) != null) {
            // ys:form。
            manipulateForForm(element);
            tagElement = element;
        } else if (element.getOriginalAttribute(YS_FIELD) != null) {
            // ys:field。
            manipulateForField(element);
            tagElement = element;
        }
        return chain.tagElementCreated(tagElement);
    }

    private void manipulateForForm(MutableTagElement element)
            throws IllegalSyntaxException {
        TalAttributes talAttributes = TalAttributes.valueOf(element
                .getAttribute(ATTR_TAL_ATTRIBUTES));

        if (element.getAttribute("action") == null
                && (talAttributes == null || talAttributes
                        .getStatement("action") == null)) {
            if (talAttributes == null) {
                talAttributes = TalAttributes.newInstance();
            }
            talAttributes.addStatement("action", "page:${ymirRequest/path}");
        }

        if (element.getAttribute(ATTR_ENCTYPE) == null) {
            for (Element elem : element.getBodyElements()) {
                if (isFileInputTag(elem)) {
                    element.addAttribute(new Attribute(ATTR_ENCTYPE,
                            "multipart/form-data"));
                    break;
                }
            }
        }

        element.addAttribute(new Attribute(ATTR_TAL_ATTRIBUTES, talAttributes
                .toFilteredString()));
    }

    private boolean isFileInputTag(Element element) {
        if (!(element instanceof TagElement)) {
            return false;
        }

        TagElement tag = (TagElement) element;
        if (!"input".equals(tag.getName())) {
            return false;
        }

        for (Attribute attr : tag.getAttributes()) {
            if ("type".equals(attr.getName()) && "file".equals(attr.getValue())) {
                return true;
            }
        }

        return false;
    }

    private void manipulateForField(MutableTagElement element)
            throws IllegalSyntaxException {
        TalAttributes talAttributes = TalAttributes.valueOf(element
                .getAttribute(ATTR_TAL_ATTRIBUTES));
        if (talAttributes == null) {
            talAttributes = TalAttributes.newInstance();
        }

        String name;
        boolean shouldEval;
        Statement nameStatement = talAttributes.getStatement("name");
        if (nameStatement != null) {
            name = "${" + nameStatement.getExpression() + "}";
            shouldEval = true;
        } else {
            name = element.getDefilteredAttributeValue("name");
            shouldEval = false;
        }
        String prefix = shouldEval ? "eval:" : "";

        String tagName = element.getName();
        if ("input".equals(tagName)) {
            // input。
            if (!"file".equals(element.getAttribute("type"))
                    && !"password".equals(element.getAttribute("type"))) {
                talAttributes.addStatement("value", prefix + "param-self/"
                        + name);
            }
        } else if ("textarea".equals(tagName)) {
            if (element.getAttribute(ATTR_TAL_CONTENT) == null
                    && element.getAttribute(ATTR_TAL_REPLACE) == null) {
                element.addAttribute(ATTR_TAL_CONTENT, TagEvaluatorUtils
                        .filter((shouldEval ? "eval:" : "") + "param-self/"
                                + name));
            }
        }

        if (talAttributes.getStatement(ATTR_CLASS) == null) {
            talAttributes.addStatement(ATTR_CLASS, "decorate-by-notes:"
                    + prefix + name + " with " + CSS_CLASS_ERROR);
        }
    }
}
