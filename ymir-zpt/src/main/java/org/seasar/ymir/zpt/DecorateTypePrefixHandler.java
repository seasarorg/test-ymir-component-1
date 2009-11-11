package org.seasar.ymir.zpt;

import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.message.Notes;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TagElement;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.ZptTemplateContext;
import net.skirnir.freyja.zpt.tales.Function;
import net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator;
import net.skirnir.freyja.zpt.tales.TypePrefixHandler;

public class DecorateTypePrefixHandler implements TypePrefixHandler {
    private static final String TOKEN_WITH = "with";

    private static final String ERROR_MESSAGE = "'[NOTE_CATEGORY] with [!]VALUE' is expected, but: ";

    private static final String PREFIX_REPLACE = "!";

    public void setTalesExpressionEvaluator(TalesExpressionEvaluator evaluator) {
    }

    public Object handle(TemplateContext context, VariableResolver varResolver,
            String expr) {
        ZptTemplateContext talContext = (ZptTemplateContext) context;

        String attrName = talContext.getTargetName();
        if (attrName == null) {
            // 少なくともtal:attributesの中ではない。
            throw new IllegalArgumentException(
                    "decorate expression should be used only in tal:attributes.");
        }
        String defaultValue = getDefaultValue((TagElement) talContext
                .getElement(), attrName);
        Notes notes = (Notes) varResolver.getVariable(context,
                RequestProcessor.ATTR_NOTES);

        if (notes != null) {
            String[] tkns = expr.trim().split("\\s+");
            if (tkns.length == 2) {
                if (TOKEN_WITH.equals(tkns[0])) {
                    // with XXXX。
                    if (!notes.isEmpty()) {
                        return decorate(defaultValue, tkns[1]);
                    }
                } else {
                    throw new IllegalArgumentException(ERROR_MESSAGE + expr);
                }
            } else if (tkns.length == 3) {
                if (TOKEN_WITH.equals(tkns[1])) {
                    // with XXXX。
                    if (notes.contains(tkns[0])) {
                        return decorate(defaultValue, tkns[2]);
                    }
                } else {
                    throw new IllegalArgumentException(ERROR_MESSAGE + expr);
                }
            } else {
                throw new IllegalArgumentException(ERROR_MESSAGE + expr);
            }
        }

        return defaultValue;
    }

    private String decorate(String defaultValue, String tkn) {
        boolean replaced = false;
        if (tkn.startsWith(PREFIX_REPLACE)) {
            replaced = true;
            tkn = tkn.substring(PREFIX_REPLACE.length());
        }
        if (defaultValue == null || replaced) {
            return tkn;
        } else {
            return Function.add(defaultValue, tkn);
        }
    }

    private String getDefaultValue(TagElement element, String attrName) {
        Attribute[] attrs = element.getAttributes();
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i].getName().equals(attrName)) {
                return TagEvaluatorUtils.defilter(attrs[i].getValue());
            }
        }
        return null;
    }
}
