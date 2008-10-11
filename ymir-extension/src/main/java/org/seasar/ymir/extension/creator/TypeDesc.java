package org.seasar.ymir.extension.creator;

import org.seasar.ymir.extension.creator.impl.SimpleClassDesc;

public interface TypeDesc extends Cloneable {
    String TYPE_VOID = "void";

    ClassDesc DEFAULT_CLASSDESC = new SimpleClassDesc(String.class.getName());

    Object clone();

    ClassDesc getClassDesc();

    void setClassDesc(ClassDesc classDesc);

    void setClassDesc(String className);

    boolean isArray();

    void setArray(boolean array);

    boolean isExplicit();

    void setExplicit(boolean explicit);

    void transcript(TypeDesc typeDesc);

    boolean isGeneric();

    String getName();

    String getShortName();

    String getShortClassName();

    String[] getImportClassNames();

    String getDefaultValue();

    String getInstanceName();
}
