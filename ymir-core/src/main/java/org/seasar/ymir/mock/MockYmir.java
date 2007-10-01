package org.seasar.ymir.mock;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.ymir.Application;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.PageNotFoundException;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.interceptor.YmirProcessInterceptor;

public class MockYmir implements Ymir {
    private Application application_;

    private String projectStatus_ = Configuration.PROJECTSTATUS_RELEASE;

    public Object backupForInclusion(AttributeContainer attributeContainer) {
        return null;
    }

    public void destroy() {
    }

    public Application getApplication() {
        return application_;
    }

    public MockYmir setApplication(Application application) {
        application_ = application;
        return this;
    }

    public String getProjectStatus() {
        return projectStatus_;
    }

    public MockYmir setProjectStatus(String projectStatus) {
        projectStatus_ = projectStatus;
        return this;
    }

    public void init() {
    }

    public boolean isUnderDevelopment() {
        return Configuration.PROJECTSTATUS_DEVELOP.equals(getProjectStatus());
    }

    public Request prepareForProcessing(String contextPath, String method,
            String characterEncoding, Map<String, String[]> parameterMap,
            Map<String, FormFile[]> fileParameterMap,
            AttributeContainer attributeContainer, Locale locale) {
        return null;
    }

    public void enterDispatch(Request request, String path, String queryString,
            Dispatcher dispatcher) {
        request.enterDispatch(new MockDispatch().setPath(path).setQueryString(
                queryString).setDispatcher(dispatcher));
    }

    public void leaveDispatch(Request request) {
        request.leaveDispatch();
    }

    public Response processException(Request request, Throwable t) {
        return null;
    }

    public Response processRequest(Request request)
            throws PageNotFoundException, PermissionDeniedException {
        return null;
    }

    public HttpServletResponseFilter processResponse(ServletContext context_,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Request request, Response response) throws IOException,
            ServletException {
        return null;
    }

    public void restoreForInclusion(AttributeContainer attributeContainer,
            Object backupped) {
    }

    public YmirProcessInterceptor[] getYmirProcessInterceptors() {
        return new YmirProcessInterceptor[0];
    }

    public void updateRequest(Request request, HttpServletRequest httpRequest) {
    }
}
