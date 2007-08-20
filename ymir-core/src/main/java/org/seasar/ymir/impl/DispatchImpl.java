package org.seasar.ymir.impl;

import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.MatchedPathMapping;

public class DispatchImpl implements Dispatch {
    private String contextPath_;

    private String path_;

    private Dispatcher dispatcher_;

    private MatchedPathMapping matched_;

    public DispatchImpl() {
    }

    public DispatchImpl(String contextPath, String path, Dispatcher dispatcher,
            MatchedPathMapping matched) {
        contextPath_ = contextPath;
        path_ = path;
        dispatcher_ = dispatcher;
        matched_ = matched;
    }

    public Dispatcher getDispatcher() {
        return dispatcher_;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        dispatcher_ = dispatcher;
    }

    public String getPath() {
        return path_;
    }

    public void setPath(String path) {
        path_ = path;
    }

    public String getAbsolutePath() {
        if ("".equals(path_)) {
            return contextPath_ + "/";
        } else {
            return contextPath_ + path_;
        }
    }

    public String getPageComponentName() {
        return matched_.getPageComponentName();
    }

    public String getPathInfo() {
        return matched_.getPathInfo();
    }

    public MatchedPathMapping getMatchedPathMapping() {
        return matched_;
    }

    public boolean isMatched() {
        return (matched_ != null);
    }

    public boolean isDenied() {
        return (matched_ != null && matched_.isDenied());
    }
}
