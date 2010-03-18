package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Confirmed;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

public class ConfirmedConstraint extends AbstractConstraint<Confirmed> {
    @Override
    protected String getConstraintKey() {
        return "confirmed";
    }

    public void confirm(Object component, Request request,
            Confirmed annotation, AnnotatedElement element)
            throws ConstraintViolatedException {
        String[] names = getParameterNames(request, getPropertyName(element),
                annotation.value());
        if (names.length == 0) {
            return;
        }

        String fullMessageKey = getFullMessageKey(annotation.messageKey());
        Notes notes = new Notes();
        confirm(request, names, notes, fullMessageKey, annotation
                .namePrefixOnNote());
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String[] names, Notes notes,
            String fullMessageKey, String namePrefixOnNote) {
        Set<String> valueSet = new HashSet<String>();
        for (String name : names) {
            String[] values = request.getParameterValues(name);
            if (values != null) {
                valueSet.addAll(Arrays.asList(values));
            }
        }
        if (valueSet.size() > 1) {
            Object[] actualNames = new Object[names.length];
            for (int i = 0; i < names.length; i++) {
                actualNames[i] = namePrefixOnNote + names[i];
            }
            notes.add(new Note(fullMessageKey, actualNames), names);
        }
    }
}
