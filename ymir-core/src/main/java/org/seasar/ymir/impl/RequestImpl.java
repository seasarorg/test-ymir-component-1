package org.seasar.ymir.impl;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.seasar.ymir.AttributeContainer;
import org.seasar.ymir.Dispatch;
import org.seasar.ymir.Dispatcher;
import org.seasar.ymir.FormFile;
import org.seasar.ymir.FrameworkRequest;
import org.seasar.ymir.HttpMethod;

public class RequestImpl implements FrameworkRequest {
    private String contextPath_;

    private HttpMethod method_;

    private String characterEncoding_;

    private Map<String, String[]> queryParameterMap_;

    private Map<String, FormFile[]> fileParameterMap_;

    private Map<String, Object> extendedParameterMap_ = Collections.emptyMap();

    private Map<String, String[]> parameterMap_;

    private AttributeContainer attributeContainer_;

    private Dispatch requestDispatch_;

    private Dispatch dispatch_;

    private LinkedList<Dispatch> dispatchStack_ = new LinkedList<Dispatch>();

    public RequestImpl() {
    }

    public RequestImpl(String contextPath, HttpMethod method,
            String characterEncoding, Map<String, String[]> queryParameterMap,
            Map<String, FormFile[]> fileParameterMap,
            AttributeContainer attributeContainer) {
        contextPath_ = contextPath;
        method_ = method;
        characterEncoding_ = characterEncoding;
        setQueryParameterMap(safeMap(queryParameterMap));
        fileParameterMap_ = safeMap(fileParameterMap);
        attributeContainer_ = attributeContainer;
    }

    <K, V> Map<K, V> safeMap(Map<K, V> map) {
        return map != null ? map : new HashMap<K, V>();
    }

    public HttpMethod getMethod() {
        return method_;
    }

    public void setMethod(HttpMethod method) {
        method_ = method;
    }

    public String getCharacterEncoding() {
        return characterEncoding_;
    }

    public void setCharacterEncoding(String characterEncoding) {
        characterEncoding_ = characterEncoding;
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
        String[] values = parameterMap_.get(name);
        if (values != null) {
            if (values.length > 0) {
                return values[0];
            } else {
                // チェックボックスのための補正によって、valuesがこうなることがある。
                // その場合といえどもgetParameterNames()はこのnameを返すため、
                // ここでnullを返すようにしてしまうとNPEの元になる。
                // そのためここでは空文字列を返すようにしている。
                return "";
            }
        } else {
            return defaultValue;
        }
    }

    public String[] getParameterValues(String name, String[] defaultValues) {
        String[] values = parameterMap_.get(name);
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

    // parameterMapを再構築していないため、enterDispatch～leaveDispatchの間では呼び出してはいけない！
    public void setQueryParameterMap(Map<String, String[]> queryParameterMap) {
        queryParameterMap_ = queryParameterMap;
        parameterMap_ = queryParameterMap;
    }

    public FormFile getFileParameter(String name) {
        FormFile[] values = fileParameterMap_.get(name);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return null;
        }
    }

    public FormFile[] getFileParameterValues(String name) {
        FormFile[] values = fileParameterMap_.get(name);
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

    public Object getExtendedParameter(String name) {
        return extendedParameterMap_.get(name);
    }

    public Object getExtendedParameter(String name, Object defaultValue) {
        Object value = extendedParameterMap_.get(name);
        if (value != null) {
            return value;
        } else {
            return defaultValue;
        }
    }

    public Iterator<String> getExtendedParameterNames() {
        return extendedParameterMap_.keySet().iterator();
    }

    public Map<String, Object> getExtendedParameterMap() {
        return extendedParameterMap_;
    }

    public void setExtendedParameterMap(Map<String, Object> extendedParameterMap) {
        extendedParameterMap_ = extendedParameterMap;
    }

    public AttributeContainer getAttributeContainer() {
        return attributeContainer_;
    }

    public void setAttributeContainer(AttributeContainer attributeContainer) {
        attributeContainer_ = attributeContainer;
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

    public Dispatch getRequestDispatch() {
        return requestDispatch_;
    }

    public Dispatch getCurrentDispatch() {
        return dispatch_;
    }

    public boolean isIncluded() {
        for (Dispatch dispatch : dispatchStack_) {
            if (dispatch.getDispatcher() == Dispatcher.INCLUDE) {
                return true;
            }
        }
        return false;
    }

    public void enterDispatch(Dispatch dispatch) {
        if (requestDispatch_ == null) {
            requestDispatch_ = dispatch;
        }
        dispatchStack_.addFirst(dispatch);
        dispatch_ = dispatch;

        parameterMap_ = mergeParameterMap(queryParameterMap_, dispatch
                .getURIParameterMap());
    }

    Map<String, String[]> mergeParameterMap(
            Map<String, String[]> queryParameterMap,
            Map<String, String[]> uriParameterMap) {
        Map<String, String[]> merged = new HashMap<String, String[]>(
                queryParameterMap);
        for (Iterator<Map.Entry<String, String[]>> itr = uriParameterMap
                .entrySet().iterator(); itr.hasNext();) {
            Map.Entry<String, String[]> entry = itr.next();
            String key = entry.getKey();
            String[] value = entry.getValue();
            String[] v = merged.get(key);
            String[] newV;
            if (v == null) {
                newV = value;
            } else {
                newV = new String[v.length + value.length];
                System.arraycopy(v, 0, newV, 0, v.length);
                System.arraycopy(value, 0, newV, v.length, value.length);
            }
            merged.put(key, newV);
        }
        return merged;
    }

    public void leaveDispatch() {
        parameterMap_ = unmergeParameterMap(queryParameterMap_);

        dispatchStack_.removeFirst();
        if (!dispatchStack_.isEmpty()) {
            dispatch_ = dispatchStack_.peek();
        } else {
            dispatch_ = null;
            requestDispatch_ = null;
        }
    }

    Map<String, String[]> unmergeParameterMap(
            Map<String, String[]> queryParameterMap) {
        return queryParameterMap;
    }

    public String getActionName() {
        return dispatch_.getActionName();
    }

    public String getOriginalActionName() {
        return dispatch_.getOriginalActionName();
    }
}
