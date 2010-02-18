package org.seasar.ymir.extension.creator.impl;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ImportDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.util.ClassUtils;

public class ImportDescImpl implements ImportDesc {
    private static final String PREFIX_JAVA = "java.";

    private static final String PREFIX_JAVAX = "javax.";

    private static final String LS = System.getProperty("line.separator");

    private SourceCreator sourceCreator_;

    private ClassDesc classDesc_;

    private Set<String> applicationClassSet_ = new TreeSet<String>();

    private Set<String> frameworkClassSet_ = new TreeSet<String>();

    private Set<String> javaClassSet_ = new TreeSet<String>();

    private Set<String> javaxClassSet_ = new TreeSet<String>();

    public ImportDescImpl(SourceCreator sourceCreator, ClassDesc classDesc) {
        sourceCreator_ = sourceCreator;
        classDesc_ = classDesc;
    }

    @Override
    public String toString() {
        return getAsString();
    }

    public void add(Collection<String> collection) {
        for (String className : collection) {
            add0(className);
        }
    }

    public void add(String... classNames) {
        for (String className : classNames) {
            add0(className);
        }
    }

    public void add(java.lang.Class<?>... classes) {
        for (Class<?> clazz : classes) {
            add0(clazz.getName());
        }
    }

    protected void add0(String className) {
        if (className == null) {
            return;
        } else if (ClassUtils.isStandard(className)) {
            return;
        } else if (className.startsWith(classDesc_.getPackageName() + ".")) {
            return;
        }

        if (className.startsWith(PREFIX_JAVA)) {
            javaClassSet_.add(className);
        } else if (className.startsWith(PREFIX_JAVAX)) {
            javaxClassSet_.add(className);
        } else {
            for (String rootPackageName : sourceCreator_.getRootPackageNames()) {
                if (className.startsWith(rootPackageName + ".")) {
                    applicationClassSet_.add(className);
                    return;
                }
            }
            frameworkClassSet_.add(className);
        }
    }

    public String[] getApplicationClassNames() {
        return applicationClassSet_.toArray(new String[0]);
    }

    public String[] getFrameworkClassNames() {
        return frameworkClassSet_.toArray(new String[0]);
    }

    public String[] getJavaClassNames() {
        return javaClassSet_.toArray(new String[0]);
    }

    public String[] getJavaxClassNames() {
        return javaxClassSet_.toArray(new String[0]);
    }

    public String getAsString() {
        StringBuilder sb = new StringBuilder();

        String delim = "";
        if (!javaClassSet_.isEmpty()) {
            sb.append(delim);
            delim = LS;
            for (String className : javaClassSet_) {
                sb.append("import ").append(className).append(";").append(LS);
            }
        }
        if (!javaxClassSet_.isEmpty()) {
            sb.append(delim);
            delim = LS;
            for (String className : javaxClassSet_) {
                sb.append("import ").append(className).append(";").append(LS);
            }
        }
        if (!frameworkClassSet_.isEmpty()) {
            sb.append(delim);
            delim = LS;
            for (String className : frameworkClassSet_) {
                sb.append("import ").append(className).append(";").append(LS);
            }
        }
        if (!applicationClassSet_.isEmpty()) {
            sb.append(delim);
            delim = LS;
            for (String className : applicationClassSet_) {
                sb.append("import ").append(className).append(";").append(LS);
            }
        }

        return sb.toString();
    }

    public boolean isEmpty() {
        return applicationClassSet_.isEmpty() && frameworkClassSet_.isEmpty()
                && javaClassSet_.isEmpty() && javaxClassSet_.isEmpty();
    }

    public void clear() {
        applicationClassSet_.clear();
        frameworkClassSet_.clear();
        javaClassSet_.clear();
        javaxClassSet_.clear();
    }

    public boolean containsClass(String className) {
        if (applicationClassSet_.contains(className)
                || frameworkClassSet_.contains(className)
                || javaClassSet_.contains(className)
                || javaxClassSet_.contains(className)) {
            return true;
        } else {
            return false;
        }
    }
}
