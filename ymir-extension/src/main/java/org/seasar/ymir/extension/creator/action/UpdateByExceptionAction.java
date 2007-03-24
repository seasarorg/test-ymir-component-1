package org.seasar.ymir.extension.creator.action;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;

public interface UpdateByExceptionAction {

    Response act(Request request, Throwable t);
}
