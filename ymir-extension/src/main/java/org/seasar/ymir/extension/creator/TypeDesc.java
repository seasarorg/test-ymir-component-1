package org.seasar.ymir.extension.creator;

public interface TypeDesc extends Desc<TypeDesc> {
    String ARRAY_SUFFIX = "[]";

    DescPool getDescPool();

    ClassDesc getComponentClassDesc();

    void setComponentClassDesc(ClassDesc classDesc);

    void setComponentClassDesc(Class<?> clazz);

    String getComponentTypeName();

    boolean isCollection();

    void setCollection(boolean collection);

    String getCollectionClassName();

    void setCollectionClassName(String collectionClassName);

    void setCollectionClass(Class<?> collectionClass);

    String getCollectionImplementationClassName();

    void setCollectionImplementationClassName(
            String collectionImplementationClassName);

    void setCollectionImplementationClass(Class<?> collectionImplementationClass);

    boolean isExplicit();

    void setExplicit(boolean explicit);

    String getName();

    String getCompleteName();

    String getShortName();

    String getShortClassName();

    String getDefaultValue();

    String getInstanceName();
}
