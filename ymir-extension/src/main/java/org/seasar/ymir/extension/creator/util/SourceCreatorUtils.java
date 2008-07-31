package org.seasar.ymir.extension.creator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

import org.seasar.kvasir.util.collection.MapProperties;
import org.seasar.ymir.Application;
import org.seasar.ymir.impl.SingleApplication;

public class SourceCreatorUtils {
    private static final String POM_XML = "pom.xml";

    private SourceCreatorUtils() {
    }

    public static MapProperties readAppPropertiesInOrder(Application application) {
        MapProperties prop = new MapProperties(
                new LinkedHashMap<String, String>());
        String filePath = application.getDefaultPropertiesFilePath();
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    prop.load(new FileInputStream(file));
                } catch (IOException ignore) {
                }
            }
        }
        return prop;
    }

    public static String getOriginalProjectRoot(Application application) {
        return readAppPropertiesInOrder(application).getProperty(
                SingleApplication.KEY_PROJECTROOT);
    }

    public static String findProjectRootDirectory(Application application) {
        String webappRoot = application.getWebappRoot();
        if (webappRoot == null) {
            return null;
        }

        File dir = new File(webappRoot);
        while (dir != null && !new File(dir, POM_XML).exists()) {
            dir = dir.getParentFile();
        }
        if (dir != null) {
            return dir.getAbsolutePath();
        } else {
            return null;
        }
    }

}
