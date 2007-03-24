package org.seasar.cms.ymir.extension.constraint;

import java.util.ArrayList;
import java.util.List;

import org.seasar.cms.ymir.Constraint;
import org.seasar.cms.ymir.ConstraintViolatedException;
import org.seasar.cms.ymir.Note;
import org.seasar.cms.ymir.Request;
import org.seasar.cms.ymir.ValidationFailedException;

public class NumericConstraint implements Constraint {

    private String[] names_;

    private boolean integer_;

    private Double greaterEdge_;

    private boolean greaterIncludeEqual_;

    private Double lessEdge_;

    private boolean lessIncludeEqual_;

    public NumericConstraint(String[] names, boolean integer,
            Double greaterEdge, boolean greaterIncludeEqual, Double lessEdge,
            boolean lessIncludeEqual) {
        names_ = names;
        integer_ = integer;
        greaterEdge_ = greaterEdge;
        greaterIncludeEqual_ = greaterIncludeEqual;
        lessEdge_ = lessEdge;
        lessIncludeEqual_ = lessIncludeEqual;
    }

    public void confirm(Object component, Request request)
            throws ConstraintViolatedException {
        List<Note> noteList = new ArrayList<Note>();
        for (int i = 0; i < names_.length; i++) {
            confirm(request, names_[i], noteList);
        }
        if (noteList.size() > 0) {
            throw new ValidationFailedException().setNotes(noteList
                    .toArray(new Note[0]));
        }
    }

    void confirm(Request request, String name, List<Note> noteList) {
        String key = PREFIX_MESSAGEKEY + "numeric";
        String[] values = request.getParameterValues(name);
        if (values == null) {
            return;
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() == 0) {
                continue;
            }
            if (integer_ && values[i].indexOf('.') >= 0) {
                noteList.add(new Note(key, new Object[] { name }));
            }
            double value;
            try {
                value = Double.parseDouble(values[i]);
            } catch (NumberFormatException ex) {
                noteList.add(new Note(key, new Object[] { name }));
                continue;
            }
            if (greaterEdge_ != null) {
                if (greaterIncludeEqual_) {
                    if (value < greaterEdge_.doubleValue()) {
                        noteList.add(new Note(key + ".greaterEqual",
                                new Object[] { name, greaterEdge_ }));
                    }
                } else {
                    if (value <= greaterEdge_.doubleValue()) {
                        noteList.add(new Note(key + ".greaterThan",
                                new Object[] { name, greaterEdge_ }));
                    }
                }
            }
            if (lessEdge_ != null) {
                if (lessIncludeEqual_) {
                    if (value > lessEdge_.doubleValue()) {
                        noteList.add(new Note(key + ".lessEqual", new Object[] {
                            name, lessEdge_ }));
                    }
                } else {
                    if (value >= lessEdge_.doubleValue()) {
                        noteList.add(new Note(key + ".lessThan", new Object[] {
                            name, lessEdge_ }));
                    }
                }
            }
        }
    }
}
