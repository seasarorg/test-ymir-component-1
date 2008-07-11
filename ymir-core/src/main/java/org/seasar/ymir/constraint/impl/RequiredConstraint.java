package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.seasar.ymir.FormFile;
import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Required;

public class RequiredConstraint extends AbstractConstraint<Required> {
    public void confirm(Object component, Request request, Required annotation,
            AnnotatedElement element) throws ConstraintViolatedException {

        Notes notes = new Notes();
        if (annotation.matchedParameterRequired()) {
            // 正規表現で指定されているパラメータについては、マッチするパラメータが存在するかのチェックを行なう。
            String[] patterns = getNotMatchedPatterns(request, annotation
                    .value());
            for (String pattern : patterns) {
                notes.add(pattern, new Note(PREFIX_MESSAGEKEY + "required",
                        new Object[] { pattern }));
            }
        }

        String[] names = getParameterNames(request, annotation.value(),
                getPropertyName(element));
        if (notes.isEmpty() && names.length == 0) {
            return;
        }

        for (int i = 0; i < names.length; i++) {
            if (isEmpty(request, names[i], annotation.completely(), annotation
                    .allowWhitespace())) {
                notes.add(names[i], new Note(PREFIX_MESSAGEKEY + "required",
                        new Object[] { names[i] }));
            }
        }
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    String[] getNotMatchedPatterns(Request request, String[] names) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < names.length; i++) {
            if (!names[i].startsWith(PREFIX_REGEX)) {
                // 正規表現でないのでチェック不要。
                continue;
            }

            Pattern pattern = Pattern.compile(names[i].substring(PREFIX_REGEX
                    .length()));
            boolean matched = false;
            for (Iterator<String> itr = request.getParameterNames(); itr
                    .hasNext();) {
                String name = itr.next();
                if (pattern.matcher(name).matches()) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                list.add(names[i]);
            }
        }
        return list.toArray(new String[0]);
    }

    boolean isEmpty(Request request, String name, boolean completely,
            boolean allowWhitespace) {
        String[] values = request.getParameterValues(name);
        if (values != null) {
            if (completely) {
                for (int i = 0; i < values.length; i++) {
                    String v = values[i];
                    if (!allowWhitespace) {
                        v = v.trim();
                    }
                    if (v.length() == 0) {
                        return true;
                    }
                }
            } else {
                for (int i = 0; i < values.length; i++) {
                    String v = values[i];
                    if (!allowWhitespace) {
                        v = v.trim();
                    }
                    if (v.length() > 0) {
                        return false;
                    }
                }
            }
        }
        FormFile[] files = request.getFileParameterValues(name);
        if (files != null) {
            if (completely) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getSize() == 0) {
                        return true;
                    }
                }
            } else {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getSize() > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
