package org.seasar.ymir.impl;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import org.seasar.ymir.Action;
import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.Request;

public class RequestImpl implements Request {
    private String contextPath_;

    private String method_;

    private Map<String, String[]> parameterMap_;

    private Map<String, FormFile[]> fileParameterMap_;

    private AttributeContainer attributeContainer_;

    private Locale locale_;

    private Dispatch requestDispatch_;

    private Dispatch dispatch_;

    private LinkedList<Dispatch> dispatchStack_ = new LinkedList<Dispatch>();

    private PageComponent pageComponent_;

    private Action action_;

    public RequestImpl() {
    }

    public RequestImpl(String contextPath, String method,
            Map<String, String[]> parameterMap,
            Map<String, FormFile[]> fileParameterMap,
            AttributeContainer attributeContainer, Locale locale) {
        contextPath_ = contextPath;
        method_ = method;
        parameterMap_ = parameterMap;
        fileParameterMap_ = fileParameterMap;
        attributeContainer_ = attributeContainer;
        locale_ = locale;
    }

    public String getDispatcher() {
        return requestDispatch_.getDispatcher();
    }

    public String getMethod() {
        return method_;
    }

    public void setMethod(String method) {
        method_ = method;
    }

    public String getContextPath() {
        return contextPath_;
    }

    public void setContextPath(String contextPath) {
        contextPath_ = contextPath;
    }

    public String getPath() {
        return requestDispatch_.getPath();
    }

    public String getAbsolutePath() {
        return requestDispatch_.getAbsolutePath();
    }

    public String getParameter(String name) {
        return getParameter(name, null);
    }

    public String getParameter(String name, String defaultValue) {
        String[] values = (String[]) parameterMap_.get(name);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return defaultValue;
        }
    }

    public String[] getParameterValues(String name, String[] defaultValues) {
        String[] values = (String[]) parameterMap_.get(name);
        if (values != null) {
            return values;
        } else {
            return defaultValues;
        }
    }

    public String[] getParameterValues(String name) {
        return getParameterValues(name, null);
    }

    public Iterator<String> getParameterNames() {
        return parameterMap_.keySet().iterator();
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap_;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        parameterMap_ = parameterMap;
    }

    public FormFile getFileParameter(String name) {
        FormFile[] values = (FormFile[]) fileParameterMap_.get(name);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return null;
        }
    }

    public FormFile[] getFileParameterValues(String name) {
        FormFile[] values = (FormFile[]) fileParameterMap_.get(name);
        if (values != null) {
            return values;
        } else {
            return null;
        }
    }

    public Iterator<String> getFileParameterNames() {
        return fileParameterMap_.keySet().iterator();
    }

    public Map<String, FormFile[]> getFileParameterMap() {
        return fileParameterMap_;
    }

    public void setFileParameterMap(Map<String, FormFile[]> fileParameterMap) {
        fileParameterMap_ = fileParameterMap;
    }

    public AttributeContainer getAttributeContainer() {
        return attributeContainer_;
    }

    public void setAttributeContainer(AttributeContainer attributeContainer) {
        attributeContainer_ = attributeContainer;
    }

    public String getPageComponentName() {
        return requestDispatch_.getMatchedPathMapping().getPageComponentName();
    }

    public Action getAction() {
        return action_;
    }

    public String getActionName() {
        if (action_ != null) {
            return action_.getName();
        } else {
            return null;
        }
    }

    public void setAction(Action action) {
        action_ = action;
    }

    public String getPathInfo() {
        return requestDispatch_.getMatchedPathMapping().getPathInfo();
    }

    public MatchedPathMapping getMatchedPathMapping() {
        return requestDispatch_.getMatchedPathMapping();
    }

    public Object getAttribute(String name) {
        return attributeContainer_.getAttribute(name);
    }

    public Enumeration<String> getAttributeNames() {
        return attributeContainer_.getAttributeNames();
    }

    public void removeAttribute(String name) {
        attributeContainer_.removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        attributeContainer_.setAttribute(name, value);
    }

    public Locale getLocale() {
        return locale_;
    }

    public boolean isMatched() {
        return requestDispatch_.isMatched();
    }

    public boolean isDenied() {
        return requestDispatch_.isDenied();
    }

    public PageComponent getPageComponent() {
        return pageComponent_;
    }

    public void setPageComponent(PageComponent pageComponent) {
        pageComponent_ = pageComponent;
    }

    public Dispatch getCurrentDispatch() {
        return dispatch_;
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
}
