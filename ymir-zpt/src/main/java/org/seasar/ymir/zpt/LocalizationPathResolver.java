package org.seasar.ymir.zpt;

import java.util.Locale;

import org.seasar.kvasir.util.collection.AttributeReader;
import org.seasar.kvasir.util.collection.I18NPropertyReader;
import org.seasar.kvasir.util.collection.PropertyReader;
import org.seasar.ymir.Globals;
import org.seasar.ymir.message.MessageNotFoundRuntimeException;
import org.seasar.ymir.message.Messages;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.webapp.ServletVariableResolver;
import net.skirnir.freyja.zpt.tales.NotePathResolver;

public class LocalizationPathResolver extends NotePathResolver {
    public boolean accept(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        return (super.accept(context, varResolver, obj, child)
                || obj instanceof Messages || obj instanceof I18NPropertyReader
                || obj instanceof PropertyReader || obj instanceof AttributeReader);
    }

    public Object resolve(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        Object resolved = super.resolve(context, varResolver, obj, child);
        if (resolved != null) {
            return resolved;
        }

        if (obj instanceof Messages) {
            Messages messages = (Messages) obj;
            String messageName;
            String value;
            if (child.startsWith("%")) {
                messageName = child.substring(1);
                value = messages.getMessage(messageName);
            } else {
                messageName = child;
                value = messages.getProperty(messageName);
            }
            if (value == null) {
                throw new MessageNotFoundRuntimeException(
                        "Message corresponding key ('" + messageName
                                + "') does not exist in default Messages ("
                                + Globals.MESSAGES + ")").setMessageKey(
                        messageName)
                        .setLocale(
                                child.startsWith("%") ? findLocale(context,
                                        varResolver) : new Locale(""));
            }
            return value;
        } else if (obj instanceof I18NPropertyReader) {
            I18NPropertyReader reader = (I18NPropertyReader) obj;
            if (child.startsWith("%")) {
                return reader.getProperty(child.substring(1), findLocale(
                        context, varResolver));
            } else {
                return reader.getProperty(child);
            }
        } else if (obj instanceof PropertyReader) {
            return ((PropertyReader) obj).getProperty(child);
        } else if (obj instanceof AttributeReader) {
            return ((AttributeReader) obj).getAttribute(child);
        }

        return null;
    }

    Locale findLocale(TemplateContext context, VariableResolver varResolver) {
        return (Locale) varResolver.getVariable(context,
                ServletVariableResolver.VAR_LOCALE);
    }
}
