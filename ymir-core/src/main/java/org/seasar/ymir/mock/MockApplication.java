package org.seasar.ymir.mock;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import org.seasar.cms.pluggable.hotdeploy.LocalHotdeployS2Container;
import org.seasar.ymir.Application;
import org.seasar.ymir.PathMappingProvider;
import org.seasar.framework.container.S2Container;

public class MockApplication implements Application {
    private S2Container s2container_;

    public String getId() {
        return null;
    }

    public String getDefaultPropertiesFilePath() {
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
        return null;
    }

    public String getProperty(String key, String defaultValue) {
        return null;
    }

    public Class getReferenceClass() {
        return null;
    }

    public String getResourcesDirectory() {
        return null;
    }

    public String getRootPackageName() {
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

    public boolean isCapable(Class clazz) {
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
    }

    public void setResourcesDirectory(String resourcesDirectory) {
    }

    public void setRootPackageName(String rootPackageName) {
    }

    public void setSourceDirectory(String sourceDirectory) {
    }

    public Enumeration propertyNames() {
        return null;
    }
}
