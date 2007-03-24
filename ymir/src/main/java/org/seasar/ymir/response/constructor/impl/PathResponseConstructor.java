package org.seasar.cms.ymir.response.constructor.impl;

import org.seasar.cms.ymir.Path;
import org.seasar.cms.ymir.Response;
import org.seasar.cms.ymir.response.RedirectResponse;
import org.seasar.cms.ymir.response.VoidResponse;
import org.seasar.cms.ymir.response.constructor.ResponseConstructor;

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
