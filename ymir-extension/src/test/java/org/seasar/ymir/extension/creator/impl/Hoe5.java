package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.Response;
import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.response.PassthroughResponse;

public class Hoe5 {
    @Meta(name = "source", value = "return new PassthroughResponse();", classValue = PassthroughResponse.class)
    public Response _get() {
        return new PassthroughResponse();
    }
}
