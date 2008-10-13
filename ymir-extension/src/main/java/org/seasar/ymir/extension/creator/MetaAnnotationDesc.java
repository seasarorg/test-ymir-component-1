package org.seasar.ymir.extension.creator;

public interface MetaAnnotationDesc extends AnnotationDesc {
    String getMetaName();

    String getValue(String name);

    String[] getValues(String name);

    Class<?>[] getClassValues(String name);

    boolean hasValue(String name);
}
