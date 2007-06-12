package org.seasar.ymir.mock;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.Request;

public class MockRequest implements Request {

    private String absolutePath_;

    private String actionName_;

    private AttributeContainer attributeContainer_ = this;

    private String componentName_;

    private String contextPath_;

    private Object defaultReturnValue_;

    private String dispatcher_;

    private String method_;

    private Map<String, String[]> parameterMap_ = new HashMap<String, String[]>();

    private Map<String, FormFile[]> fileParameterMap_ = new HashMap<String, FormFile[]>();

    private String path_;

    private String pathInfo_;

    private boolean dispatchingByRequestParameter_;

    private boolean dispatchingByParameter_;

    private Locale locale_;

    private boolean matched_;

    private boolean denied_;

    private Map<String, Object> attributeMap_ = new HashMap<String, Object>();

    public String getAbsolutePath() {
        return absolutePath_;
    }

    public String getActionName() {
        return actionName_;
    }

    public AttributeContainer getAttributeContainer() {
        return attributeContainer_;
    }

    public String getComponentName() {
        return componentName_;
    }

    public String getContextPath() {
        return contextPath_;
    }

    public Object getDefaultReturnValue() {
        return defaultReturnValue_;
    }

    public String getDispatcher() {
        return dispatcher_;
    }

    public boolean isDispatchingByParameter() {
        return dispatchingByParameter_;
    }

    public boolean isDispatchingByRequestParameter() {
        return dispatchingByRequestParameter_;
    }

    public boolean isMatched() {
        return matched_;
    }

    public boolean isDenied() {
        return denied_;
    }

    public String getMethod() {
        return method_;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap_;
    }

    public MockRequest setParameter(String name, String value) {
        getParameterMap().put(name, new String[] { value });
        return this;
    }

    public MockRequest setParameterValues(String name, String[] values) {
        getParameterMap().put(name, values);
        return this;
    }

    public String getPath() {
        return path_;
    }

    public String getPathInfo() {
        return pathInfo_;
    }

    public MockRequest setAbsolutePath(String absolutePath) {
        absolutePath_ = absolutePath;
        return this;
    }

    public void setActionName(String actionName) {
        actionName_ = actionName;
    }

    public MockRequest setAttributeContainer(
            AttributeContainer attributeContainer) {
        attributeContainer_ = attributeContainer;
        return this;
    }

    public MockRequest setComponentName(String componentName) {
        componentName_ = componentName;
        return this;
    }

    public MockRequest setContextPath(String contextPath) {
        contextPath_ = contextPath;
        return this;
    }

    public MockRequest setDefaultReturnValue(Object defaultReturnValue) {
        defaultReturnValue_ = defaultReturnValue;
        return this;
    }

    public MockRequest setDispatcher(String dispatcher) {
        dispatcher_ = dispatcher;
        return this;
    }

    public MockRequest setDispatchingByParameter(boolean dispatchingByParameter) {
        dispatchingByParameter_ = dispatchingByParameter;
        return this;
    }

    public MockRequest setDispatchingByRequestParameter(
            boolean dispatchingByRequestParameter) {
        dispatchingByRequestParameter_ = dispatchingByRequestParameter;
        return this;
    }

    public MockRequest setMatched(boolean matched) {
        matched_ = matched;
        return this;
    }

    public MockRequest setMethod(String method) {
        method_ = method;
        return this;
    }

    public MockRequest setParameterMap(Map<String, String[]> parameterMap) {
        parameterMap_ = parameterMap;
        return this;
    }

    public MockRequest setFileParameterMap(
            Map<String, FormFile[]> fileParameterMap) {
        fileParameterMap_ = fileParameterMap;
        return this;
    }

    public MockRequest setFileParameter(String name, FormFile value) {
        getFileParameterMap().put(name, new FormFile[] { value });
        return this;
    }

    public MockRequest setFileParameterValues(String name, FormFile[] values) {
        getFileParameterMap().put(name, values);
        return this;
    }

    public MockRequest setPath(String path) {
        path_ = path;
        return this;
    }

    public MockRequest setPathInfo(String pathInfo) {
        pathInfo_ = pathInfo;
        return this;
    }

    public String extractParameterName(String name) {
        return null;
    }

    public FormFile getFileParameter(String name) {
        FormFile[] values = getFileParameterValues(name);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return null;
        }
    }

    public Map<String, FormFile[]> getFileParameterMap() {
        return fileParameterMap_;
    }

    public Iterator<String> getFileParameterNames() {
        return getFileParameterMap().keySet().iterator();
    }

    public FormFile[] getFileParameterValues(String name) {
        return getFileParameterMap().get(name);
    }

    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return null;
        }
    }

    public String getParameter(String name, String defaultValue) {
        String value = getParameter(name);
        if (value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }

    public Iterator<String> getParameterNames() {
        return getParameterMap().keySet().iterator();
    }

    public String[] getParameterValues(String name) {
        return getParameterMap().get(name);
    }

    public String[] getParameterValues(String name, String[] defaultValues) {
        String[] values = getParameterValues(name);
        if (values != null) {
            return values;
        } else {
            return defaultValues;
        }
    }

    public Object getAttribute(String name) {
        return attributeMap_.get(name);
    }

    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributeMap_.keySet());
    }

    public void removeAttribute(String name) {
        attributeMap_.remove(name);
    }

    public void setAttribute(String name, Object value) {
        attributeMap_.put(name, value);
    }

    public Locale getLocale() {
        return locale_;
    }

    public void setLocale(Locale locale) {
        locale_ = locale;
    }
}
