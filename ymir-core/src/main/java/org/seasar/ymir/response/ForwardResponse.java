package org.seasar.ymir.response;

import org.seasar.ymir.ResponseType;

public class ForwardResponse extends TransitionResponse {
    public ForwardResponse() {
    }

    public ForwardResponse(String path) {
        super(path);
    }

    public String toString() {
        return "forward:" + getPath();
    }

    public ResponseType getType() {
        return ResponseType.FORWARD;
    }
}
