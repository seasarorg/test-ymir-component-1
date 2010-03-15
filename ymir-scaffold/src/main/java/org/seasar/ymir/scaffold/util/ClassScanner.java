package org.seasar.ymir.scaffold.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.seasar.framework.util.ResourcesUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourcesUtil.Resources;

/**
 * @since 1.0.7
 */
public class ClassScanner {
    public List<Class<?>> scan(String packageName, String classNamePattern) {
        final String packagePrefix = packageName + ".";
        final Pattern pattern = Pattern.compile(classNamePattern);
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        for (Resources resources : ResourcesUtil.getResourcesTypes(packageName)) {
            try {
                resources.forEach(new ClassHandler() {
                    public void processClass(String packageName,
                            String shortClassName) {
                        try {
                            String className = packageName + "."
                                    + shortClassName;
                            if (className.startsWith(packagePrefix)
                                    && pattern.matcher(
                                            className.substring(packagePrefix
                                                    .length())).matches()) {
                                classes.add(cl.loadClass(className));
                            }
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException("Can't happen!", ex);
                        }
                    }
                });
            } finally {
                resources.close();
            }
        }
        return classes;
    }

    public List<String> scanNames(String packageName, String classNamePattern) {
        final String packagePrefix = packageName + ".";
        final Pattern pattern = Pattern.compile(classNamePattern);
        final List<String> classNames = new ArrayList<String>();
        for (Resources resources : ResourcesUtil.getResourcesTypes(packageName)) {
            try {
                resources.forEach(new ClassHandler() {
                    public void processClass(String packageName,
                            String shortClassName) {
                        String className = packageName + "." + shortClassName;
                        if (className.startsWith(packagePrefix)
                                && pattern.matcher(
                                        className.substring(packagePrefix
                                                .length())).matches()) {
                            classNames.add(className);
                        }
                    }
                });
            } finally {
                resources.close();
            }
        }
        return classNames;
    }
}
