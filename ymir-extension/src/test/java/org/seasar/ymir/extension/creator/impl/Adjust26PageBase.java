package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.scope.annotation.RequestParameter;

public class Adjust26PageBase extends AbstractAdjust26Page {
    @Meta(name = "formProperty", value = "adjust26Form")
    @RequestParameter
    public void setMessage(String message) {
        adjust26Form_.setMessage(message);
    }

    public String getMessage() {
        return adjust26Form_.getMessage();
    }
}
