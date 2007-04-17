package org.seasar.ymir.util;

import org.seasar.framework.container.hotdeploy.HotdeployUtil;
import org.seasar.ymir.YmirContext;

public class HotdeployUtils extends HotdeployUtil {
    protected HotdeployUtils() {
    }

    public static Object rebuildValue(Object value) {
        if (YmirContext.isUnderDevelopment()) {
            return rebuildValueInternal(value);
        }
        return value;
    }
}
