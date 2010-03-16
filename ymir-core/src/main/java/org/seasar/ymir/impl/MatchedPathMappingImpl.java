package org.seasar.ymir.impl;

import java.util.Map;

import org.seasar.kvasir.util.el.VariableResolver;
import org.seasar.ymir.Action;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PathMapping;
import org.seasar.ymir.Request;

public class MatchedPathMappingImpl implements MatchedPathMapping {
    private PathMapping pathMapping_;

    private VariableResolver variableResolver_;

    public MatchedPathMappingImpl() {
    }

    public MatchedPathMappingImpl(PathMapping pathMapping,
            VariableResolver variableResolver) {
        setPathMapping(pathMapping);
        setVariableResolver(variableResolver);
    }

    public PathMapping getPathMapping() {
        return pathMapping_;
    }

    public void setPathMapping(PathMapping pathMapping) {
        pathMapping_ = pathMapping;
    }

    public VariableResolver getVariableResolver() {
        return variableResolver_;
    }

    public void setVariableResolver(VariableResolver variableResolver) {
        variableResolver_ = variableResolver;
    }

    public String getPageComponentName() {
        return pathMapping_.getPageComponentName(variableResolver_);
    }

    public String getPathInfo() {
        return pathMapping_.getPathInfo(variableResolver_);

    }

    public Map<String, String[]> getParameterMap() {
        return pathMapping_.getParameterMap(variableResolver_);
    }

    public Object getDefaultReturnValue() {
        return pathMapping_.getDefaultReturnValue(variableResolver_);
    }

    public boolean isDenied() {
        return pathMapping_.isDenied();
    }

    public boolean isIgnored() {
        return pathMapping_.isIgnored();
    }

    public Action getAction(PageComponent pageComponent, Request request) {
        return pathMapping_
                .getAction(pageComponent, request, variableResolver_);
    }

    public Action getPrerenderAction(PageComponent pageComponent,
            Request request) {
        return pathMapping_.getPrerenderAction(pageComponent, request,
                variableResolver_);
    }
}
