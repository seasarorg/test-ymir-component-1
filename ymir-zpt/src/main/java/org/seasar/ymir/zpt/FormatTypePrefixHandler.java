package org.seasar.ymir.zpt;

import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.locale.LocaleManager;
import org.seasar.ymir.util.StringUtils;
import org.seasar.ymir.zpt.util.Parameters;

import net.skirnir.freyja.IllegalSyntaxException;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator;
import net.skirnir.freyja.zpt.tales.TypePrefixHandler;

public class FormatTypePrefixHandler implements TypePrefixHandler {
    public void setTalesExpressionEvaluator(TalesExpressionEvaluator evaluator) {
    }

    public Object handle(TemplateContext context, VariableResolver varResolver,
            String expr) {
        List<Object> parameterList;
        try {
            parameterList = new Parameters(expr)
                    .getEvaluatedParameterList(context);
        } catch (IllegalSyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }

        if (parameterList.size() < 2) {
            throw new IllegalArgumentException(
                    "The number of parameters are more than 1: " + expr);
        }

        MessageFormat mf = new MessageFormat(StringUtils.asString(parameterList
                .get(0)), findLocale(context));
        Format[] formats = mf.getFormatsByArgumentIndex();
        for (int i = 0; i < formats.length; i++) {
            if (formats[i] instanceof DateFormat) {
                ((DateFormat) formats[i]).setTimeZone(findTimeZone(context));
            }
        }
        return mf.format(parameterList.subList(1, parameterList.size())
                .toArray(new Object[0]));
    }

    private Locale findLocale(TemplateContext context) {
        return getLocaleManager(context).getLocale();
    }

    private TimeZone findTimeZone(TemplateContext context) {
        return getLocaleManager(context).getTimeZone();
    }

    private LocaleManager getLocaleManager(TemplateContext context) {
        return (LocaleManager) ((S2Container) context.getVariableResolver()
                .getVariable(context, YmirVariableResolver.NAME_CONTAINER))
                .getComponent(LocaleManager.class);
    }
}
