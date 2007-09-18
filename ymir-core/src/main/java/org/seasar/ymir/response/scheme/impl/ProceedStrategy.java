package org.seasar.ymir.response.scheme.impl;

import org.seasar.ymir.response.ForwardResponse;
import org.seasar.ymir.response.TransitionResponse;

public class ProceedStrategy extends AbstractTransitionStrategy {
    private static final String SCHEME = "proceed";

    @Override
    public TransitionResponse newResponse() {
        ForwardResponse response = new ForwardResponse();
        response.setParameterTakenOver(false);
        response.setMethodTakenOver(false);
        return response;
    }

    public String getScheme() {
        return SCHEME;
    }
}
