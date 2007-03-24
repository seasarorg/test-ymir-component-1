package org.seasar.ymir.extension.constraint;

import java.util.ArrayList;
import java.util.List;

import org.seasar.ymir.Constraint;
import org.seasar.ymir.ConstraintViolatedException;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.Note;
import org.seasar.ymir.Request;
import org.seasar.ymir.ValidationFailedException;

public class RequiredConstraint implements Constraint {

    private String[] names_;

    public RequiredConstraint(String[] names) {
        names_ = names;
    }

    public void confirm(Object component, Request request)
            throws ConstraintViolatedException {
        List<Note> noteList = new ArrayList<Note>();
        for (int i = 0; i < names_.length; i++) {
            if (isEmpty(request, names_[i])) {
                noteList.add(new Note(PREFIX_MESSAGEKEY + "required",
                        new Object[] { names_[i] }));
            }
        }
        if (noteList.size() > 0) {
            throw new ValidationFailedException().setNotes(noteList
                    .toArray(new Note[0]));
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
