package org.seasar.ymir.extension.creator;

import java.util.Map;

import org.seasar.ymir.extension.creator.impl.SimpleClassDesc;

public interface TypeDesc extends Cloneable {
    String TYPE_VOID = "void";

    ClassDesc DEFAULT_CLASSDESC = new SimpleClassDesc(String.class.getName());

    Object clone();

    ClassDesc getComponentClassDesc();

    void setComponentClassDesc(ClassDesc classDesc);

    void setName(String typeName);

    void setName(String typeName, Map<String, ClassDesc> classDescMap);

    boolean isCollection();

    void setCollection(boolean collection);

    String getCollectionClassName();

    void setCollectionClassName(String collectionClassName);

    String getCollectionImplementationClassName();

    void setCollectionImplementationClassName(
            String collectionImplementationClassName);

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
