package org.seasar.ymir.zpt;

import java.util.Locale;

import org.seasar.kvasir.util.collection.AttributeReader;
import org.seasar.kvasir.util.collection.I18NPropertyReader;
import org.seasar.kvasir.util.collection.PropertyReader;
import org.seasar.ymir.Globals;
import org.seasar.ymir.MessageNotFoundRuntimeException;
import org.seasar.ymir.Messages;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.NotePathResolver;

public class LocalizationPathResolver extends NotePathResolver {
    public boolean accept(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        return (super.accept(context, varResolver, obj, child)
                || obj instanceof I18NPropertyReader
                || obj instanceof PropertyReader || obj instanceof AttributeReader);
    }

    public Object resolve(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        Object resolved = super.resolve(context, varResolver, obj, child);
        if (resolved != null) {
            return resolved;
        }

        if (obj instanceof I18NPropertyReader) {
            I18NPropertyReader reader = (I18NPropertyReader) obj;
            String messageKey;
            String value;
            if (child.startsWith("%")) {
                messageKey = child.substring(1);
                value = reader.getProperty(messageKey, getNoteLocalizer()
                        .findLocale(context, varResolver));
            } else {
                messageKey = child;
                value = reader.getProperty(messageKey);
            }
            if (value == null && obj instanceof Messages) {
                throw new MessageNotFoundRuntimeException(
                        "Message corresponding key ('" + messageKey
                                + "') does not exist in default Messages ("
                                + Globals.MESSAGES + ")").setMessageKey(
                        messageKey).setLocale(
                        child.startsWith("%") ? getNoteLocalizer().findLocale(
                                context, varResolver) : new Locale(""));
            }
            return value;
        } else if (obj instanceof PropertyReader) {
            return ((PropertyReader) obj).getProperty(child);
        } else if (obj instanceof AttributeReader) {
            return ((AttributeReader) obj).getAttribute(child);
        }

        return null;
    }
}
