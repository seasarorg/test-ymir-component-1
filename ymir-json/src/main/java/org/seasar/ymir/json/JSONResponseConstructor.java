package org.seasar.ymir.json;

import org.seasar.ymir.Response;
import org.seasar.ymir.response.SelfContainedResponse;
import org.seasar.ymir.response.VoidResponse;
import org.seasar.ymir.response.constructor.ResponseConstructor;

import net.sf.json.JSONObject;

public class JSONResponseConstructor implements ResponseConstructor<JSONObject> {
    public static final String CONTENT_TYPE = "application/json; charset=UTF-8";

    public Response constructResponse(Object page, JSONObject returnValue) {
        if (returnValue == null) {
            return VoidResponse.INSTANCE;
        } else {
            return new SelfContainedResponse(returnValue.toString(),
                    CONTENT_TYPE);
        }
    }

    public Class<JSONObject> getTargetClass() {
        return JSONObject.class;
    }
}
