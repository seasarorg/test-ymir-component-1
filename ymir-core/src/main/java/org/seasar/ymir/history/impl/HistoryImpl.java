package org.seasar.ymir.history.impl;

import java.util.Arrays;
import java.util.LinkedList;

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
        recordCount_ = recordCount;
    }

    public synchronized boolean isEmpty() {
        return elementList_.isEmpty();
    }

    public synchronized HistoryElement[] getElements() {
        return elementList_.toArray(new HistoryElement[0]);
    }

    public synchronized HistoryElement getLatestElement(String path) {
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

    public boolean equalsLatestPageTo(Class<?> pageClass) {
        HistoryElement element = peekElement();
        if (element == null) {
            return false;
        } else {
            return pageClass == element.getPageClass();
        }
    }

    public boolean equalsLatestPathTo(String path) {
        HistoryElement element = peekElement();
        if (element == null) {
            return false;
        } else {
            return path.equals(element.getPath().getTrunk());
        }
    }
}
