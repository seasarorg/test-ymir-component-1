package org.seasar.ymir.extension.creator;

public interface FormDesc {

    String getActionPageClassName();

    boolean isDispatchingByRequestParameter();

    void setActionMethodDesc(String parameterName);

    ClassDesc getClassDesc();

    ClassDesc getDtoClassDesc();

    String getName();
}
