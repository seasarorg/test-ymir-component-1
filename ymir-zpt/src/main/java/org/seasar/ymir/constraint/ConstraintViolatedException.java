package org.seasar.ymir.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.seasar.ymir.Note;

abstract public class ConstraintViolatedException extends Exception {

    private static final long serialVersionUID = -7148377969598636958L;

    private String path_;

    private List<Note> noteList_ = new ArrayList<Note>();

    public ConstraintViolatedException() {
    }

    public ConstraintViolatedException(String note) {

        super(note);
    }

    public ConstraintViolatedException(Throwable cause) {

        super(cause);

    }

    public ConstraintViolatedException(String note, Throwable cause) {

        super(note, cause);

    }

    public String getPath() {

        return path_;
    }

    public ConstraintViolatedException setPath(String path) {

        path_ = path;
        return this;
    }

    public boolean hasNote() {

        return (noteList_.size() > 0);
    }

    public Note[] getNotes() {

        return noteList_.toArray(new Note[0]);
    }

    public ConstraintViolatedException setNotes(Note[] notes) {

        noteList_.clear();
        noteList_.addAll(Arrays.asList(notes));
        return this;
    }

    public ConstraintViolatedException addNote(Note note) {

        noteList_.add(note);
        return this;
    }

    public ConstraintViolatedException addNote(String value, Object[] parameters) {

        return addNote(new Note(value, parameters));
    }
}
