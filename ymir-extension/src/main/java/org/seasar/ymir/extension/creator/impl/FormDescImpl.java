package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.FormDesc;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.mapping.impl.ActionSelectorSeedImpl;

public class FormDescImpl implements FormDesc {
    private SourceCreator sourceCreator_;

    private ClassDesc classDesc_;

    private ClassDesc dtoClassDesc_;

    private String name_;

    private String path_;

    private String method_;

    public FormDescImpl(SourceCreator sourceCreator, ClassDesc classDesc,
            ClassDesc dtoClassDesc, String name, String path, String method) {
        sourceCreator_ = sourceCreator;
        classDesc_ = classDesc;
        dtoClassDesc_ = dtoClassDesc;
        name_ = name;
        path_ = path;
        method_ = toUpperCase(method);
    }

    String toUpperCase(String string) {
        if (string == null) {
            return null;
        }
        return string.toUpperCase();
    }

    public void setActionMethodDesc(String parameterName) {
        classDesc_.setMethodDesc(sourceCreator_.getExtraPathMapping(path_,
                method_).newActionMethodDesc(
                new ActionSelectorSeedImpl(parameterName)));
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

    public void close() {
    }
}
