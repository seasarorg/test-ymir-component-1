package org.seasar.ymir.mock;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.ymir.Application;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.HttpServletResponseFilter;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.Ymir;
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

    public MatchedPathMapping findMatchedPathMapping(String path,
            HttpMethod method) {
        return null;
    }

    public String getPathOfPageClass(Class<?> pageClass) {
        return getPathOfPageClass(pageClass != null ? pageClass.getName()
                : null);
    }

    public String getPathOfPageClass(String pageClassName) {
        return null;
    }

    public String getPageClassNameOfPath(String path) {
        return null;
    }

    public Class<?> getPageClassOfPath(String path) {
        return null;
    }

    public YmirProcessInterceptor[] getYmirProcessInterceptors() {
        return new YmirProcessInterceptor[0];
    }

    public void process(ServletContext servletContext,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            Dispatcher dispatcher, String path, HttpMethod method,
            Map<String, FormFile[]> fileParameterMap, FilterChain chain)
            throws IOException, ServletException {
    }
}
