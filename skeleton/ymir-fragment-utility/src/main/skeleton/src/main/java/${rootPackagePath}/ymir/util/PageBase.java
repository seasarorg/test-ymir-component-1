package ${rootPackageName}.ymir.util;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.response.scheme.impl.PassthroughStrategy;

abstract public class PageBase {
    public static final String PASSTHROUGH = PassthroughStrategy.SCHEME + ":";

    private Request ${fieldPrefix}ymirRequest${fieldSuffix};

    @Binding(bindingType = BindingType.MUST)
    final public void setYmirRequest(Request ymirRequest) {
        ${fieldSpecialPrefix}${fieldPrefix}ymirRequest${fieldSuffix} = ymirRequest;
    }

    final public Request getYmirRequest() {
        return ${fieldPrefix}ymirRequest${fieldSuffix};
    }

    final protected void addNote(String key) {
        addNote(new Note(key));
    }

    final protected void addNote(Note note) {
        if (note != null) {
            getNotes().add(note);
        }
    }

    final protected Notes getNotes() {
        Notes notes = (Notes) ${fieldPrefix}ymirRequest${fieldSuffix}.getAttribute(RequestProcessor.ATTR_NOTES);
        if (notes == null) {
            notes = new Notes();
            ${fieldPrefix}ymirRequest${fieldSuffix}.setAttribute(RequestProcessor.ATTR_NOTES, notes);
        }
        return notes;
    }
}
