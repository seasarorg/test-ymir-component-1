package org.seasar.ymir;

import java.util.Map;

public class DispatchWrapper implements Dispatch {
    protected Dispatch dispatch_;

    public DispatchWrapper(Dispatch dispatch) {
        dispatch_ = dispatch;
    }

    public Dispatch getDispatch() {
        return dispatch_;
    }

    public String getAbsolutePath() {
        return dispatch_.getAbsolutePath();
    }

    public Action getOriginalAction() {
        return dispatch_.getOriginalAction();
    }

    public String getOriginalActionName() {
        return dispatch_.getOriginalActionName();
    }

    public Action getAction() {
        return dispatch_.getAction();
    }

    public String getActionName() {
        return dispatch_.getActionName();
    }

    public Dispatcher getDispatcher() {
        return dispatch_.getDispatcher();
    }

    public MatchedPathMapping getMatchedPathMapping() {
        return dispatch_.getMatchedPathMapping();
    }

    public PageComponent getPageComponent() {
        return dispatch_.getPageComponent();
    }

    public String getPageComponentName() {
        return dispatch_.getPageComponentName();
    }

    @Deprecated
    public Map<String, String[]> getParameterMap() {
        return dispatch_.getParameterMap();
    }

    public Map<String, String[]> getPathParameterMap() {
        return dispatch_.getPathParameterMap();
    }

    public Map<String, String[]> getQueryParameterMap() {
        return dispatch_.getQueryParameterMap();
    }

    public String getPath() {
        return dispatch_.getPath();
    }

    public String getPathInfo() {
        return dispatch_.getPathInfo();
    }

    @Deprecated
    public String getQueryString() {
        return dispatch_.getQueryString();
    }

    public boolean isDenied() {
        return dispatch_.isDenied();
    }

    public boolean isMatched() {
        return dispatch_.isMatched();
    }
}
