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

    public Class<?> getTargetClass() {
        return action_.getTargetClass();
    }

    public final Object invoke() throws WrappingRuntimeException {
        return getMethodInvoker().invoke(getTarget());
    }

    public final boolean shouldInvoke() {
        return getMethodInvoker().shouldInvoke();
    }

    public final Class<? extends Object> getReturnType() {
        return getMethodInvoker().getReturnType();
    }
}
