package org.seasar.ymir.util;

import java.util.Arrays;
import java.util.Comparator;

import org.seasar.ymir.interceptor.YmirProcessInterceptor;

public class YmirUtils {
    private static final Comparator<YmirProcessInterceptor> COMPARATOR_YMIRPROCESSINTERCEOTOR = new Comparator<YmirProcessInterceptor>() {
        public int compare(YmirProcessInterceptor o1, YmirProcessInterceptor o2) {
            return (int) Math.signum(o1.getPriority() - o2.getPriority());
        }
    };

    protected YmirUtils() {
    }

    public static void sortYmirProcessInterceptors(
            YmirProcessInterceptor[] ymirProcessInterceptors) {
        if (ymirProcessInterceptors != null) {
            Arrays.sort(ymirProcessInterceptors,
                    COMPARATOR_YMIRPROCESSINTERCEOTOR);
        }
    }
}
