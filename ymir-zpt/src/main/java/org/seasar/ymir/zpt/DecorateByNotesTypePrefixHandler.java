package org.seasar.ymir.zpt;

import static org.seasar.ymir.zpt.util.DecorateUtils.decorate;
import static org.seasar.ymir.zpt.util.DecorateUtils.getDefaultValue;

import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.message.Notes;

import net.skirnir.freyja.TagElement;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.ZptTemplateContext;
import net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator;
import net.skirnir.freyja.zpt.tales.TypePrefixHandler;

public class DecorateByNotesTypePrefixHandler implements TypePrefixHandler {
    private static final String TOKEN_WITH = "with";

    private static final String ERROR_MESSAGE = "'[NOTE_CATEGORY] with [!]VALUE' is expected, but: ";

    private static final String ATTR_CLASS = "class";

    private static final String ATTRIBUTES_TAG = "tal:attributes";

    public void setTalesExpressionEvaluator(TalesExpressionEvaluator evaluator) {
    }

    public Object handle(TemplateContext context, VariableResolver varResolver,
            String expr) {
        ZptTemplateContext talContext = (ZptTemplateContext) context;

        if (!ATTR_CLASS.equals(talContext.getTargetName())
                || !ATTRIBUTES_TAG.equals(talContext
                        .getProcessingAttributeName())) {
            throw new IllegalArgumentException(
                    "This expression type should be used only in "
                            + ATTRIBUTES_TAG + " for '" + ATTR_CLASS
                            + "' attribute.");
        }
        String defaultValue = getDefaultValue((TagElement) talContext
                .getElement(), ATTR_CLASS);
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
}
