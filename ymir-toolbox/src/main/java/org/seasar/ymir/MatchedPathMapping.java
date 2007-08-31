package org.seasar.ymir;

import org.seasar.kvasir.util.el.VariableResolver;

public interface MatchedPathMapping {
    PathMapping getPathMapping();

    VariableResolver getVariableResolver();

    String getPageComponentName();

    String getActionName();

    String getPathInfo();

    Object getDefaultReturnValue();

    boolean isDenied();

    boolean isDispatchingByParameter();

    Action getAction(PageComponent pageComponent, Request request);
}
