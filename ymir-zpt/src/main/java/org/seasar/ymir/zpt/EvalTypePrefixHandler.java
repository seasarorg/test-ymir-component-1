package org.seasar.ymir.zpt;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator;
import net.skirnir.freyja.zpt.tales.TalesUtils;
import net.skirnir.freyja.zpt.tales.TypePrefixHandler;

/**
 * String式形式の式を評価した結果をTAL式として評価するようなTypePrefixHandlerです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。</p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.7
 */
public class EvalTypePrefixHandler implements TypePrefixHandler {
    private TalesExpressionEvaluator evaluator_;

    /*
     * TypePrefixHandler
     */

    public void setTalesExpressionEvaluator(TalesExpressionEvaluator evaluator) {
        evaluator_ = evaluator;
    }

    public Object handle(TemplateContext context, VariableResolver varResolver,
            String expr) {
        return evaluator_.evaluate(context, varResolver, TalesUtils
                .resolveAsString(context, expr, varResolver, evaluator_));
    }
}
