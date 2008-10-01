package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Length;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

public class LengthConstraint extends AbstractConstraint<Length> {
    public static final String SUFFIX_DIFFERENT = ".different";

    public static final String SUFFIX_MIN = ".min";

    public static final String SUFFIX_MAX = ".max";

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
            confirm(request, names[i], annotation.min(), max, notes);
        }
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String name, int min, int max, Notes notes) {
        String key = PREFIX_MESSAGEKEY + "length";
        String[] values = request.getParameterValues(name);
        if (values == null) {
            return;
        }
        boolean same = (min == max);
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() == 0) {
                continue;
            }
            String actualKey = null;
            int border = 0;
            if (values[i].length() < min) {
                if (same) {
                    actualKey = key + SUFFIX_DIFFERENT;
                } else {
                    actualKey = key + SUFFIX_MIN;
                }
                border = min;
            } else if (values[i].length() > max) {
                if (same) {
                    actualKey = key + SUFFIX_DIFFERENT;
                } else {
                    actualKey = key + SUFFIX_MAX;
                }
                border = max;
            }
            if (actualKey != null) {
                notes.add(name, new Note(actualKey,
                        new Object[] { name, border }));
            }
        }
    }
}
