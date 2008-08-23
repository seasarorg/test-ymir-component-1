package org.seasar.ymir.response.scheme.impl;

import org.seasar.ymir.response.ProceedResponse;
import org.seasar.ymir.response.TransitionResponse;

public class ProceedStrategy extends AbstractTransitionStrategy {
    public static final String SCHEME = "proceed";

    @Override
    public TransitionResponse newResponse() {
        return new ProceedResponse();
    }

    public String getScheme() {
        return SCHEME;
    }
}
