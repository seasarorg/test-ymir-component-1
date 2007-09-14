package org.seasar.ymir.response;

abstract public class TransitionResponse extends ResponseBase {
    protected String path_;

    protected boolean parameterTakenOver_ = true;

    public TransitionResponse() {
    }

    public TransitionResponse(String path) {
        setPath(path);
    }

    public String getPath() {
        return path_;
    }

    public void setPath(String path) {
        path_ = path;
    }

    public boolean isParameterTakenOver() {
        return parameterTakenOver_;
    }

    public void setParameterTakenOver(boolean parameterTakenOver) {
        parameterTakenOver_ = parameterTakenOver;
    }
}
