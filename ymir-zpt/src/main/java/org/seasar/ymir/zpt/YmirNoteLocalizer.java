package org.seasar.ymir.zpt;

import java.text.MessageFormat;
import java.util.Locale;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.ymir.Globals;
import org.seasar.ymir.MessageNotFoundRuntimeException;
import org.seasar.ymir.Messages;
import org.seasar.ymir.MessagesNotFoundRuntimeException;
import org.seasar.ymir.Request;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.NoteLocalizer;

public class YmirNoteLocalizer implements NoteLocalizer {
    private static final String PROPERTYPREFIX_LABEL = "label.";

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
            Locale locale = findLocale(context, varResolver);
            String v = messages.getProperty(noteValue, locale);
            if (v != null) {
                String pageName = getPageName(context, varResolver);
                for (int i = 0; i < noteParameters.length; i++) {
                    if (noteParameters[i] instanceof String) {
                        String localizedValue = messages.getProperty(
                                PROPERTYPREFIX_LABEL + noteParameters[i] + "."
                                        + pageName, locale);
                        if (localizedValue == null) {
                            localizedValue = messages.getProperty(
                                    PROPERTYPREFIX_LABEL + noteParameters[i],
                                    locale);
                        }
                        if (localizedValue != null) {
                            noteParameters[i] = localizedValue;
                        }
                    }
                }
                noteValue = MessageFormat.format(v, noteParameters);
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
                        .setLocale(locale);
            }
        }

        return noteValue;
    }

    String getPageName(TemplateContext context, VariableResolver varResolver) {
        Request request = (Request) varResolver.getVariable(context,
                YmirVariableResolver.NAME_YMIRREQUEST);
        if (request == null) {
            return null;
        }

        String path = request.getPath();
        String name;
        int slash = path.lastIndexOf('/');
        if (slash < 0) {
            name = path;
        } else {
            name = path.substring(slash + 1);
        }
        int dot = name.lastIndexOf('.');
        if (dot >= 0) {
            name = name.substring(0, dot);
        }
        return name;
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
        return ((Request) varResolver.getVariable(context,
                YmirVariableResolver.NAME_YMIRREQUEST)).getLocale();
    }
}
