package org.seasar.ymir.response.scheme.impl;

import org.seasar.ymir.response.RedirectResponse;
import org.seasar.ymir.response.TransitionResponse;

public class RedirectStrategy extends AbstractTransitionStrategy {
    private static final String SCHEME = "redirect";

    @Override
    public TransitionResponse newResponse() {
        return new RedirectResponse();
    }

    public String getScheme() {
        return SCHEME;
    }
}
