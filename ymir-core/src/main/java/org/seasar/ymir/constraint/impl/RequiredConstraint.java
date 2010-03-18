package org.seasar.ymir.constraint.impl;

import static org.seasar.ymir.constraint.Globals.PREFIX_REGEX;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintUtils;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Required;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

public class RequiredConstraint extends AbstractConstraint<Required> {
    @Override
    protected String getConstraintKey() {
        return "required";
    }

    public void confirm(Object component, Request request, Required annotation,
            AnnotatedElement element) throws ConstraintViolatedException {

        String fullMessageKey = getFullMessageKey(annotation.messageKey());
        Notes notes = new Notes();
        if (annotation.matchedParameterRequired()) {
            // 正規表現で指定されているパラメータについては、マッチするパラメータが存在するかのチェックを行なう。
            String[] patterns = getNotMatchedPatterns(request, annotation
                    .value());
            for (String pattern : patterns) {
                notes.add(pattern, new Note(fullMessageKey, annotation
                        .namePrefixOnNote()
                        + pattern));
            }
        }

        String[] names = getParameterNames(request, getPropertyName(element),
                annotation.value());
        if (notes.isEmpty() && names.length == 0) {
            return;
        }

        for (String name : names) {
            if (ConstraintUtils.isEmpty(request, name, annotation.completely(),
                    annotation.allowWhitespace(), annotation
                            .allowFullWidthWhitespace())) {
                notes.add(name, new Note(fullMessageKey, annotation
                        .namePrefixOnNote()
                        + name));
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
}
