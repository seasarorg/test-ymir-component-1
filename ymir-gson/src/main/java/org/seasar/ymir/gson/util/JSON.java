package org.seasar.ymir.gson.util;

import java.lang.reflect.Type;

import org.seasar.ymir.Response;
import org.seasar.ymir.response.SelfContainedResponse;

import com.google.gson.Gson;

public class JSON {
    public static final String CONTENT_TYPE = "application/json; charset=UTF-8";

    protected JSON() {
    }

    public static Response of(Object obj) {
        Gson gson = new Gson();
        return new SelfContainedResponse(gson.toJson(obj), CONTENT_TYPE);
    }

    public static Response of(Object obj, Type type) {
        return of(new Gson(), obj, type);
    }

    public static Response of(Gson gson, Object obj, Type type) {
        return new SelfContainedResponse(gson.toJson(obj, type), CONTENT_TYPE);
    }
}
