package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

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
        String[] names = getParameterNames(request, annotation.value(),
                getPropertyName(element));
        if (names.length == 0) {
            return;
        }

        Notes notes = new Notes();
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
