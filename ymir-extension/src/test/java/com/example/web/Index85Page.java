package com.example.web;

import org.seasar.ymir.render.Selector;

import com.example.dto.Form85Dto;

public class Index85Page {
    private Form85Dto form85Dto_ = new Form85Dto();

    public Form85Dto getForm85Dto() {
        return form85Dto_;
    }

    public void setForm85Dto(Form85Dto form85Dto) {
        form85Dto_ = form85Dto;
    }

    public Selector getEntrySelector() {
        return form85Dto_.getEntrySelector();
    }

    public void _get() {
    }
}
