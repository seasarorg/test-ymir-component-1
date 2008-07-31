package org.seasar.ymir.impl;

import java.util.Collections;
import java.util.Map;

import org.seasar.ymir.Action;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PageComponent;

public class DispatchImpl implements Dispatch {
    private String contextPath_;

    private String path_;

    private String queryString_;

    private Dispatcher dispatcher_;

    private MatchedPathMapping matched_;

    private PageComponent pageComponent_;

    private Action action_;

    public DispatchImpl() {
    }

    public DispatchImpl(String contextPath, String path, String queryString,
            Dispatcher dispatcher, MatchedPathMapping matched) {
        contextPath_ = contextPath;
        path_ = path;
        queryString_ = queryString;
        dispatcher_ = dispatcher;
        matched_ = matched;
    }

    public Dispatcher getDispatcher() {
        return dispatcher_;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        dispatcher_ = dispatcher;
    }

    public Action getAction() {
        return action_;
    }

    public String getActionName() {
        if (action_ != null) {
            return action_.getName();
        } else {
            return null;
        }
    }

    public void setAction(Action action) {
        action_ = action;
    }

    public String getPath() {
        return path_;
    }

    public void setPath(String path) {
        path_ = path;
    }

    public String getQueryString() {
        return queryString_;
    }

    public void setQueryString(String queryString) {
        queryString_ = queryString;
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

    public PageComponent getPageComponent() {
        return pageComponent_;
    }

    public void setPageComponent(PageComponent pageComponent) {
        pageComponent_ = pageComponent;
    }

    public String getPathInfo() {
        return matched_.getPathInfo();
    }

    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = matched_.getParameterMap();
        if (parameterMap != null) {
            return parameterMap;
        } else {
            return Collections.emptyMap();
        }
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
