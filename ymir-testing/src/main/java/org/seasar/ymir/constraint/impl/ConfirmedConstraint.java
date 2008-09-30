package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        String[] names = getParameterNames(request, getPropertyName(element),
                annotation.value());
        if (names.length == 0) {
            return;
        }

        Notes notes = new Notes();
        confirm(request, names, notes);
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String[] names, Notes notes) {
        String key = PREFIX_MESSAGEKEY + "confirmed";
        Set<String> valueSet = new HashSet<String>();
        for (String name : names) {
            String[] values = request.getParameterValues(name);
            if (values != null) {
                valueSet.addAll(Arrays.asList(values));
            }
        }
        if (valueSet.size() > 1) {
            notes.add(new Note(key, (Object[]) names), names);
        }
    }
}
