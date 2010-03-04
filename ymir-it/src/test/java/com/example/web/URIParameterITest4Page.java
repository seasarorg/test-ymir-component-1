package com.example.web;

import org.seasar.ymir.Response;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.response.RedirectResponse;

public class URIParameterITest4Page {
    public Response _get() {
        return new RedirectResponse(YmirContext.getYmir().getPathOfPageClass(
                URIParameterITest4Page.class));
    }
}
