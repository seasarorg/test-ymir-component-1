package org.seasar.ymir.zpt;

import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.NoteLocalizer;
import net.skirnir.freyja.zpt.tales.PathResolver;

public class YmirPathResolver implements PathResolver {
    public static final String NAME_SIZE = "size";

    public static final String NAMEPREFIX_SIZE = "size(";

    public static final String NAMESUFFIX_SIZE = ")";

    public static final String NAMEPREFIX_NOTES = "notes(";

    public static final String NAMESUFFIX_NOTES = ")";

    public static final String NAME_CATEGORIES = "categories";

    public static final String NAME_VALUE = "%value";

    public static final String NAMEPREFIX_CONTAINS = "contains(";

    public static final String NAMESUFFIX_CONTAINS = ")";

    private NoteLocalizer noteLocalizer_;

    public NoteLocalizer getNoteLocalizer() {
        return noteLocalizer_;
    }

    public YmirPathResolver setNoteLocalizer(NoteLocalizer noteLocalizer) {
        noteLocalizer_ = noteLocalizer;
        return this;
    }

    public boolean accept(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        return (obj instanceof Notes || obj instanceof Note
                || obj instanceof Variables || obj instanceof ParamSelf);
    }

    public Object resolve(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        if (obj instanceof Notes) {
            Notes notes = (Notes) obj;
            if (child.equals(NAME_SIZE)) {
                return notes.size();
            } else if (child.startsWith(NAMEPREFIX_SIZE)
                    && child.endsWith(NAMESUFFIX_SIZE)) {
                return notes.size(child.substring(NAMEPREFIX_SIZE.length(),
                        child.length() - NAMESUFFIX_SIZE.length()));
            } else if (child.startsWith(NAMEPREFIX_NOTES)
                    && child.endsWith(NAMESUFFIX_NOTES)) {
                return notes.getNotes(child.substring(
                        NAMEPREFIX_NOTES.length(), child.length()
                                - NAMESUFFIX_NOTES.length()));
            } else if (child.equals(NAME_CATEGORIES)) {
                return notes.categories();
            } else if (child.startsWith(NAMEPREFIX_CONTAINS)
                    && child.endsWith(NAMESUFFIX_CONTAINS)) {
                return notes.contains(child.substring(NAMEPREFIX_CONTAINS
                        .length(), child.length()
                        - NAMESUFFIX_CONTAINS.length()));
            } else if (notes.size(child) > 0) {
                return notes.get(child);
            }
        } else if (obj instanceof Note) {
            Note note = (Note) obj;
            if (child.equals(NAME_VALUE) && noteLocalizer_ != null) {
                return noteLocalizer_.getMessageResourceValue(context,
                        varResolver, note.getValue(), note.getParameters());
            }
        } else if (obj instanceof Variables) {
            return varResolver.getVariable(context, child);
        } else if (obj instanceof ParamSelf) {
            return ((ParamSelf) obj).get(context, child);
        }
        return null;
    }
}
