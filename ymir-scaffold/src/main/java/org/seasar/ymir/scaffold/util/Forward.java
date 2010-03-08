package org.seasar.ymir.scaffold.util;

import java.util.Map;

import org.seasar.ymir.Response;
import org.seasar.ymir.response.ForwardResponse;

public class Forward {
    protected Forward() {
    }

    public static Response to(String path) {
        return to(path, null);
    }

    public static Response to(String path, String param, Object... params) {
        Object[] pms;
        if (param != null) {
            pms = new Object[1 + params.length];
            pms[0] = param;
            System.arraycopy(params, 0, pms, 1, params.length);
        } else {
            pms = params;
        }

        return new ForwardResponse(PageUtils.constructPath(path, false, pms));
    }

    /**
     * @since 1.0.3-1
     */
    public static Response to(String path, Map<String, String[]> parameterMap) {
        return new ForwardResponse(PageUtils.constructPath(path, false,
                parameterMap));
    }

    public static Response to(Class<?> pageClass) {
        return to(pageClass, (String) null);
    }

    public static Response to(Class<?> pageClass, String param,
            Object... params) {
        Object[] pms;
        if (param != null) {
            pms = new Object[1 + params.length];
            pms[0] = param;
            System.arraycopy(params, 0, pms, 1, params.length);
        } else {
            pms = params;
        }

        return new ForwardResponse(PageUtils.constructPath(pageClass, false,
                pms));
    }

    /**
     * @since 1.0.3-1
     */
    public static Response to(Class<?> pageClass,
            Map<String, String[]> parameterMap) {
        return new ForwardResponse(PageUtils.constructPath(pageClass, false,
                parameterMap));
    }
}
