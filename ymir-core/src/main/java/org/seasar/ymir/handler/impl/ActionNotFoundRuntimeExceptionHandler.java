package org.seasar.ymir.handler.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.ymir.ActionNotFoundRuntimeException;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.handler.annotation.ExceptionHandler;

public class ActionNotFoundRuntimeExceptionHandler {
    private static final String HEADER_ALLOW = "Allow";

    private Request request_;

    private HttpServletResponse httpResponse_;

    @Binding(bindingType = BindingType.MUST)
    public void setRequest(Request request) {
        request_ = request;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setHttpResponse(HttpServletResponse httpResponse) {
        httpResponse_ = httpResponse;
    }

    @ExceptionHandler
    public Response handle(ActionNotFoundRuntimeException t) {
        httpResponse_.setHeader(HEADER_ALLOW, PropertyUtils
                .join(getAllowedMethods()));
        try {
            httpResponse_.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }

    String[] getAllowedMethods() {
        Ymir ymir = getYmir();
        Dispatch dispatch = request_.getCurrentDispatch();
        String path = dispatch.getPath();

        List<String> list = new ArrayList<String>();
        for (HttpMethod method : HttpMethod.values()) {
            MatchedPathMapping matched = ymir.findMatchedPathMapping(path,
                    method);
            if (matched != null
                    && matched.getAction(dispatch.getPageComponent(), request_) != null) {
                list.add(method.name());
            }
        }

        return list.toArray(new String[0]);
    }

    Ymir getYmir() {
        return YmirContext.getYmir();
    }
}
