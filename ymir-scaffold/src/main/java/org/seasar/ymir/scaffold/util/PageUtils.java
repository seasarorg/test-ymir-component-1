package org.seasar.ymir.scaffold.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.Path;
import org.seasar.ymir.Request;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.response.scheme.impl.PassthroughStrategy;

public class PageUtils {
    public static final String SCHEME_FORWARD = "forward:";

    public static final String SCHEME_REDIRECT = "redirect:";

    public static final String SCHEME_PROCEED = "proceed:";

    public static final String SCHEME_PASSTHROUGH = PassthroughStrategy.SCHEME
            + ":";

    protected PageUtils() {
    }

    public static String transitTo(String scheme, String path,
            boolean asNoCache, Object... params) {
        return scheme + constructPath(path, asNoCache, params);
    }

    public static String constructPath(String path, boolean asNoCache,
            Object... params) {
        Map<String, String[]> parameterMap = new LinkedHashMap<String, String[]>();
        if (params != null) {
            for (int i = 0; i < params.length; i += 2) {
                if (params[i] == null) {
                    throw new IllegalClientCodeRuntimeException(
                            "parameter name must be non-null value, but params["
                                    + i + "] is null.");
                }

                Object value;
                if (i + 1 < params.length) {
                    value = params[i + 1];
                    if (value == null) {
                        throw new IllegalClientCodeRuntimeException(
                                "parameter value must be non-null value, but params["
                                        + (i + 1) + "] is null.");
                    }
                } else {
                    value = "";
                }
                List<String> valueList = new ArrayList<String>();
                if (value.getClass().isArray()) {
                    int length = Array.getLength(value);
                    for (int j = 0; j < length; j++) {
                        Object v = Array.get(value, j);
                        if (v == null) {
                            throw new IllegalClientCodeRuntimeException(
                                    "parameter value must be non-null value, but params["
                                            + i + "][" + j + "] is null.");
                        }
                        valueList.add(v.toString());
                    }
                } else {
                    valueList.add(value.toString());
                }

                addParameter(parameterMap, params[i].toString(), valueList
                        .toArray(new String[0]));
            }
        }
        return constructPath(path, asNoCache, parameterMap);
    }

    /**
     * @since 1.0.3-1
     */
    public static String constructPath(String path, boolean asNoCache,
            Map<String, String[]> parameterMap) {
        return newPath(path, parameterMap).setAsNoCache(asNoCache).asString();
    }

    /**
     * @since 1.0.3-0
     */
    public static Path newPath(String path, Map<String, String[]> parameterMap) {
        if (parameterMap == null) {
            parameterMap = new LinkedHashMap<String, String[]>();
        }
        return new Path(path, parameterMap, getCharacterEncoding());
    }

    /**
     * @since 1.0.2-0
     */
    public static String transitTo(String scheme, Class<?> pageClass,
            boolean asNoCache, Object... params) {
        return scheme + constructPath(pageClass, asNoCache, params);
    }

    public static String constructPath(Class<?> pageClass, boolean asNoCache,
            Object... params) {
        String path = YmirContext.getYmir().getPathOfPageClass(pageClass);
        if (path == null) {
            throw new IllegalClientCodeRuntimeException(
                    "Can't find path from page class ("
                            + pageClass.getName()
                            + "). You may need to add 'setReverseMapping' definition to PathMapping components in mapping.dicon");
        }

        return constructPath(path, asNoCache, params);
    }

    /**
     * @since 1.0.3-1
     */
    public static String constructPath(Class<?> pageClass, boolean asNoCache,
            Map<String, String[]> parameterMap) {
        String path = YmirContext.getYmir().getPathOfPageClass(pageClass);
        if (path == null) {
            throw new IllegalClientCodeRuntimeException(
                    "Can't find path from page class ("
                            + pageClass.getName()
                            + "). You may need to add 'setReverseMapping' definition to PathMapping components in mapping.dicon");
        }

        return constructPath(path, asNoCache, parameterMap);
    }

    public static Map<String, String[]> addParameter(
            Map<String, String[]> parameterMap, String name, String value) {
        if (value == null) {
            return parameterMap;
        }

        return addParameter(parameterMap, name, new String[] { value });
    }

    public static Map<String, String[]> addParameter(
            Map<String, String[]> parameterMap, String name, String[] value) {
        if (value == null || value.length == 0) {
            return parameterMap;
        }

        String[] v = parameterMap.get(name);
        if (v == null) {
            v = value;
        } else {
            String[] v2 = new String[v.length + value.length];
            System.arraycopy(v, 0, v2, 0, v.length);
            System.arraycopy(value, 0, v2, v.length, value.length);
            v = v2;
        }
        parameterMap.put(name, v);

        return parameterMap;
    }

    public static Map<String, String[]> setParameter(
            Map<String, String[]> parameterMap, String name, String value) {
        if (value == null) {
            parameterMap.remove(name);
            return parameterMap;
        } else {
            return setParameter(parameterMap, name, new String[] { value });
        }
    }

    public static Map<String, String[]> setParameter(
            Map<String, String[]> parameterMap, String name, String[] value) {
        if (value == null || value.length == 0) {
            parameterMap.remove(name);
        } else {
            parameterMap.put(name, value);
        }

        return parameterMap;
    }

    static String getCharacterEncoding() {
        return ((Request) YmirContext.getYmir().getApplication()
                .getS2Container().getComponent(Request.class))
                .getCharacterEncoding();
    }
}
