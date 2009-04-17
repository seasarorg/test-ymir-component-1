package org.seasar.ymir.extension.creator.impl;

import java.util.HashMap;
import java.util.Map;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.FormDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.mapping.impl.ActionSelectorSeedImpl;

public class FormDescImpl implements FormDesc {
    private SourceCreator sourceCreator_;

    private ClassDesc classDesc_;

    private ClassDesc dtoClassDesc_;

    private String name_;

    private String path_;

    private HttpMethod method_;

    private Map<String, ParameterMetaData> parameterMap_ = new HashMap<String, ParameterMetaData>();

    public FormDescImpl(SourceCreator sourceCreator, ClassDesc classDesc,
            ClassDesc dtoClassDesc, String name, String path, HttpMethod method) {
        sourceCreator_ = sourceCreator;
        classDesc_ = classDesc;
        dtoClassDesc_ = dtoClassDesc;
        name_ = name;
        path_ = path;
        method_ = method;
    }

    String toUpperCase(String string) {
        if (string == null) {
            return null;
        }
        return string.toUpperCase();
    }

    public void setActionMethodDesc(String parameterName) {
        MethodDesc methodDesc = sourceCreator_.newActionMethodDesc(path_,
                method_, new ActionSelectorSeedImpl(parameterName));
        classDesc_.setMethodDesc(methodDesc);
    }

    public String getActionPageClassName() {
        return classDesc_.getName();
    }

    public ClassDesc getClassDesc() {
        return classDesc_;
    }

    public ClassDesc getDtoClassDesc() {
        return dtoClassDesc_;
    }

    public String getName() {
        return name_;
    }

    public void addParameter(String parameterName, boolean radio) {
        ParameterMetaData metaData = parameterMap_.get(parameterName);
        if (metaData == null) {
            metaData = new ParameterMetaData(radio);
            parameterMap_.put(parameterName, metaData);
        } else {
            if (!metaData.isRadio() || !radio) {
                metaData.setMultiple(true);
            }
        }
    }

    public boolean isMultipleParameter(String parameterName) {
        ParameterMetaData metaData = parameterMap_.get(parameterName);
        if (metaData == null) {
            return false;
        } else {
            return metaData.isMultiple();
        }
    }

    public void close() {
    }

    static class ParameterMetaData {
        private boolean radio_;

        private boolean multiple_;

        ParameterMetaData(boolean radio) {
            radio_ = radio;
        }

        public boolean isRadio() {
            return radio_;
        }

        public void setRadio(boolean radio) {
            radio_ = radio;
        }

        public boolean isMultiple() {
            return multiple_;
        }

        public void setMultiple(boolean multiple) {
            multiple_ = multiple;
        }
    }
}
