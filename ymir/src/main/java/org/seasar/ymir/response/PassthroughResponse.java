package org.seasar.ymir.response;

import org.seasar.ymir.ResponseType;

public class PassthroughResponse extends ResponseBase {
    public static final PassthroughResponse INSTANCE = new PassthroughResponse();

    public String toString() {
        return "(passthrough)";
    }

    public ResponseType getType() {
        return ResponseType.PASSTHROUGH;
    }
}
