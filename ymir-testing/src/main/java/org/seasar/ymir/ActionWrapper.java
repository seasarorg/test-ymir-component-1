package org.seasar.ymir;

public class ActionWrapper implements Action {
    protected Action action_;

    public ActionWrapper(Action action) {
        action_ = action;
    }

    public Action getAction() {
        return action_;
    }

    public MethodInvoker getMethodInvoker() {
        return action_.getMethodInvoker();
    }

    public String getName() {
        return action_.getName();
    }

    public Object getTarget() {
        return action_.getTarget();
    }

    public Object invoke() throws WrappingRuntimeException {
        return action_.invoke();
    }

    public boolean shouldInvoke() {
        return action_.shouldInvoke();
    }

    public Class<? extends Object> getReturnType() {
        return action_.getReturnType();
    }
}
