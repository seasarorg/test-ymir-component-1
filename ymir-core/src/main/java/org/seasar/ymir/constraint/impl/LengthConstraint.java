package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintUtils;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Length;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

public class LengthConstraint extends AbstractConstraint<Length> {
    public static final String SUFFIX_DIFFERENT = ".different";

    public static final String SUFFIX_MIN = ".min";

    public static final String SUFFIX_MAX = ".max";

    @Override
    protected String getConstraintKey() {
        return "length";
    }

    public void confirm(Object component, Request request, Length annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        String[] names = getParameterNames(request, getPropertyName(element),
                annotation.property());
        if (names.length == 0) {
            return;
        }

        int max = annotation.max();
        if (max == Integer.MAX_VALUE) {
            max = annotation.value();
        }

        Notes notes = new Notes();
        for (int i = 0; i < names.length; i++) {
            confirm(request, names[i], annotation.min(), max, notes, annotation
                    .messageKey(), annotation.namePrefixOnNote());
        }
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String name, int min, int max, Notes notes,
            String messageKey, String namePrefixOnNote) {
        String[] values = request.getParameterValues(name);
        if (values == null) {
            return;
        }
        boolean same = (min == max);
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() == 0) {
                continue;
            }
            String suffix = null;
            int border = 0;
            if (values[i].length() < min) {
                if (same) {
                    suffix = SUFFIX_DIFFERENT;
                } else {
                    suffix = SUFFIX_MIN;
                }
                border = min;
            } else if (values[i].length() > max) {
                if (same) {
                    suffix = SUFFIX_DIFFERENT;
                } else {
                    suffix = SUFFIX_MAX;
                }
                border = max;
            }
            if (suffix != null) {
                notes.add(name, new Note(ConstraintUtils.getFullMessageKey(
                        getConstraintKey() + suffix, messageKey),
                        namePrefixOnNote + name, border));
            }
        }
    }
}
