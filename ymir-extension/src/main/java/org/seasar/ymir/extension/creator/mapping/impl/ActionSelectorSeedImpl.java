package org.seasar.ymir.extension.creator.mapping.impl;

import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.mapping.ActionSelectorSeed;

public class ActionSelectorSeedImpl implements ActionSelectorSeed {
    private String actionKey_ = Globals.ACTIONKEY_DEFAULT;

    public ActionSelectorSeedImpl() {
    }

    public ActionSelectorSeedImpl(String actionKey) {
        setActionKey(actionKey);
    }

    public String getActionKey() {
        return actionKey_;
    }

    public void setActionKey(String actionKey) {
        if (actionKey == null) {
            actionKey = Globals.ACTIONKEY_DEFAULT;
        }
        actionKey_ = actionKey;
    }
}
