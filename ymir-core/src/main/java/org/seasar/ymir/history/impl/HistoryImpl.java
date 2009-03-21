package org.seasar.ymir.history.impl;

import java.util.Arrays;
import java.util.LinkedList;

import org.seasar.ymir.YmirContext;
import org.seasar.ymir.conversation.ConversationManager;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.history.Conversation;
import org.seasar.ymir.history.History;
import org.seasar.ymir.history.HistoryElement;

public class HistoryImpl implements History {
    private static final long serialVersionUID = -2052864334032766177L;

    protected static final int DEFAULT_RECORDCOUNT = 10;

    private LinkedList<HistoryElement> elementList_ = new LinkedList<HistoryElement>();

    private int recordCount_;

    public HistoryImpl() {
        this(DEFAULT_RECORDCOUNT);
    }

    public HistoryImpl(int recordCount) {
        setRecordCount(recordCount);
    }

    public synchronized boolean isEmpty() {
        return elementList_.isEmpty();
    }

    public synchronized HistoryElement[] getElements() {
        return elementList_.toArray(new HistoryElement[0]);
    }

    public synchronized HistoryElement peekElement(String path) {
        for (HistoryElement element : elementList_) {
            if (path.equals(element.getPath().getTrunk())) {
                return element;
            }
        }
        return null;
    }

    public synchronized HistoryElement peekElement() {
        return elementList_.peek();
    }

    public synchronized HistoryElement popElement() {
        return elementList_.poll();
    }

    public synchronized void pushElement(HistoryElement element) {
        elementList_.addFirst(element);

        if (recordCount_ >= 0 && elementList_.size() > recordCount_) {
            elementList_.removeLast();
        }
    }

    public synchronized void setElements(HistoryElement[] elements) {
        elementList_.clear();
        elementList_.addAll(Arrays.asList(elements));
    }

    public boolean equalsPageTo(Class<?> pageClass, HistoryElement element) {
        if (element == null) {
            return false;
        } else {
            return pageClass == element.getPageClass();
        }
    }

    public boolean equalsPathTo(String path, HistoryElement element) {
        if (element == null) {
            return false;
        } else {
            return path.equals(element.getPath().getTrunk());
        }
    }

    public synchronized HistoryElement peekElementInCurrentConversation() {
        ConversationManager conversationManager = getConversationManager();
        Conversations conversations = conversationManager
                .getConversations(false);
        if (conversations == null) {
            return null;
        }

        String conversationName = conversations.getCurrentConversationName();
        if (conversationName == null) {
            return null;
        }

        for (HistoryElement element : elementList_) {
            Conversation conversation = element.getConversation();
            if (conversation == null) {
                continue;
            }
            if (conversationName.equals(conversation.getName())) {
                return element;
            }
        }
        return null;
    }

    ConversationManager getConversationManager() {
        return (ConversationManager) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(ConversationManager.class);
    }

    public void setRecordCount(int recordCount) {
        recordCount_ = recordCount;
    }
}
