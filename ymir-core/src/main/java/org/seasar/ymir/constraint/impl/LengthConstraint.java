package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Length;

public class LengthConstraint extends AbstractConstraint<Length> {
    public void confirm(Object component, Request request, Length annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        String[] names = getParameterNames(request, annotation.property(),
                getPropertyName(element));
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
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() == 0) {
                continue;
            }
            if (values[i].length() < min) {
                notes.add(name, new Note(key + ".min",
                        new Object[] { name, min }));
            } else if (values[i].length() > max) {
                notes.add(name, new Note(key + ".max",
                        new Object[] { name, max }));
            }
        }
    }
}
