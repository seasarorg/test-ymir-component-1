package ${rootPackageName}.ymir.util;

import static ${rootPackageName}.ymir.util.PageUtils.SCHEME_FORWARD;

public class Forward {
    protected Forward() {
    }

    public static String to(String path, String... params) {
        return PageUtils.transitTo(SCHEME_FORWARD, path, false, params);
    }
}
