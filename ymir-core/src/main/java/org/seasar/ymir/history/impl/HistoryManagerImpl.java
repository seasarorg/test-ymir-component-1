package org.seasar.ymir.history.impl;

import java.util.regex.Pattern;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.Globals;
import org.seasar.ymir.LifecycleListener;
import org.seasar.ymir.history.History;
import org.seasar.ymir.history.HistoryManager;
import org.seasar.ymir.window.WindowManager;

public class HistoryManagerImpl implements HistoryManager, LifecycleListener {
    public static final String ATTRPREFIX_HISTORY = Globals.IDPREFIX
            + "history.";

    public static final String ATTR_HISTORY = ATTRPREFIX_HISTORY + "history";

    private WindowManager windowManager_;

    private int recordCount_ = HistoryImpl.DEFAULT_RECORDCOUNT;

    @Binding(bindingType = BindingType.MUST)
    public void setWindowManager(WindowManager windowManager) {
        windowManager_ = windowManager;
    }

    @Binding(bindingType = BindingType.NONE)
    public void setRecordCount(int recordCount) {
        recordCount_ = recordCount;
    }

    public void init() {
        windowManager_.addStraddlingAttributeNamePattern(Pattern
                .quote(ATTR_HISTORY));
    }

    public void destroy() {
    }

    public synchronized void startRecording() {
        if (windowManager_.getScopeAttribute(ATTR_HISTORY) == null) {
            windowManager_.setScopeAttribute(ATTR_HISTORY, new HistoryImpl(
                    recordCount_));
        }
    }

    public boolean isRecording() {
        return windowManager_.getScopeAttribute(ATTR_HISTORY) != null;
    }

    public synchronized History getHistory() {
        History history = windowManager_.getScopeAttribute(ATTR_HISTORY);
        if (history == null) {
            // なければ空のHistoryオブジェクトを返す。
            history = new HistoryImpl(recordCount_);
        }
        return history;
    }

    public synchronized void stopRecording() {
        windowManager_.setScopeAttribute(ATTR_HISTORY, null);
    }
}
