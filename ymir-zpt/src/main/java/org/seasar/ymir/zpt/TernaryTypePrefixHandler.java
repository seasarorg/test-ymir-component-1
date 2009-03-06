package org.seasar.ymir.zpt;

import net.skirnir.freyja.StringUtils;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator;
import net.skirnir.freyja.zpt.tales.TypePrefixHandler;

/**
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。</p>
 *
 * @author YOKOTA Takehiko
 */
public class TernaryTypePrefixHandler implements TypePrefixHandler {
    // Path式のindirection指定である「?」を三項演算子の「?」と誤認識しないようにこうしている。
    private static final String OP_QUESTION = " ?";

    // typeと式のデリミタである「:」を三項演算子の「:」と誤認識しないようにこうしている。
    private static final String OP_COLON = " :";

    private TalesExpressionEvaluator evaluator_;

    public TernaryTypePrefixHandler() {
    }

    public void setTalesExpressionEvaluator(TalesExpressionEvaluator evaluator) {
        evaluator_ = evaluator;
    }

    public Object handle(TemplateContext context, VariableResolver varResolver,
            String expr) {
        expr = StringUtils.trimLeft(expr);
        int question = expr.indexOf(OP_QUESTION);
        if (question < 0) {
            throw new IllegalArgumentException(
                    "Illegal syntax. Must be \"condition ? expr1 : expr2\": "
                            + expr);
        }
        int colon = expr.indexOf(OP_COLON, question + 1);
        if (colon < 0) {
            throw new IllegalArgumentException(
                    "Illegal syntax. Must be \"condition ? expr1 : expr2\": "
                            + expr);
        }

        if (evaluator_.isTrue(evaluator_.evaluate(context, varResolver, expr
                .substring(0, question).trim()))) {
            return evaluator_.evaluate(context, varResolver, expr.substring(
                    question + OP_QUESTION.length(), colon).trim());
        } else {
            return evaluator_.evaluate(context, varResolver, StringUtils
                    .trimLeft(expr.substring(colon + OP_COLON.length())));
        }
    }
}
