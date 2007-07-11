package org.seasar.ymir;

import org.seasar.kvasir.util.el.VariableResolver;

public interface MatchedPathMapping {
    PathMapping getPathMapping();

    VariableResolver getVariableResolver();

    String getComponentName();

    String getActionName();

    String getPathInfo();

    Object getDefaultReturnValue();

    boolean isDenied();

    boolean isDispatchingByParameter();

    MethodInvoker getActionMethodInvoker(Class pageClass, Request request);
}
