package org.seasar.ymir;

import java.util.Map;

import org.seasar.kvasir.util.el.VariableResolver;

public interface MatchedPathMapping {
    PathMapping getPathMapping();

    VariableResolver getVariableResolver();

    String getPageComponentName();

    String getActionName();

    String getPathInfo();

    Map<String, String[]> getParameterMap();

    Object getDefaultReturnValue();

    boolean isDenied();

    Action getAction(PageComponent pageComponent, Request request);
}
