package ${rootPackageName}.ymir.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.seasar.ymir.Path;

public class PageUtils {
    public static final String SCHEME_FORWARD = "forward:";

    public static final String SCHEME_REDIRECT = "redirect:";

    public static final String SCHEME_PROCEED = "proceed:";

    protected PageUtils() {
    }

    public static String transitTo(String scheme, String path,
        boolean asNoCache, String... params) {
        if (params.length % 2 == 1) {
            throw new IllegalArgumentException(
                "Number of params must be even but: " + params.length);
        }

        Map<String, String[]> parameterMap = new LinkedHashMap<String, String[]>();
        for (int i = 0; i < params.length; i += 2) {
            addParameter(parameterMap, params[i], params[i + 1]);
        }
        return scheme
            + new Path(path, parameterMap).setAsNoCache(asNoCache).asString();
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
}
