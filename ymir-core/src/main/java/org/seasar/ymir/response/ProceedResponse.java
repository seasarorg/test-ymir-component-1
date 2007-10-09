package org.seasar.ymir.response;

import org.seasar.ymir.ResponseType;

public class ProceedResponse extends TransitionResponse {
    public ProceedResponse() {
    }

    public ProceedResponse(String path) {
        super(path);
    }

    public boolean isSubordinate() {
        return false;
    }

    @Override
    public String toString() {
        return "proceed:" + getPath();
    }

    public ResponseType getType() {
        return ResponseType.FORWARD;
    }
}
