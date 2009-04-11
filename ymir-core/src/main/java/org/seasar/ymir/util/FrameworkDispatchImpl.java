package org.seasar.ymir.util;

import java.util.Map;

import org.seasar.ymir.Action;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FrameworkDispatch;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.impl.DispatchImpl;

class FrameworkDispatchImpl implements FrameworkDispatch {
    private Dispatch dispatch_;

    private DispatchImpl original_;

    FrameworkDispatchImpl(Dispatch dispatch) {
        dispatch_ = dispatch;
        original_ = YmirUtils.unwrapDispatch(dispatch);
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

    public void setOriginalAction(Action originalAction) {
        original_.setOriginalAction(originalAction);
    }

    public void setAction(Action action) {
        original_.setAction(action);
    }

    public void setPageComponent(PageComponent pageComponent) {
        original_.setPageComponent(pageComponent);
    }
}
