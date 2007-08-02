package org.seasar.ymir.response.constructor.impl;

import org.seasar.ymir.Response;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;

public class VoidResponseConstructor implements ResponseConstructor<Void> {

    public Class<Void> getTargetClass() {

        return Void.TYPE;
    }

    public Response constructResponse(Object page, Void returnValue) {

        return PassthroughResponse.INSTANCE;
    }
}
