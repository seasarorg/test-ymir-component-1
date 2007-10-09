package org.seasar.ymir.response;

import org.seasar.ymir.ResponseType;

public class RedirectResponse extends TransitionResponse {
    public RedirectResponse() {
    }

    public RedirectResponse(String path) {
        super(path);
    }

    public boolean isSubordinate() {
        return false;
    }

    public String toString() {
        return "redirect:" + getPath();
    }

    public ResponseType getType() {
        return ResponseType.REDIRECT;
    }
}
