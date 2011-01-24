package org.seasar.ymir.history.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.LifecycleListener;
import org.seasar.ymir.history.Globals;
import org.seasar.ymir.history.History;
import org.seasar.ymir.history.HistoryManager;
import org.seasar.ymir.window.WindowManager;

public class HistoryManagerImpl implements HistoryManager, LifecycleListener {
    public static final String ATTRPREFIX_HISTORY = Globals.IDPREFIX
            + "history.";

    public static final String ATTR_HISTORY = ATTRPREFIX_HISTORY + "history";

    private ApplicationManager applicationManager_;

    private WindowManager windowManager_;

    private int recordCount_ = HistoryImpl.DEFAULT_RECORDCOUNT;

    private List<Pattern> ignorePathPatterns = new ArrayList<Pattern>();

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setWindowManager(WindowManager windowManager) {
        windowManager_ = windowManager;
    }

    @Binding(bindingType = BindingType.NONE)
    public void setRecordCount(int recordCount) {
        recordCount_ = recordCount;
    }

    public void addIgnorePathPatternString(String ignorePathPatternString) {
        ignorePathPatterns.add(Pattern.compile(ignorePathPatternString));
    }

    public void setIgnorePathPatternStrings(
            List<String> ignorePathPatternStrings) {
        ignorePathPatterns.clear();
        for (String ignorePathPatternString : ignorePathPatternStrings) {
            addIgnorePathPatternString(ignorePathPatternString);
        }
    }

    public List<Pattern> getIgnorePathPatterns() {
        return ignorePathPatterns;
    }

    public void init() {
        windowManager_.addStraddlingAttributeNamePattern(Pattern
                .quote(ATTR_HISTORY));
    }

    public void destroy() {
    }

    public void startRecording() {
        synchronized (monitor()) {
            if (!isRecording()) {
                windowManager_.setScopeAttribute(ATTR_HISTORY, newHistory());
            }
        }
    }

    protected HistoryImpl newHistory() {
        return new HistoryImpl(recordCount_);
    }

    private Object monitor() {
        return windowManager_.findWindowId().intern();
    }

    public boolean isRecording() {
        synchronized (monitor()) {
            if (isAutoRecording()) {
                return windowManager_.existsWindowScope();
            } else {
                return windowManager_.getScopeAttribute(ATTR_HISTORY) != null;
            }
        }
    }

    public History getHistory() {
        synchronized (monitor()) {
            History history = windowManager_.getScopeAttribute(ATTR_HISTORY);
            if (history == null) {
                history = newHistory();
                if (isAutoRecording() && windowManager_.existsWindowScope()) {
                    windowManager_.setScopeAttribute(ATTR_HISTORY, history);
                }
            }
            return history;
        }
    }

    public void stopRecording() {
        synchronized (monitor()) {
            windowManager_.setScopeAttribute(ATTR_HISTORY, null);
        }
    }

    boolean isAutoRecording() {
        return PropertyUtils.valueOf(applicationManager_
                .findContextApplication().getProperty(
                        Globals.APPKEY_CORE_HISTORY_AUTORECORDING),
                Globals.DEFAULT_CORE_HISTORY_AUTORECORDING);
    }
}
