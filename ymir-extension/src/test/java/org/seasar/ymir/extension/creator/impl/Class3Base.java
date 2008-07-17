package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.annotation.Meta;

import com.example.dto.FormDto;

public class Class3Base {
    @Meta(name = "property", value = "form")
    protected FormDto form_ = new FormDto();

    public FormDto getForm() {
        return form_;
    }
}
