package org.seasar.ymir.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.FrameworkRequest;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.impl.RequestImpl;

class FrameworkRequestImpl implements FrameworkRequest {
    private Request request_;

    private RequestImpl original_;

    FrameworkRequestImpl(Request request) {
        request_ = request;
        original_ = YmirUtils.unwrapRequest(request);
    }

    public void enterDispatch(Dispatch dispatch) {
        request_.enterDispatch(dispatch);
    }

    public String getAbsolutePath() {
        return request_.getAbsolutePath();
    }

    public String getActionName() {
        return request_.getActionName();
    }

    public Object getAttribute(String name) {
        return request_.getAttribute(name);
    }

    public AttributeContainer getAttributeContainer() {
        return request_.getAttributeContainer();
    }

    public Enumeration<String> getAttributeNames() {
        return request_.getAttributeNames();
    }

    public String getCharacterEncoding() {
        return request_.getCharacterEncoding();
    }

    public String getContextPath() {
        return request_.getContextPath();
    }

    public Dispatch getCurrentDispatch() {
        return request_.getCurrentDispatch();
    }

    public boolean isIncluded() {
        return request_.isIncluded();
    }

    public Object getExtendedParameter(String name, Object defaultValue) {
        return request_.getExtendedParameter(name, defaultValue);
    }

    public Object getExtendedParameter(String name) {
        return request_.getExtendedParameter(name);
    }

    public Map<String, Object> getExtendedParameterMap() {
        return request_.getExtendedParameterMap();
    }

    public Iterator<String> getExtendedParameterNames() {
        return request_.getExtendedParameterNames();
    }

    public FormFile getFileParameter(String name) {
        return request_.getFileParameter(name);
    }

    public Map<String, FormFile[]> getFileParameterMap() {
        return request_.getFileParameterMap();
    }

    public Iterator<String> getFileParameterNames() {
        return request_.getFileParameterNames();
    }

    public FormFile[] getFileParameterValues(String name) {
        return request_.getFileParameterValues(name);
    }

    public HttpMethod getMethod() {
        return request_.getMethod();
    }

    public String getParameter(String name, String defaultValue) {
        return request_.getParameter(name, defaultValue);
    }

    public String getParameter(String name) {
        return request_.getParameter(name);
    }

    public Map<String, String[]> getParameterMap() {
        return request_.getParameterMap();
    }

    public Iterator<String> getParameterNames() {
        return request_.getParameterNames();
    }

    public String[] getParameterValues(String name, String[] defaultValues) {
        return request_.getParameterValues(name, defaultValues);
    }

    public String[] getParameterValues(String name) {
        return request_.getParameterValues(name);
    }

    public String getPath() {
        return request_.getPath();
    }

    public Dispatch getRequestDispatch() {
        return request_.getRequestDispatch();
    }

    public void leaveDispatch() {
        request_.leaveDispatch();
    }

    public void removeAttribute(String name) {
        request_.removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        request_.setAttribute(name, value);
    }

    public void setExtendedParameterMap(Map<String, Object> extendedParameterMap) {
        original_.setExtendedParameterMap(extendedParameterMap);
    }
}
