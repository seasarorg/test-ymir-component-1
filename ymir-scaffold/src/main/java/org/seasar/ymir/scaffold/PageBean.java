package org.seasar.ymir.scaffold;

import java.util.Map;

public class PageBean {
    private String name;

    private String path;

    private Map<String, String[]> parameterMap;

    private Class<?> pageClass;

    public PageBean() {
    }

    public PageBean(String name, String path, Class<?> pageClass) {
        this(name, path, null, pageClass);
    }

    public PageBean(String name, String path,
            Map<String, String[]> parameterMap, Class<?> pageClass) {
        this.name = name;
        this.path = path;
        this.parameterMap = parameterMap;
        this.pageClass = pageClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public Class<?> getPageClass() {
        return pageClass;
    }

    public void setPageClass(Class<?> pageClass) {
        this.pageClass = pageClass;
    }
}
