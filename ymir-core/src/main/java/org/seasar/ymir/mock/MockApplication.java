package org.seasar.ymir.mock;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.cms.pluggable.hotdeploy.LocalHotdeployS2Container;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.ymir.Application;
import org.seasar.ymir.PathMappingProvider;

public class MockApplication implements Application {
    private S2Container s2container_ = new S2ContainerImpl();

    private Map<Class<?>, Object> relatedObjectMap_ = new ConcurrentHashMap<Class<?>, Object>();

    private Properties prop_ = new Properties();

    public String getId() {
        return null;
    }

    public String getDefaultPropertiesFilePath() {
        return null;
    }

    public URL getDefaultPropertiesResourceURL() {
        return null;
    }

    public String getDefaultLocalPropertiesFilePath() {
        return null;
    }

    public URL getDefaultLocalPropertiesResourceURL() {
        return null;
    }

    public LocalHotdeployS2Container getHotdeployS2Container() {
        return null;
    }

    public PathMappingProvider getPathMappingProvider() {
        return null;
    }

    public String getProjectRoot() {
        return null;
    }

    public String getProperty(String key) {
        return prop_.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return prop_.getProperty(key, defaultValue);
    }

    public Class<?>[] getReferenceClasses() {
        return new Class[0];
    }

    public String getResourcesDirectory() {
        return null;
    }

    @Deprecated
    public String getRootPackageName() {
        return null;
    }

    @Deprecated
    public void setRootPackageName(String rootPackageName) {
    }

    public String[] getRootPackageNames() {
        return new String[0];
    }

    public String getFirstRootPackageName() {
        return null;
    }

    public S2Container getS2Container() {
        return s2container_;
    }

    public MockApplication setS2Container(S2Container s2container) {
        s2container_ = s2container;
        return this;
    }

    public String getSourceDirectory() {
        return null;
    }

    public String getWebappRoot() {
        return null;
    }

    public String getWebappSourceRoot() {
        return null;
    }

    public boolean isUnderDevelopment() {
        return false;
    }

    public boolean isCapable(Class<?> clazz) {
        return false;
    }

    public boolean isResourceExists(String path) {
        return false;
    }

    public void removeProperty(String key) {
    }

    public void save(OutputStream out, String header) throws IOException {
    }

    public void setProjectRoot(String projectRoot) {
    }

    public void setProperty(String key, String value) {
        prop_.setProperty(key, value);
    }

    public void setResourcesDirectory(String resourcesDirectory) {
    }

    @Deprecated
    public void setSourceDirectory(String sourceDirectory) {
    }

    @SuppressWarnings("unchecked")
    public Enumeration<String> propertyNames() {
        return (Enumeration<String>) prop_.propertyNames();
    }

    @SuppressWarnings("unchecked")
    public <T> T getRelatedObject(Class<T> clazz) {
        return (T) relatedObjectMap_.get(clazz);
    }

    public <T> void setRelatedObject(Class<T> clazz, T object) {
        if (object != null) {
            relatedObjectMap_.put(clazz, object);
        } else {
            relatedObjectMap_.remove(clazz);
        }
    }

    public void clear() {
        relatedObjectMap_.clear();
    }

    public String getTemplateEncoding() {
        return null;
    }
}
