package org.seasar.ymir.extension.creator.mapping.impl;

import org.seasar.ymir.extension.creator.mapping.ActionSelectorSeed;

public class ActionSelectorSeedImpl implements ActionSelectorSeed {
    private String pushedButtonName_;

    public ActionSelectorSeedImpl() {
    }

    public ActionSelectorSeedImpl(String pushedButtonName) {
        pushedButtonName_ = pushedButtonName;
    }

    public String getButtonName() {
        return pushedButtonName_;
    }

    public void setPushedButtonName(String pushedButtonName) {
        pushedButtonName_ = pushedButtonName;
    }
}
