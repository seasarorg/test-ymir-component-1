package org.seasar.ymir.history;

import java.io.Serializable;

/**
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public interface History extends Serializable {
    void setRecordCount(int recordCount);

    boolean isEmpty();

    void pushElement(HistoryElement element);

    HistoryElement popElement();

    HistoryElement peekElement();

    HistoryElement peekElement(String path);

    HistoryElement peekElementInCurrentConversation();

    HistoryElement[] getElements();

    void setElements(HistoryElement[] elements);

    boolean equalsPathTo(String path, HistoryElement element);

    boolean equalsPageTo(Class<?> pageClass, HistoryElement element);
}
