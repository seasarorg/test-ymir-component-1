package org.seasar.cms.ymir.response.constructor.impl;

import org.seasar.cms.ymir.Response;
import org.seasar.cms.ymir.response.VoidResponse;
import org.seasar.cms.ymir.response.constructor.ResponseConstructor;

public class ResponseResponseConstructor implements
        ResponseConstructor<Response> {

    public Class<Response> getTargetClass() {

        return Response.class;
    }

    public Response constructResponse(Object component, Response returnValue) {

        if (returnValue == null) {
            returnValue = VoidResponse.INSTANCE;
        }

        return returnValue;
    }
}
