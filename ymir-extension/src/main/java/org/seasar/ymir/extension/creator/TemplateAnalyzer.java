package org.seasar.ymir.extension.creator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;

public interface TemplateAnalyzer {
    void analyze(ServletContext servletContext, HttpServletRequest request,
            HttpServletResponse response, Request ymirRequest, String path,
            HttpMethod method, Template template, String className,
            DescPool pool, String[] ignoreVariables);
}
