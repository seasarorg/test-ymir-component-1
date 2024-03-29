package org.seasar.ymir.response.constructor.impl;

import java.io.InputStream;

import org.seasar.ymir.Response;
import org.seasar.ymir.impl.AsIsInputStreamFactory;
import org.seasar.ymir.response.SelfContainedResponse;
import org.seasar.ymir.response.VoidResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;

public class InputStreamResponseConstructor implements
        ResponseConstructor<InputStream> {

    public Class<InputStream> getTargetClass() {

        return InputStream.class;
    }

    public Response constructResponse(Object page, InputStream returnValue) {

        if (returnValue == null) {
            return VoidResponse.INSTANCE;
        } else {
            return new SelfContainedResponse(new AsIsInputStreamFactory(
                    returnValue));
        }
    }
}
