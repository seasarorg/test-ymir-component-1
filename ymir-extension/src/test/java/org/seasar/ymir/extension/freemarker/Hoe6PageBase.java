package org.seasar.ymir.extension.freemarker;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.scope.annotation.RequestParameter;

import com.example.dto.FormDto;

public class Hoe6PageBase {
    public static final String P_name = "name";

    @Meta(name = "property", value = "form")
    protected FormDto form_;

    @Meta(name = "formProperty", value = "form")
    public String getName() {
        return form_.getName();
    }

    @Meta(name = "formProperty", value = "form")
    @RequestParameter
    public void setName(String name) {
        form_.setName(name);
    }
}
