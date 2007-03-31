package org.seasar.ymir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Notes {

    public static final String GLOBAL_NOTE = "org.seasar.ymir.GLOBAL_NOTE";

    private List<Note> list_ = new ArrayList<Note>();

    private Map<String, List<Note>> map_ = new LinkedHashMap<String, List<Note>>();

    private boolean accessed_ = false;

    public Notes() {
    }

    public Notes(Notes notes) {
        add(notes);
    }

    public Notes add(Notes notes) {
        if (notes == null) {
            return this;
        }

        Iterator<String> categories = notes.categories();
        while (categories.hasNext()) {
            String category = categories.next();

            Iterator<Note> itr = notes.get(category);
            while (itr.hasNext()) {
                Note note = itr.next();
                add(category, note);
            }
        }
        return this;
    }

    public Notes add(Note note) {
        return add(GLOBAL_NOTE, note);
    }

    public Notes add(String category, Note note) {
        list_.add(note);
        List<Note> noteList = map_.get(category);
        if (noteList == null) {
            noteList = new ArrayList<Note>();
            map_.put(category, noteList);
        }
        noteList.add(note);
        return this;
    }

    public Notes clear() {
        list_.clear();
        map_.clear();
        accessed_ = false;
        return this;
    }

    public Iterator<Note> get() {
        accessed_ = true;
        return Collections.unmodifiableCollection(list_).iterator();
    }

    public Note[] getNotes() {
        accessed_ = true;
        return list_.toArray(new Note[0]);
    }

    public boolean isAccessed() {
        return accessed_;
    }

    public Iterator<Note> get(String category) {
        accessed_ = true;
        List<Note> noteList = map_.get(category);
        if (noteList == null) {
            noteList = new ArrayList<Note>();
        }
        return Collections.unmodifiableCollection(noteList).iterator();
    }

    public Note[] getNotes(String category) {
        accessed_ = true;
        List<Note> noteList = map_.get(category);
        if (noteList == null) {
            return new Note[0];
        } else {
            return noteList.toArray(new Note[0]);
        }
    }

    public boolean isEmpty() {
        return list_.isEmpty();
    }

    public Iterator<String> categories() {
        return Collections.unmodifiableSet(map_.keySet()).iterator();
    }

    public int size() {
        return list_.size();
    }

    public int size(String category) {
        List<Note> noteList = map_.get(category);
        if (noteList == null) {
            return 0;
        } else {
            return noteList.size();
        }
    }

    public boolean containsValue(String value) {
        int n = list_.size();
        for (int i = 0; i < n; i++) {
            Note note = list_.get(i);
            if (note.getValue().equals(value)) {
                return true;
            }
        }

        return false;
    }
}
