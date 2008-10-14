package org.seasar.ymir.extension.zpt;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator;
import net.skirnir.freyja.zpt.tales.TypePrefixHandler;

public class AnalyzerNotTypePrefixHandler implements TypePrefixHandler {
    /*
     * TypePrefixHandler
     */

    public void setTalesExpressionEvaluator(TalesExpressionEvaluator evaluator) {
    }

    public Object handle(TemplateContext context, VariableResolver varResolver,
            String expr) {
        return Boolean.TRUE;
    }
}
