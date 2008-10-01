package org.seasar.ymir.mock;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.RequestWrapper;
import org.seasar.ymir.impl.RequestImpl;

public class MockRequest extends RequestWrapper {
    private AttributeContainer attributeContainer_ = this;

    private String contextPath_;

    private HttpMethod method_;

    private Map<String, String[]> parameterMap_ = new HashMap<String, String[]>();

    private Map<String, FormFile[]> fileParameterMap_ = new HashMap<String, FormFile[]>();

    private boolean dispatchingByParameter_;

    private Locale locale_;

    private Map<String, Object> attributeMap_ = new HashMap<String, Object>();

    private MatchedPathMapping matchedPathMapping_ = new MockMatchedPathMapping();

    private Dispatch requestDispatch_;

    private Dispatch dispatch_;

    private LinkedList<Dispatch> dispatchStack_ = new LinkedList<Dispatch>();

    public MockRequest() {
        super(new RequestImpl());
    }

    public String getAbsolutePath() {
        if (requestDispatch_ != null) {
            return requestDispatch_.getAbsolutePath();
        } else {
            return null;
        }
    }

    public AttributeContainer getAttributeContainer() {
        return attributeContainer_;
    }

    public String getContextPath() {
        return contextPath_;
    }

    public Dispatcher getDispatcher() {
        if (requestDispatch_ != null) {
            return requestDispatch_.getDispatcher();
        } else {
            return null;
        }
    }

    public boolean isMatched() {
        return matchedPathMapping_ != null;
    }

    public boolean isDenied() {
        return matchedPathMapping_ == null || matchedPathMapping_.isDenied();
    }

    public HttpMethod getMethod() {
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
        if (requestDispatch_ != null) {
            return requestDispatch_.getPath();
        } else {
            return null;
        }
    }

    public String getPathInfo() {
        if (requestDispatch_ != null) {
            return requestDispatch_.getPathInfo();
        } else {
            return null;
        }
    }

    public MockRequest setAttributeContainer(
            AttributeContainer attributeContainer) {
        attributeContainer_ = attributeContainer;
        return this;
    }

    public MockRequest setContextPath(String contextPath) {
        contextPath_ = contextPath;
        return this;
    }

    public MockRequest setDispatchingByParameter(boolean dispatchingByParameter) {
        dispatchingByParameter_ = dispatchingByParameter;
        return this;
    }

    public MockRequest setMethod(HttpMethod method) {
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

    public MatchedPathMapping getMatchedPathMapping() {
        return matchedPathMapping_;
    }

    public MockRequest setMatchedPathMapping(
            MatchedPathMapping matchedPathMapping) {
        matchedPathMapping_ = matchedPathMapping;
        return this;
    }

    public Dispatch getCurrentDispatch() {
        return dispatch_;
    }

    public Dispatch getRequestDispatch() {
        return requestDispatch_;
    }

    public void enterDispatch(Dispatch dispatch) {
        if (requestDispatch_ == null) {
            requestDispatch_ = dispatch;
        }
        dispatchStack_.addFirst(dispatch);
        dispatch_ = dispatch;
    }

    public void leaveDispatch() {
        dispatchStack_.removeFirst();
        if (!dispatchStack_.isEmpty()) {
            dispatch_ = dispatchStack_.peek();
        } else {
            dispatch_ = null;
            requestDispatch_ = null;
        }
    }

    public String getActionName() {
        return dispatch_.getActionName();
    }
}
