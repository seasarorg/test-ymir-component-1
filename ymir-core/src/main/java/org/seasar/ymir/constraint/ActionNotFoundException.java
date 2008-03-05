package org.seasar.ymir.constraint;

public class ActionNotFoundException extends PermissionDeniedException {
    private static final long serialVersionUID = 5885040967668460745L;

    private String actionName_;

    public ActionNotFoundException() {
    }

    public ActionNotFoundException(String actionName) {
        setActionName(actionName);
    }

    public String getActionName() {
        return actionName_;
    }

    public ActionNotFoundException setActionName(String actionName) {
        actionName_ = actionName;
        return this;
    }
}
