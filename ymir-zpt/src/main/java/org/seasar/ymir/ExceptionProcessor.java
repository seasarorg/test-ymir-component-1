package org.seasar.ymir;

public interface ExceptionProcessor {

    String PATH_EXCEPTION_TEMPLATE = "/WEB-INF/template/exception/";

    String SUFFIX_EXCEPTION_TEMPLATE = ".html";

    Response process(Request request, Throwable t);
}
