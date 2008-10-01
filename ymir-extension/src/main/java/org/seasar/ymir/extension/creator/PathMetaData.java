package org.seasar.ymir.extension.creator;

import java.io.File;

import org.seasar.ymir.HttpMethod;

public interface PathMetaData {
    HttpMethod getMethod();

    String getPath();

    boolean isDenied();

    String getComponentName();

    String getClassName();

    File getBaseSourceFile();

    File getSourceFile();

    Template getTemplate();
}
