package org.seasar.ymir.extension.creator.action.impl;

import org.seasar.ymir.extension.zpt.ParameterRole;

public class ParameterDto {
    private String name_;

    private ParameterRole role_;

    public ParameterDto() {
    }

    public ParameterDto(String name, ParameterRole role) {
        name_ = name;
        role_ = role;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        name_ = name;
    }

    public ParameterRole getRole() {
        return role_;
    }

    public void setRole(ParameterRole role) {
        role_ = role;
    }
}
