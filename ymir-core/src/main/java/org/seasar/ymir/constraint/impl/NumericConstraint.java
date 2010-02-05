package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintUtils;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Numeric;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

public class NumericConstraint extends AbstractConstraint<Numeric> {
    @Override
    protected String getConstraintKey() {
        return "numeric";
    }

    public void confirm(Object component, Request request, Numeric annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        String[] names = getParameterNames(request, getPropertyName(element),
                annotation.value(), annotation.property());
        if (names.length == 0) {
            return;
        }

        boolean integer = annotation.integer();

        boolean greaterIncludeEqual = false;
        Double greaterEdge = annotation.greaterThan();
        if (greaterEdge == -Double.MAX_VALUE) {
            greaterEdge = annotation.greaterEqual();
            greaterIncludeEqual = true;
            if (greaterEdge == -Double.MAX_VALUE) {
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
                    greaterIncludeEqual, lessEdge, lessIncludeEqual, notes,
                    annotation.messageKey());
        }
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String name, boolean integer,
            Double greaterEdge, boolean greaterIncludeEqual, Double lessEdge,
            boolean lessIncludeEqual, Notes notes, String messageKey) {
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
                notes
                        .add(name, new Note(ConstraintUtils.getFullMessageKey(
                                getConstraintKey(), messageKey),
                                new Object[] { name }));
                continue;
            }
            if (integer && values[i].indexOf('.') >= 0) {
                notes.add(name, new Note(ConstraintUtils.getFullMessageKey(
                        getConstraintKey() + ".integer", messageKey),
                        new Object[] { name, lessEdge, greaterEdge }));
            }
            if (greaterEdge != null) {
                if (greaterIncludeEqual) {
                    if (value < greaterEdge.doubleValue()) {
                        notes.add(name, new Note(ConstraintUtils
                                .getFullMessageKey(getConstraintKey()
                                        + ".greaterEqual", messageKey),
                                new Object[] { name, greaterEdge, lessEdge }));
                    }
                } else {
                    if (value <= greaterEdge.doubleValue()) {
                        notes.add(name, new Note(ConstraintUtils
                                .getFullMessageKey(getConstraintKey()
                                        + ".greaterThan", messageKey),
                                new Object[] { name, greaterEdge, lessEdge }));
                    }
                }
            }
            if (lessEdge != null) {
                if (lessIncludeEqual) {
                    if (value > lessEdge.doubleValue()) {
                        notes.add(name, new Note(ConstraintUtils
                                .getFullMessageKey(getConstraintKey()
                                        + ".lessEqual", messageKey),
                                new Object[] { name, lessEdge, greaterEdge }));
                    }
                } else {
                    if (value >= lessEdge.doubleValue()) {
                        notes.add(name, new Note(ConstraintUtils
                                .getFullMessageKey(getConstraintKey()
                                        + ".lessThan", messageKey),
                                new Object[] { name, lessEdge, greaterEdge }));
                    }
                }
            }
        }
    }
}
