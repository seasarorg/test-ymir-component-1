package org.seasar.ymir.response.constructor.impl;

import org.seasar.ymir.Response;
import org.seasar.ymir.response.VoidResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;

public class ResponseResponseConstructor implements
        ResponseConstructor<Response> {

    public Class<Response> getTargetClass() {

        return Response.class;
    }

    public Response constructResponse(Object page, Response returnValue) {

        if (returnValue == null) {
            returnValue = VoidResponse.INSTANCE;
        }

        return returnValue;
    }
}
