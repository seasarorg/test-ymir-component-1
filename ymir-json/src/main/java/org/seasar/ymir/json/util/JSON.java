package org.seasar.ymir.json.util;

import org.seasar.ymir.Response;
import org.seasar.ymir.json.JSONResponseConstructor;
import org.seasar.ymir.response.SelfContainedResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSON {
    protected JSON() {
    }

    public static Response of(JSONObject obj) {
        return new SelfContainedResponse(obj.toString(),
                JSONResponseConstructor.CONTENT_TYPE);
    }

    public static Response of(JSONArray array) {
        return new SelfContainedResponse(array.toString(),
                JSONResponseConstructor.CONTENT_TYPE);
    }
}
