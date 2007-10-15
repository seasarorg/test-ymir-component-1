package org.seasar.ymir.extension.creator;

public interface MetaAnnotationDesc extends AnnotationDesc {
    String getValue(String name);

    String[] getValues(String name);

    boolean hasValue(String name);
}
