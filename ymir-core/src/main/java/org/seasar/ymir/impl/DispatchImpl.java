package org.seasar.ymir.impl;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

import org.seasar.ymir.Action;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FrameworkDispatch;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.util.ServletUtils;

public class DispatchImpl implements FrameworkDispatch {
    private String contextPath_;

    private String path_;

    private Map<String, String[]> queryParameterMap_;

    private String queryString_;

    private Dispatcher dispatcher_;

    private MatchedPathMapping matched_;

    private PageComponent pageComponent_;

    private Action originalAction_;

    private Action action_;

    public DispatchImpl() {
    }

    public DispatchImpl(String contextPath, String path,
            Map<String, String[]> queryParameterMap,
            String queryParameterEncoding, Dispatcher dispatcher,
            MatchedPathMapping matched) {
        contextPath_ = contextPath;
        path_ = path;
        queryParameterMap_ = queryParameterMap;
        try {
            String queryString = ServletUtils.constructURI("",
                    queryParameterMap, queryParameterEncoding);
            queryString_ = "".equals(queryString) ? null : queryString;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Can't happen!", ex);
        }
        dispatcher_ = dispatcher;
        matched_ = matched;
    }

    public Dispatcher getDispatcher() {
        return dispatcher_;
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

    public String getPath() {
        return path_;
    }

    public String getQueryString() {
        return queryString_;
    }

    public String getAbsolutePath() {
        if ("".equals(path_)) {
            return contextPath_ + "/";
        } else {
            return contextPath_ + path_;
        }
    }

    public String getPageComponentName() {
        if (matched_ != null) {
            return matched_.getPageComponentName();
        } else {
            return null;
        }
    }

    public PageComponent getPageComponent() {
        return pageComponent_;
    }

    public void setPageComponent(PageComponent pageComponent) {
        pageComponent_ = pageComponent;
    }

    public String getPathInfo() {
        if (matched_ != null) {
            return matched_.getPathInfo();
        } else {
            return null;
        }
    }

    public Map<String, String[]> getParameterMap() {
        return getPathParameterMap();
    }

    public Map<String, String[]> getPathParameterMap() {
        if (matched_ != null) {
            Map<String, String[]> parameterMap = matched_.getParameterMap();
            if (parameterMap != null) {
                return parameterMap;
            }
        }
        return Collections.emptyMap();
    }

    public Map<String, String[]> getQueryParameterMap() {
        return queryParameterMap_;
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
