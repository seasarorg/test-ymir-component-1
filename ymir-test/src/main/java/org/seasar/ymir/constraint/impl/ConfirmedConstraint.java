package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Confirmed;

public class ConfirmedConstraint extends AbstractConstraint<Confirmed> {
    public void confirm(Object component, Request request,
            Confirmed annotation, AnnotatedElement element)
            throws ConstraintViolatedException {
        String name = getPropertyName(element);
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException(
                    "Target property name must be specified");
        }

        Notes notes = new Notes();
        confirm(request, name, annotation.value(), notes);
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String name, String confirmedName, Notes notes) {
        String key = PREFIX_MESSAGEKEY + "confirmed";
        String[] values = request.getParameterValues(name);
        if (values == null) {
            return;
        }
        String confirmedValue = request.getParameter(confirmedName);
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() == 0) {
                continue;
            }
            if (!values[i].equals(confirmedValue)) {
                notes.add(name, new Note(key, new Object[] { name,
                    confirmedName }));
            }
        }
    }
}
