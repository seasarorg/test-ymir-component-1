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
        String[] names = add(annotation.value(), getPropertyName(element));
        if (names.length == 0) {
            throw new IllegalArgumentException(
                    "Please specify at least one property: " + element);
        }

        Notes notes = new Notes();
        for (int i = 0; i < names.length; i++) {
            if (isEmpty(request, names[i])) {
                notes.add(names[i], new Note(PREFIX_MESSAGEKEY + "required",
                        new Object[] { names[i] }));
            }
        }
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    boolean isEmpty(Request request, String name) {
        String value = request.getParameter(name);
        if (value != null && value.length() > 0) {
            return false;
        }
        FormFile file = request.getFileParameter(name);
        if (file != null && file.getSize() > 0) {
            return false;
        }
        return true;
    }
}
