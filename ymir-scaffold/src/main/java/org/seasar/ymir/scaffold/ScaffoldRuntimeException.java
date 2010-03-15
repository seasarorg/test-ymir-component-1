package org.seasar.ymir.scaffold;

import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.message.NotesHolder;

public class ScaffoldRuntimeException extends RuntimeException implements
        NotesHolder {
    private static final long serialVersionUID = 1L;

    private Notes notes = new Notes();

    public ScaffoldRuntimeException() {
    }

    public ScaffoldRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScaffoldRuntimeException(String message) {
        super(message);
    }

    public ScaffoldRuntimeException(Throwable cause) {
        super(cause);
    }

    public Notes getNotes() {
        return notes;
    }

    public ScaffoldRuntimeException addNote(Note note) {
        notes.add(note);
        return this;
    }
}
