package org.seasar.ymir;

import java.util.Map;

public class DispatchWrapper implements Dispatch {
    private Dispatch dispatch_;

    public String getAbsolutePath() {
        return dispatch_.getAbsolutePath();
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

    public Map<String, String[]> getParameterMap() {
        return dispatch_.getParameterMap();
    }

    public String getPath() {
        return dispatch_.getPath();
    }

    public String getPathInfo() {
        return dispatch_.getPathInfo();
    }

    public String getQueryString() {
        return dispatch_.getQueryString();
    }

    public boolean isDenied() {
        return dispatch_.isDenied();
    }

    public boolean isMatched() {
        return dispatch_.isMatched();
    }

    public void setAction(Action action) {
        dispatch_.setAction(action);
    }

    public void setPageComponent(PageComponent pageComponent) {
        dispatch_.setPageComponent(pageComponent);
    }

    public DispatchWrapper(Dispatch dispatch) {
        dispatch_ = dispatch;
    }

    public Dispatch getDispatch() {
        return dispatch_;
    }
}
