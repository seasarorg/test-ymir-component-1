package org.seasar.ymir.response;

import org.seasar.ymir.ResponseType;

public class PassthroughResponse extends ResponseBase {
    public String toString() {
        return "(passthrough)";
    }

    public ResponseType getType() {
        return ResponseType.PASSTHROUGH;
    }

    public boolean isSubordinate() {
        return true;
    }
}
