package org.seasar.ymir.extension.creator.util;

import java.util.Iterator;
import java.util.Map;

import org.seasar.ymir.FormFile;
import org.seasar.ymir.Request;
import org.seasar.ymir.RequestWrapper;

public class ParameterReplacedRequestWrapper extends RequestWrapper {
    private Map<String, String[]> parameterMap_;

    private Map<String, FormFile[]> fileParameterMap_;

    public ParameterReplacedRequestWrapper(Request request,
            Map<String, String[]> parameterMap,
            Map<String, FormFile[]> fileParameterMap) {
        super(request);
        parameterMap_ = parameterMap;
        fileParameterMap_ = fileParameterMap;
    }

    @Override
    public String getParameter(String name) {
        return getParameter(name, null);
    }

    @Override
    public String getParameter(String name, String defaultValue) {
        String[] values = (String[]) parameterMap_.get(name);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return defaultValue;
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap_;
    }

    @Override
    public Iterator<String> getParameterNames() {
        return parameterMap_.keySet().iterator();
    }

    @Override
    public String[] getParameterValues(String name) {
        return getParameterValues(name, null);
    }

    @Override
    public String[] getParameterValues(String name, String[] defaultValues) {
        String[] values = (String[]) parameterMap_.get(name);
        if (values != null) {
            return values;
        } else {
            return defaultValues;
        }
    }

    @Override
    public FormFile getFileParameter(String name) {
        FormFile[] values = (FormFile[]) fileParameterMap_.get(name);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return null;
        }
    }

    @Override
    public Map<String, FormFile[]> getFileParameterMap() {
        return fileParameterMap_;
    }

    @Override
    public Iterator<String> getFileParameterNames() {
        return fileParameterMap_.keySet().iterator();
    }

    @Override
    public FormFile[] getFileParameterValues(String name) {
        FormFile[] values = (FormFile[]) fileParameterMap_.get(name);
        if (values != null) {
            return values;
        } else {
            return null;
        }
    }
}
