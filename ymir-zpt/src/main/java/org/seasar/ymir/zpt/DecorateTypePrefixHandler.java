package org.seasar.ymir.zpt;

import static org.seasar.ymir.zpt.util.DecorateUtils.decorate;
import static org.seasar.ymir.zpt.util.DecorateUtils.getDefaultValue;

import net.skirnir.freyja.TagElement;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.ZptTemplateContext;
import net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator;
import net.skirnir.freyja.zpt.tales.TypePrefixHandler;

public class DecorateTypePrefixHandler implements TypePrefixHandler {
    private static final String TOKEN_WITH = "with";

    private static final String ERROR_MESSAGE = "'EXPRESSION with [!]VALUE' is expected, but: ";

    private static final String ATTR_CLASS = "class";

    private static final String ATTRIBUTES_TAG = "tal:attributes";

    private TalesExpressionEvaluator evaluator_;

    public void setTalesExpressionEvaluator(TalesExpressionEvaluator evaluator) {
        evaluator_ = evaluator;
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
                            + "' attribute: " + expr);
        }
        String defaultValue = getDefaultValue((TagElement) talContext
                .getElement(), ATTR_CLASS);

        expr = expr.trim();
        String value = null;
        String with = null;
        boolean skipWhitespace = false;
        int whitespaceStartIndex = expr.length();
        for (int i = expr.length() - 1; i >= 0; i--) {
            if (expr.charAt(i) == ' ') {
                if (!skipWhitespace) {
                    skipWhitespace = true;
                    String tkn = expr.substring(i + 1, whitespaceStartIndex);
                    if (value == null) {
                        value = tkn;
                    } else if (with == null) {
                        with = tkn;
                    }
                }
                whitespaceStartIndex = i;
            } else {
                if (skipWhitespace) {
                    skipWhitespace = false;
                } else {
                    if (with != null) {
                        break;
                    }
                }
            }
        }
        if (!TOKEN_WITH.equals(with)) {
            throw new IllegalArgumentException(ERROR_MESSAGE + expr);
        }
        String condition = expr.substring(0, whitespaceStartIndex);

        if (evaluator_.evaluateCondition(context, varResolver, condition)) {
            return decorate(defaultValue, value);
        } else {
            return defaultValue;
        }
    }
}
