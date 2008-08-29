package org.seasar.ymir.extension.creator;

import java.io.File;

public interface PathMetaData {

    String getMethod();

    String getPath();

    boolean isDenied();

    String getComponentName();

    String getClassName();

    File getBaseSourceFile();

    File getSourceFile();

    Template getTemplate();
}
