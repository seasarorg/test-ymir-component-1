package org.seasar.ymir.response;

import org.seasar.ymir.ResponseType;

public class VoidResponse extends ResponseBase {
    public static final VoidResponse INSTANCE = new VoidResponse();

    public ResponseType getType() {
        return ResponseType.VOID;
    }
}
