package org.seasar.ymir.zpt;

import org.seasar.ymir.message.impl.NoteRendererImpl;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.NoteLocalizer;
import net.skirnir.freyja.zpt.tales.PathTypePrefixHandler;
import net.skirnir.freyja.zpt.tales.UtilityPathResolver;
import net.skirnir.freyja.zpt.webapp.ServletTalesExpressionEvaluator;

public class YmirTalesExpressionEvaluator extends
        ServletTalesExpressionEvaluator {
    public static final String TYPE_I18NPAGE = "i18npage";

    public static final String TYPE_TERNARY = "ternary";

    public YmirTalesExpressionEvaluator() {
        NoteLocalizer noteLocalizer = newNoteLocalilzer();
        addPathResolver(new YmirPathResolver().setNoteLocalizer(noteLocalizer))
                .addPathResolver(
                        new LocalizationPathResolver()
                                .setNoteLocalizer(noteLocalizer));
        addTypePrefix(TYPE_I18NPAGE, new I18NPageTypePrefixHandler());
        addTypePrefix(TYPE_TERNARY, new TernaryTypePrefixHandler());
    }

    @Override
    protected PathTypePrefixHandler newPathTypePrefixHandler(char pathExpDelim) {
        return new PathTypePrefixHandler(pathExpDelim).addPathResolver(
                new YmirBeanPathResolver()).addPathResolver(
                new UtilityPathResolver());
    }

    protected YmirNoteLocalizer newNoteLocalilzer() {
        return new YmirNoteLocalizer(new NoteRendererImpl());
    }

    @Override
    public Object evaluate(TemplateContext context,
            VariableResolver varResolver, String expression) {
        context.setAttribute(YmirUtils.ATTR_TYPECONVERSION_HINT, null);

        return super.evaluate(context, varResolver, expression);
    }
}
