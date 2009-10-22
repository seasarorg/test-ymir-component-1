package org.seasar.ymir.mock;

import java.util.Map;

import org.seasar.ymir.Action;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PageComponent;

public class MockDispatch implements Dispatch {
    private String path_;

    private String queryString_;

    private Map<String, String[]> queryParameterMap_;

    private Dispatcher dispatcher_;

    private MatchedPathMapping matched_;

    private String absolutePath_;

    private String pathInfo_;

    private Map<String, String[]> parameterMap_;

    private String pageComponentName_;

    private PageComponent pageComponent_;

    private Action originalAction_;

    private Action action_;

    public MockDispatch() {
    }

    public Dispatcher getDispatcher() {
        return dispatcher_;
    }

    public MockDispatch setDispatcher(Dispatcher dispatcher) {
        dispatcher_ = dispatcher;
        return this;
    }

    public String getPath() {
        return path_;
    }

    public MockDispatch setPath(String path) {
        path_ = path;
        return this;
    }

    public String getQueryString() {
        return queryString_;
    }

    public MockDispatch setQueryString(String queryString) {
        queryString_ = queryString;
        return this;
    }

    public String getAbsolutePath() {
        return absolutePath_;
    }

    public MockDispatch setAbsolutePath(String absolutePath) {
        absolutePath_ = absolutePath;
        return this;
    }

    public String getPageComponentName() {
        if (pageComponentName_ != null) {
            return pageComponentName_;
        } else {
            return matched_.getPageComponentName();
        }
    }

    public MockDispatch setPageComponentName(String pageComponentName) {
        pageComponentName_ = pageComponentName;
        return this;
    }

    public String getPathInfo() {
        if (pathInfo_ != null) {
            return pathInfo_;
        } else {
            return matched_.getPathInfo();
        }
    }

    public MockDispatch setPathInfo(String pathInfo) {
        pathInfo_ = pathInfo;
        return this;
    }

    public Map<String, String[]> getParameterMap() {
        return getPathParameterMap();
    }

    public Map<String, String[]> getPathParameterMap() {
        if (parameterMap_ != null) {
            return parameterMap_;
        } else {
            return matched_.getParameterMap();
        }
    }

    public MockDispatch setParameterMap(Map<String, String[]> parameterMap) {
        parameterMap_ = parameterMap;
        return this;
    }

    public Map<String, String[]> getQueryParameterMap() {
        return queryParameterMap_;
    }

    public void setQueryParameterMap(Map<String, String[]> queryParameterMap) {
        queryParameterMap_ = queryParameterMap;
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

    public boolean isIgnored() {
        return (matched_ != null && matched_.isIgnored());
    }

    public Action getOriginalAction() {
        return originalAction_;
    }

    public String getOriginalActionName() {
        if (originalAction_ != null) {
            return originalAction_.getName();
        } else {
            return null;
        }
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

    public void setOriginalAction(Action originalAction) {
        originalAction_ = originalAction;
    }

    public void setAction(Action action) {
        action_ = action;
    }

    public PageComponent getPageComponent() {
        return pageComponent_;
    }

    public void setPageComponent(PageComponent pageComponent) {
        pageComponent_ = pageComponent;
    }
}
