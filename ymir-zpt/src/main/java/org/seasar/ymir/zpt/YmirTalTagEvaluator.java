package org.seasar.ymir.zpt;

import java.lang.annotation.Annotation;

import org.seasar.ymir.YmirContext;
import org.seasar.ymir.converter.TypeConversionManager;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.zpt.TalTagEvaluator;

public class YmirTalTagEvaluator extends TalTagEvaluator {
    @Override
    protected String renderEvaluatedValue(TemplateContext context,
            Object evaluated) {
        return getTypeConversionManager().convert(
                evaluated,
                String.class,
                (Annotation[]) context
                        .getAttribute(YmirUtils.ATTR_TYPECONVERSION_HINT));
    }

    protected TypeConversionManager getTypeConversionManager() {
        return (TypeConversionManager) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(TypeConversionManager.class);
    }
}
