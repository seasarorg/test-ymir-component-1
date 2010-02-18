package org.seasar.ymir.extension.creator;

import java.util.Collection;

public interface ImportDesc {
    void add(Collection<String> collection);

    void add(Class<?>... classes);

    void add(String... classNames);

    String[] getJavaClassNames();

    String[] getJavaxClassNames();

    String[] getFrameworkClassNames();

    String[] getApplicationClassNames();

    String getAsString();

    boolean isEmpty();

    void clear();
    
    boolean containsClass(String className);
}
