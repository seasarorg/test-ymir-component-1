package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

import org.seasar.ymir.Note;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Length;

public class LengthConstraint extends AbstractConstraint<Length> {
    public void confirm(Object component, Request request, Length annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        String[] names = add(annotation.property(), getPropertyName(element));
        if (names.length == 0) {
            throw new IllegalArgumentException(
                    "Please specify at least one property: " + element);
        }

        List<Note> noteList = new ArrayList<Note>();
        for (int i = 0; i < names.length; i++) {
            confirm(request, names[i], annotation.min(), annotation.max(),
                    noteList);
        }
        if (noteList.size() > 0) {
            throw new ValidationFailedException().setNotes(noteList
                    .toArray(new Note[0]));
        }
    }

    void confirm(Request request, String name, int min, int max,
            List<Note> noteList) {
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
                noteList
                        .add(new Note(key + ".min", new Object[] { name, min }));
            } else if (values[i].length() > max) {
                noteList
                        .add(new Note(key + ".max", new Object[] { name, max }));
            }
        }
    }
}
