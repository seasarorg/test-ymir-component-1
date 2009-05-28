package com.example.web;

import javax.servlet.http.HttpServletRequest;

import org.seasar.ymir.Phase;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.scope.annotation.Inject;

public class PageItest3IncludedPage {
    @Invoke(Phase.PAGECOMPONENT_CREATED)
    public void initialize(@Inject
    HttpServletRequest request) {
        request.setAttribute("initialized", Boolean.TRUE);
    }
}
