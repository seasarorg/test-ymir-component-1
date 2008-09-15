package ${rootPackageName}.ymir;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.response.scheme.impl.PassthroughStrategy;

abstract public class PageBase {
    public static final String PASSTHROUGH = PassthroughStrategy.SCHEME + ":";

    private Request ymirRequest_;

    @Binding(bindingType = BindingType.MUST)
    final public void setYmirRequest(Request ymirRequest) {
        ymirRequest_ = ymirRequest;
    }

    final public Request getYmirRequest() {
        return ymirRequest_;
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
        Notes notes = (Notes) ymirRequest_.getAttribute(RequestProcessor.ATTR_NOTES);
        if (notes == null) {
            notes = new Notes();
            ymirRequest_.setAttribute(RequestProcessor.ATTR_NOTES, notes);
        }
        return notes;
    }
}
