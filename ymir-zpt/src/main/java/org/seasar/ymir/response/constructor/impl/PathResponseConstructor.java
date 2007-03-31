package org.seasar.ymir.response.constructor.impl;

import org.seasar.ymir.Path;
import org.seasar.ymir.Response;
import org.seasar.ymir.response.RedirectResponse;
import org.seasar.ymir.response.VoidResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;

public class PathResponseConstructor implements ResponseConstructor<Path> {

    public Class<Path> getTargetClass() {

        return Path.class;
    }

    public Response constructResponse(Object component, Path returnValue) {

        if (returnValue == null) {
            return VoidResponse.INSTANCE;
        }

        return new RedirectResponse(returnValue.asString());
    }
}
