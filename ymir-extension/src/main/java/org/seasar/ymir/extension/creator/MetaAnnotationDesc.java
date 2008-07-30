package org.seasar.ymir.extension.creator;

public interface MetaAnnotationDesc extends AnnotationDesc {
    String NAME_DEFAULTACTION_EXCEPTION = "defaultAction.exception";

    String getValue(String name);

    String[] getValues(String name);

    Class<?>[] getClassValues(String name);

    boolean hasValue(String name);
}
