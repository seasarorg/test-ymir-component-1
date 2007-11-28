package org.seasar.ymir.impl;

import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.response.PassthroughResponse;

@Deprecated
public class DefaultRequestProcessor extends RequestProcessorImpl {
    @Override
    protected Response normalizeResponse(Response response, String path) {
        if (response.getType() == ResponseType.FORWARD
                && response.getPath().equals(path)) {
            return new PassthroughResponse();
        } else {
            return response;
        }
    }
}