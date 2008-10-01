package org.seasar.ymir.message.impl;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;

import org.seasar.ymir.message.Messages;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.NoteRenderer;

public class NoteRendererImpl implements NoteRenderer {
    private static final String PROPERTYPREFIX_NOTE_PARAMETER = "_.note.parameter.";

    private static final String PROPERTY_NOTE_PARAMETER_SEGMENT_DIRECTION = PROPERTYPREFIX_NOTE_PARAMETER
            + "segment.direction";

    private static final String PROPERTY_NOTE_PARAMETER_HEAD = PROPERTYPREFIX_NOTE_PARAMETER
            + "head";

    private static final String PROPERTY_NOTE_PARAMETER_DELIMITER = PROPERTYPREFIX_NOTE_PARAMETER
            + "delimiter";

    private static final String PROPERTY_NOTE_PARAMETER_TAIL = PROPERTYPREFIX_NOTE_PARAMETER
            + "tail";

    private static final String PROPERTY_NOTE_PARAMETER_SEGMENT_TEMPLATE = PROPERTYPREFIX_NOTE_PARAMETER
            + "segment.template";

    private static final String PROPERTYPREFIX_LABEL = "label.";

    private static final String DIRECTION_LEFT = "l";

    private static final String DELIMITER = ".";

    private static final String DIRECTION_DEFAULT = "r";

    public String render(Note note, Messages messages) {
        return render(note.getValue(), note.getParameters(), messages);
    }

    public String render(String templateKey, Object[] parameters,
            Messages messages) {
        String v = messages.getMessage(templateKey);
        if (v == null) {
            return null;
        }

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] instanceof String) {
                parameters[i] = localize((String) parameters[i], messages);
            }
        }
        return MessageFormat.format(v, parameters);
    }

    protected String localize(String parameter, Messages messages) {
        String segmentTemplate = messages
                .getMessage(PROPERTY_NOTE_PARAMETER_SEGMENT_TEMPLATE);
        if (segmentTemplate == null) {
            // 例えばa[1].b[2].cについて、テンプレートがみつからない場合は
            // a.b.cに対応するラベルを探し、なければcに対応するラベルを探すようにする。
            String stripped = stripIndex(parameter);
            String message = messages.getMessage(PROPERTYPREFIX_LABEL
                    + stripped);
            if (message == null) {
                message = messages.getMessage(PROPERTYPREFIX_LABEL
                        + getLastSegment(stripped));
            }
            if (message == null) {
                message = parameter;
            }
            return message;
        }

        StringBuilder sb = new StringBuilder();

        String head = messages.getMessage(PROPERTY_NOTE_PARAMETER_HEAD);
        if (head != null) {
            sb.append(head);
        }

        String direction = messages
                .getMessage(PROPERTY_NOTE_PARAMETER_SEGMENT_DIRECTION);
        if (direction == null) {
            direction = DIRECTION_DEFAULT;
        }
        boolean left = direction.toLowerCase().startsWith(DIRECTION_LEFT);

        String delimiter = messages
                .getMessage(PROPERTY_NOTE_PARAMETER_DELIMITER);
        if (delimiter == null) {
            delimiter = DELIMITER;
        }

        int pre = 0;
        int idx;
        LinkedList<String> localizedSegmentList = new LinkedList<String>();
        while ((idx = parameter.indexOf(DELIMITER, pre)) >= 0) {
            String localized = localizeSegment(parameter.substring(pre, idx),
                    messages, segmentTemplate);
            if (left) {
                localizedSegmentList.addFirst(localized);
            } else {
                localizedSegmentList.addLast(localized);
            }
            pre = idx + 1;
        }
        String localized = localizeSegment(parameter.substring(pre), messages,
                segmentTemplate);
        if (left) {
            localizedSegmentList.addFirst(localized);
        } else {
            localizedSegmentList.addLast(localized);
        }

        String delim = "";
        for (Iterator<String> itr = localizedSegmentList.iterator(); itr
                .hasNext();) {
            sb.append(delim);
            delim = delimiter;
            sb.append(itr.next());
        }

        String tail = messages.getMessage(PROPERTY_NOTE_PARAMETER_TAIL);
        if (tail != null) {
            sb.append(tail);
        }

        return sb.toString();
    }

    String getLastSegment(String parameter) {
        if (parameter == null) {
            return null;
        }

        int delim = parameter.lastIndexOf(DELIMITER);
        if (delim < 0) {
            return parameter;
        } else {
            return parameter.substring(delim + 1);
        }
    }

    String stripIndex(String parameter) {
        if (parameter == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        int pre = 0;
        int idx;
        while ((idx = parameter.indexOf('[', pre)) >= 0) {
            sb.append(parameter.substring(pre, idx));
            int rparen = parameter.indexOf(']', idx + 1);
            if (rparen >= 0) {
                pre = rparen + 1;
            } else {
                pre = parameter.length();
            }
        }
        sb.append(parameter.substring(pre));

        return sb.toString();
    }

    String localizeSegment(String segment, Messages messages, String template) {
        if (segment.endsWith("]")) {
            int lparen = segment.indexOf('[');
            if (lparen >= 0) {
                String index = segment.substring(lparen + 1,
                        segment.length() - 1);
                String oneOriginIndex;
                try {
                    oneOriginIndex = String
                            .valueOf(Integer.parseInt(index) + 1);
                } catch (NumberFormatException ex) {
                    oneOriginIndex = index;
                }
                return MessageFormat.format(template, new Object[] {
                    localizeSingle(segment.substring(0, lparen), messages),
                    index, oneOriginIndex });
            }
        }

        return localizeSingle(segment, messages);
    }

    String localizeSingle(String single, Messages messages) {
        String message = messages.getMessage(PROPERTYPREFIX_LABEL + single);
        if (message != null) {
            return message;
        } else {
            return single;
        }
    }
}
