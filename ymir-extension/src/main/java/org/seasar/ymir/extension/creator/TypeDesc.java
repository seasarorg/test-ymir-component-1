package org.seasar.ymir.extension.creator;

public interface TypeDesc extends Cloneable {
    String ARRAY_SUFFIX = "[]";

    Object clone();

    DescPool getDescPool();

    ClassDesc getComponentClassDesc();

    void setComponentClassDesc(ClassDesc classDesc);

    void setComponentClassDesc(Class<?> clazz);

    void setName(String typeName);

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

    void transcript(TypeDesc typeDesc);

    boolean isGeneric();

    String getName();

    String getCompleteName();

    String getShortName();

    String getShortClassName();

    String[] getImportClassNames();

    String getDefaultValue();

    String getInstanceName();
}
