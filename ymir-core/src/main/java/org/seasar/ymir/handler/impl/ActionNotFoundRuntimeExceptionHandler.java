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
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.handler.ExceptionHandler;

public class ActionNotFoundRuntimeExceptionHandler implements
        ExceptionHandler<ActionNotFoundRuntimeException> {
    private static final String HEADER_ALLOW = "Allow";

    private static final String[] METHODS = new String[] {
        Request.METHOD_CONNECT, Request.METHOD_DELETE, Request.METHOD_GET,
        Request.METHOD_HEAD, Request.METHOD_LINK, Request.METHOD_OPTIONS,
        Request.METHOD_PATCH, Request.METHOD_POST, Request.METHOD_PUT,
        Request.METHOD_TRACE, Request.METHOD_UNLINK, };

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

    public String handle(ActionNotFoundRuntimeException t) {
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
        for (int i = 0; i < METHODS.length; i++) {
            MatchedPathMapping matched = ymir.findMatchedPathMapping(path,
                    METHODS[i]);
            if (matched != null
                    && matched.getAction(dispatch.getPageComponent(), request_) != null) {
                list.add(METHODS[i]);
            }
        }

        return list.toArray(new String[0]);
    }

    Ymir getYmir() {
        return YmirContext.getYmir();
    }
}
