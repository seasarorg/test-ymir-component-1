package org.seasar.ymir.mock;

import java.util.Map;

import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.Action;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;

public class MockMatchedPathMapping implements MatchedPathMapping {
    private Action action_;

    private String actionName_;

    private String componentName_;

    private Object defaultReturnValue_;

    private String pathInfo_;

    private Map<String, String[]> parameterMap_;

    private PathMapping pathMapping_;

    private VariableResolver resolver_;

    private boolean denied_;

    private boolean ignored_;

    private String renderActionName_;

    private Action renderAction_;

    private String defaultActionName_;

    public Action getAction(PageComponent pageComponent, Request request) {
        return action_;
    }

    public MockMatchedPathMapping setAction(Action action) {
        action_ = action;
        return this;
    }

    public String getActionName() {
        return actionName_;
    }

    public MockMatchedPathMapping setActionName(String actionName) {
        actionName_ = actionName;
        return this;
    }

    public String getPageComponentName() {
        return componentName_;
    }

    public MockMatchedPathMapping setComponentName(String componentName) {
        componentName_ = componentName;
        return this;
    }

    public Object getDefaultReturnValue() {
        return defaultReturnValue_;
    }

    public String evaluate(String template) {
        return null;
    }

    public MockMatchedPathMapping setDefaultReturnValue(
            Object defaultReturnValue) {
        defaultReturnValue_ = defaultReturnValue;
        return this;
    }

    public String getPathInfo() {
        return pathInfo_;
    }

    public MockMatchedPathMapping setPathInfo(String pathInfo) {
        pathInfo_ = pathInfo;
        return this;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap_;
    }

    public MockMatchedPathMapping setParameterMap(
            Map<String, String[]> parameterMap) {
        parameterMap_ = parameterMap;
        return this;
    }

    public PathMapping getPathMapping() {
        return pathMapping_;
    }

    public MockMatchedPathMapping setPathMapping(PathMapping pathMapping) {
        pathMapping_ = pathMapping;
        return this;
    }

    public VariableResolver getVariableResolver() {
        return resolver_;
    }

    public MockMatchedPathMapping setVariableResolver(VariableResolver resolver) {
        resolver_ = resolver;
        return this;
    }

    public boolean isDenied() {
        return denied_;
    }

    public MockMatchedPathMapping setDenied(boolean denied) {
        denied_ = denied;
        return this;
    }

    public boolean isIgnored() {
        return ignored_;
    }

    public MockMatchedPathMapping setIgnored(boolean ignored) {
        ignored_ = ignored;
        return this;
    }

    public String getRenderActionName() {
        return renderActionName_;
    }

    public void setRenderActionName(String renderActionName) {
        renderActionName_ = renderActionName;
    }

    public Action getPrerenderAction(PageComponent pageComponent,
            Request request) {
        return renderAction_;
    }

    public void setRenderAction(Action renderAction) {
        renderAction_ = renderAction;
    }

    public String getDefaultActionName() {
        return defaultActionName_;
    }

    public void setDefaultActionName(String defaultActionName) {
        defaultActionName_ = defaultActionName;
    }
}
