package org.seasar.ymir.zpt;

import java.util.Locale;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.ymir.Globals;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.message.MessageNotFoundRuntimeException;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.message.MessagesNotFoundRuntimeException;
import org.seasar.ymir.message.NoteRenderer;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.webapp.ServletVariableResolver;
import net.skirnir.freyja.zpt.tales.NoteLocalizer;

public class YmirNoteLocalizer implements NoteLocalizer {
    private NoteRenderer renderer_;

    public YmirNoteLocalizer(NoteRenderer renderer) {
        renderer_ = renderer;
    }

    public String getMessageResourceValue(TemplateContext context,
            VariableResolver varResolver, String noteValue,
            Object[] noteParameters) {
        if (noteValue == null) {
            return null;
        }

        String messagesName;
        int slash = noteValue.indexOf('/');
        if (slash >= 0) {
            messagesName = noteValue.substring(0, slash);
            noteValue = noteValue.substring(slash + 1);
        } else {
            messagesName = Globals.NAME_MESSAGES;
        }

        Messages messages;
        try {
            messages = findMessages(messagesName);
        } catch (ClassCastException ex) {
            throw new RuntimeException(
                    "Not Messages Object: messages' name may be incorrect: key="
                            + noteValue + ", messages' name=" + messagesName,
                    ex);
        } catch (ComponentNotFoundRuntimeException ex) {
            throw new MessagesNotFoundRuntimeException(
                    "Messages object not found: messages' name may be incorrect: key="
                            + noteValue + ", messages' name=" + messagesName,
                    ex).setMessagesName(messagesName).setMessageKey(noteValue);
        }

        if (messages != null) {
            String rendered = renderer_.render(noteValue, noteParameters,
                    messages);
            if (rendered != null) {
                return rendered;
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("Message corresponding key ('").append(noteValue)
                        .append("') does not exist in ");
                if (messagesName != null) {
                    sb.append("Messages ('").append(messagesName).append("')");
                } else {
                    sb.append("default Messages (").append(Globals.MESSAGES)
                            .append(")");
                }
                throw new MessageNotFoundRuntimeException(sb.toString())
                        .setMessagesName(messagesName).setMessageKey(noteValue)
                        .setLocale(findLocale(context, varResolver));
            }
        }

        return noteValue;
    }

    Messages findMessages(String messagesName) {
        Ymir ymir = YmirContext.getYmir();
        if (ymir == null) {
            return null;
        }

        return (Messages) ymir.getApplication().getS2Container().getComponent(
                messagesName);
    }

    public Locale findLocale(TemplateContext context,
            VariableResolver varResolver) {
        return (Locale) varResolver.getVariable(context,
                ServletVariableResolver.VAR_LOCALE);
    }
}
