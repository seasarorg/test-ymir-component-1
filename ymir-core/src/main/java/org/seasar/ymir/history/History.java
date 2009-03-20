package org.seasar.ymir.history;

import java.io.Serializable;

/**
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public interface History extends Serializable {
    boolean isEmpty();

    void pushElement(HistoryElement element);

    HistoryElement popElement();

    HistoryElement peekElement();

    HistoryElement getLatestElement(String path);

    HistoryElement[] getElements();

    void setElements(HistoryElement[] elements);

    boolean equalsLatestPathTo(String path);

    boolean equalsLatestPageTo(Class<?> pageClass);
}
