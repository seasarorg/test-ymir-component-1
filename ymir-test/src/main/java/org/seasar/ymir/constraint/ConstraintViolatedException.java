package org.seasar.ymir.constraint;

import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;

abstract public class ConstraintViolatedException extends Exception {
    private static final long serialVersionUID = -7148377969598636958L;

    private String path_;

    private Notes notes_;

    public ConstraintViolatedException() {
    }

    public ConstraintViolatedException(String note) {
        super(note);
    }

    public ConstraintViolatedException(Throwable cause) {
        super(cause);

    }

    public ConstraintViolatedException(String message, Throwable cause) {
        super(message, cause);

    }

    public ConstraintViolatedException(Notes notes) {
        notes_ = notes;
    }

    public String getPath() {
        return path_;
    }

    public ConstraintViolatedException setPath(String path) {
        path_ = path;
        return this;
    }

    public boolean hasNotes() {
        return (notes_ != null);
    }

    public Notes getNotes() {
        return notes_;
    }

    public ConstraintViolatedException setNotes(Notes notes) {
        notes_ = notes;
        return this;
    }

    public ConstraintViolatedException addNote(Note note) {
        if (notes_ == null) {
            notes_ = new Notes();
        }
        notes_.add(note);
        return this;
    }

    public ConstraintViolatedException addNote(String value, Object[] parameters) {
        return addNote(new Note(value, parameters));
    }

    public ConstraintViolatedException addNote(String category, Note note) {
        if (notes_ == null) {
            notes_ = new Notes();
        }
        notes_.add(category, note);
        return this;
    }

    public ConstraintViolatedException addNote(String category, String value,
            Object[] parameters) {
        return addNote(category, new Note(value, parameters));
    }
}
