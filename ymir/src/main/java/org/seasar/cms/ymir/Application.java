package org.seasar.cms.ymir;

import java.io.IOException;
import java.io.OutputStream;

import org.seasar.framework.container.S2Container;

public interface Application {

    /**
     * Webアプリケーションがデプロイされているディレクトリのパスを返します。
     * <p>nullを返すことはありません。</p>
     *
     * @return Webアプリケーションのトップディレクトリのパス。
     */
    String getWebappRoot();

    Class getReferenceClass();

    String getProjectRoot();

    void setProjectRoot(String projectRoot);

    String getRootPackageName();

    void setRootPackageName(String rootPackageName);

    String getSourceDirectory();

    void setSourceDirectory(String sourceDirectory);

    String getClassesDirectory();

    void setClassesDirectory(String classesDirectory);

    String getResourcesDirectory();

    void setResourcesDirectory(String resourcesDirectory);

    boolean isResourceExists(String path);

    S2Container getS2Container();

    boolean isCapable(Class clazz);

    String getProperty(String key);

    String getProperty(String key, String defaultValue);

    void setProperty(String key, String value);

    void removeProperty(String key);

    void save(OutputStream out, String header) throws IOException;

    String getDefaultPropertiesFilePath();
}
