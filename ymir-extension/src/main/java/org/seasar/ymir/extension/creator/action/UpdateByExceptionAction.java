package org.seasar.ymir.extension.creator.action;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.PathMetaData;

public interface UpdateByExceptionAction {
    Response act(Request request, PathMetaData pathMetaData, Throwable t);
}
