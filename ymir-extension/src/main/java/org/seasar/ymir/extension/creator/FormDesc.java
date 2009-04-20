package org.seasar.ymir.extension.creator;

public interface FormDesc {
    String getActionPageClassName();

    void setActionMethodDesc(String parameterName);

    ClassDesc getActionPageClassDesc();

    ClassDesc getDtoClassDesc();

    String getName();

    void addParameter(String parameterName, boolean radio);

    boolean isMultipleParameter(String parameterName);

    void close();
}
