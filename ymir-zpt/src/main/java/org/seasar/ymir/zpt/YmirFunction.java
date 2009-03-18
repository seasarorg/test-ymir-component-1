package org.seasar.ymir.zpt;

import net.skirnir.freyja.zpt.tales.Function;
import net.skirnir.freyja.zpt.tales.JavaTypePrefixHandler.InternalFunction;

public class YmirFunction {
    private static final String PREFIX_PARAM_SELF = YmirVariableResolver.NAME_PARAM_SELF
            + "/";

    private YmirFunction() {
    }

    public static String paramSelf(String name) {
        return Function.stringOf(InternalFunction
                .path(PREFIX_PARAM_SELF + name));
    }

    public static boolean peq(String name, Object value) {
        return Function.eq(paramSelf(name), value);
    }
}
