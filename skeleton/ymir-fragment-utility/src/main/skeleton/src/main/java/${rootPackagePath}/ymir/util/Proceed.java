package ${rootPackageName}.ymir.util;

import static ${rootPackageName}.ymir.util.PageUtils.SCHEME_PROCEED;

public class Proceed {
    protected Proceed() {
    }

    public static String to(String path, String... params) {
        return PageUtils.transitTo(SCHEME_PROCEED, path, false, params);
    }
}
