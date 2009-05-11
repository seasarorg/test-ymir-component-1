package org.seasar.ymir.extension.zpt;

import java.text.MessageFormat;
import java.util.Locale;

import org.seasar.kvasir.util.collection.I18NProperties;
import org.seasar.kvasir.util.collection.I18NPropertyHandler;
import org.seasar.kvasir.util.io.impl.JavaResource;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.NoteLocalizer;

public class NoteLocalizerImpl implements NoteLocalizer {
    private I18NPropertyHandler prop_ = new I18NProperties(new JavaResource(
            "org/seasar/ymir/extension"), "messages", ".xproperties");

    public Locale findLocale(TemplateContext context,
            VariableResolver varResolver) {
        return Locale.getDefault();
    }

    public String getMessageResourceValue(TemplateContext context,
            VariableResolver varResolver, String noteValue,
            Object[] noteParameters) {
        String format = prop_.getProperty(noteValue, findLocale(context,
                varResolver));
        if (format != null) {
            return MessageFormat.format(format, noteParameters);
        } else {
            return null;
        }
    }
}
