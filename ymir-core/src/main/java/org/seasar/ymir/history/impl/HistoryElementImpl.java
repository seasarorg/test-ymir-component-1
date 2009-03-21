package org.seasar.ymir.history.impl;

import java.util.Map;

import org.seasar.ymir.Path;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.history.Conversation;
import org.seasar.ymir.history.HistoryElement;

public class HistoryElementImpl implements HistoryElement {
    private static final long serialVersionUID = -4130580172801001752L;

    private Path path_;

    private Conversation conversation_;

    public Path getPath() {
        return path_;
    }

    public void setPath(Path path) {
        path_ = path;
    }

    public Conversation getConversation() {
        return conversation_;
    }

    public void setConversation(Conversation conversation) {
        conversation_ = conversation;
    }

    public Class<?> getPageClass() {
        return YmirContext.getYmir().getPageClassOfPath(path_.getTrunk());
    }
}
