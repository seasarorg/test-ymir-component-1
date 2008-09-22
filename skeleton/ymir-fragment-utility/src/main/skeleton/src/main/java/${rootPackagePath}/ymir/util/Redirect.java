package ${rootPackageName}.ymir.util;

import static ${rootPackageName}.ymir.util.PageUtils.SCHEME_REDIRECT;

public class Redirect {
    protected Redirect() {
    }

    public static String to(String path, String... params) {
        return PageUtils.transitTo(SCHEME_REDIRECT, path, false, params);
    }

    public static String toNonCached(String path, String... params) {
        return PageUtils.transitTo(SCHEME_REDIRECT, path, true, params);
    }
}
