package org.seasar.ymir.response;

import org.seasar.ymir.ResponseType;

public class ForwardResponse extends TransitionResponse {
    protected boolean parameterTakenOver_ = true;

    protected boolean methodTakenOver_ = true;

    public ForwardResponse() {
    }

    public ForwardResponse(String path) {
        super(path);
    }

    @Override
    public String toString() {
        return "forward:" + getPath();
    }

    public ResponseType getType() {
        return ResponseType.FORWARD;
    }

    public boolean isParameterTakenOver() {
        return parameterTakenOver_;
    }

    public void setParameterTakenOver(boolean parameterTakenOver) {
        parameterTakenOver_ = parameterTakenOver;
    }

    public boolean isMethodTakenOver() {
        return methodTakenOver_;
    }

    public void setMethodTakenOver(boolean methodTakenOver) {
        methodTakenOver_ = methodTakenOver;
    }
}
