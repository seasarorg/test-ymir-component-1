package org.seasar.cms.ymir.response.constructor.impl;

import org.seasar.cms.ymir.Response;
import org.seasar.cms.ymir.response.PassthroughResponse;
import org.seasar.cms.ymir.response.constructor.ResponseConstructor;

public class VoidResponseConstructor implements ResponseConstructor<Void> {

    public Class<Void> getTargetClass() {

        return Void.TYPE;
    }

    public Response constructResponse(Object component, Void returnValue) {

        return PassthroughResponse.INSTANCE;
    }
}
