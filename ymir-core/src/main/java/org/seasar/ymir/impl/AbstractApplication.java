package org.seasar.ymir.impl;

import java.net.URL;

import org.seasar.cms.pluggable.hotdeploy.LocalHotdeployS2Container;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Application;
import org.seasar.ymir.PathMappingProvider;
import org.seasar.ymir.util.FileUtils;

abstract public class AbstractApplication implements Application {
    public static final String KEY_PROJECTROOT = "projectRoot";

    public static final String KEY_WEBAPPSOURCEROOT = "webappSourceRoot";

    public static final String KEY_ROOTPACKAGENAME = "rootPackageName";

    public static final String KEY_RESOURCESDIRECTORY = "resourcesDirectory";

    public static final String KEY_SOURCEDIRECTORY = "sourceDirectory";

    public static final String KEY_TEMPLATEENCODING = "templateEncoding";

    private static final String PROPERTIESPATH = "app.properties";

    private static final String LOCALPROPERTIESPATH = "app-local.properties";

    private String id_;

    private LocalHotdeployS2Container ondemandContainer_;

    private PathMappingProvider pathMappingProvider_;

    protected AbstractApplication(String id,
            LocalHotdeployS2Container ondemandContainer,
            PathMappingProvider pathMappingProvider) {
        id_ = id;
        ondemandContainer_ = ondemandContainer;
        pathMappingProvider_ = pathMappingProvider;
    }

    public String getId() {
        return id_;
    }

    public String getProjectRoot() {
        return getProperty(KEY_PROJECTROOT);
    }

    public void setProjectRoot(String projectRoot) {
        setProperty(KEY_PROJECTROOT, projectRoot);
    }

    @Deprecated
    public String getRootPackageName() {
        return getProperty(KEY_ROOTPACKAGENAME);
    }

    @Deprecated
    public void setRootPackageName(String rootPackageName) {
        setProperty(KEY_ROOTPACKAGENAME, rootPackageName);
    }

    public String[] getRootPackageNames() {
        return PropertyUtils.toLines(getProperty(KEY_ROOTPACKAGENAME));
    }

    public String getFirstRootPackageName() {
        String[] rootPackageNames = getRootPackageNames();
        if (rootPackageNames.length > 0) {
            return rootPackageNames[0];
        } else {
            return null;
        }
    }

    public String getResourcesDirectory() {
        String resourcesDirectory = getProperty(KEY_RESOURCESDIRECTORY);
        if (resourcesDirectory == null) {
            String projectRoot = getProjectRoot();
            if (projectRoot != null) {
                resourcesDirectory = projectRoot + "/src/main/resources";
            }
        } else {
            String projectRoot = getProjectRoot();
            if (projectRoot != null
                    && FileUtils.isRelativePath(resourcesDirectory)) {
                resourcesDirectory = projectRoot + "/" + resourcesDirectory;
            }
        }
        return resourcesDirectory;
    }

    public void setResourcesDirectory(String resourcesDirectory) {
        setProperty(KEY_RESOURCESDIRECTORY, resourcesDirectory);
    }

    public String getSourceDirectory() {
        String sourceDirectory = getProperty(KEY_SOURCEDIRECTORY);
        if (sourceDirectory == null) {
            String projectRoot = getProjectRoot();
            if (projectRoot != null) {
                sourceDirectory = projectRoot + "/src/main/java";
            }
        } else {
            String projectRoot = getProjectRoot();
            if (projectRoot != null
                    && FileUtils.isRelativePath(sourceDirectory)) {
                sourceDirectory = projectRoot + "/" + sourceDirectory;
            }
        }
        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        setProperty(KEY_SOURCEDIRECTORY, sourceDirectory);
    }

    public String getWebappSourceRoot() {
        String webappSourceRoot = getProperty(KEY_WEBAPPSOURCEROOT);
        if (webappSourceRoot == null) {
            String projectRoot = getProjectRoot();
            if (projectRoot != null) {
                webappSourceRoot = projectRoot + "/src/main/webapp";
            }
        } else {
            String projectRoot = getProjectRoot();
            if (projectRoot != null
                    && FileUtils.isRelativePath(webappSourceRoot)) {
                webappSourceRoot = projectRoot + "/" + webappSourceRoot;
            }
        }
        return webappSourceRoot;
    }

    public void setWebappSourceRoot(String webappSourceRoot) {
        setProperty(KEY_WEBAPPSOURCEROOT, webappSourceRoot);
    }

    public String getDefaultPropertiesFilePath() {
        String resourcesDirectory = getResourcesDirectory();
        if (resourcesDirectory == null) {
            return null;
        } else {
            return resourcesDirectory + "/" + PROPERTIESPATH;
        }
    }

    public URL getDefaultPropertiesResourceURL() {
        return getClass().getClassLoader().getResource(PROPERTIESPATH);
    }

    public String getDefaultLocalPropertiesFilePath() {
        String resourcesDirectory = getResourcesDirectory();
        if (resourcesDirectory == null) {
            return null;
        } else {
            return resourcesDirectory + "/" + LOCALPROPERTIESPATH;
        }
    }

    public URL getDefaultLocalPropertiesResourceURL() {
        return getClass().getClassLoader().getResource(LOCALPROPERTIESPATH);
    }

    public String getTemplateEncoding() {
        String templateEncoding = getProperty(KEY_TEMPLATEENCODING);
        if (templateEncoding == null) {
            templateEncoding = "UTF-8";
        }
        return templateEncoding;
    }

    public LocalHotdeployS2Container getHotdeployS2Container() {
        return ondemandContainer_;
    }

    public PathMappingProvider getPathMappingProvider() {
        return pathMappingProvider_;
    }

    public boolean isCapable(Class<?> clazz) {
        for (String rootPackageName : getRootPackageNames()) {
            return clazz.getName().startsWith(rootPackageName + ".");
        }
        return false;
    }
}
