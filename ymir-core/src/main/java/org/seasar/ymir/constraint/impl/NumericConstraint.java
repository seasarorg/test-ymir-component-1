package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Numeric;

public class NumericConstraint extends AbstractConstraint<Numeric> {
    public void confirm(Object component, Request request, Numeric annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        String[] names = getParameterNames(request, annotation.value(),
                annotation.property(), getPropertyName(element));
        if (names.length == 0) {
            throw new IllegalArgumentException(
                    "Please specify at least one property: " + element);
        }

        boolean integer = annotation.integer();

        boolean greaterIncludeEqual = false;
        Double greaterEdge = annotation.greaterThan();
        if (greaterEdge == Double.MIN_VALUE) {
            greaterEdge = annotation.greaterEqual();
            greaterIncludeEqual = true;
            if (greaterEdge == Double.MIN_VALUE) {
                greaterEdge = null;
            }
        }

        boolean lessIncludeEqual = false;
        Double lessEdge = annotation.lessThan();
        if (lessEdge == Double.MAX_VALUE) {
            lessEdge = annotation.lessEqual();
            lessIncludeEqual = true;
            if (lessEdge == Double.MAX_VALUE) {
                lessEdge = null;
            }
        }

        Notes notes = new Notes();
        for (int i = 0; i < names.length; i++) {
            confirm(request, names[i], integer, greaterEdge,
                    greaterIncludeEqual, lessEdge, lessIncludeEqual, notes);
        }
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String name, boolean integer,
            Double greaterEdge, boolean greaterIncludeEqual, Double lessEdge,
            boolean lessIncludeEqual, Notes notes) {
        String key = PREFIX_MESSAGEKEY + "numeric";
        String[] values = request.getParameterValues(name);
        if (values == null) {
            return;
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() == 0) {
                continue;
            }
            double value;
            try {
                value = Double.parseDouble(values[i]);
            } catch (NumberFormatException ex) {
                notes.add(name, new Note(key, new Object[] { name }));
                continue;
            }
            if (integer && values[i].indexOf('.') >= 0) {
                notes.add(name, new Note(key + ".integer",
                        new Object[] { name }));
            }
            if (greaterEdge != null) {
                if (greaterIncludeEqual) {
                    if (value < greaterEdge.doubleValue()) {
                        notes.add(name, new Note(key + ".greaterEqual",
                                new Object[] { name, greaterEdge }));
                    }
                } else {
                    if (value <= greaterEdge.doubleValue()) {
                        notes.add(name, new Note(key + ".greaterThan",
                                new Object[] { name, greaterEdge }));
                    }
                }
            }
            if (lessEdge != null) {
                if (lessIncludeEqual) {
                    if (value > lessEdge.doubleValue()) {
                        notes.add(name, new Note(key + ".lessEqual",
                                new Object[] { name, lessEdge }));
                    }
                } else {
                    if (value >= lessEdge.doubleValue()) {
                        notes.add(name, new Note(key + ".lessThan",
                                new Object[] { name, lessEdge }));
                    }
                }
            }
        }
    }
}
