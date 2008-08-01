package org.seasar.ymir.extension.creator.util;

import org.seasar.ymir.extension.creator.util.type.TypeToken;

public class DescUtils {
    private static final char BEGIN_TYPESPEC = '<';

    private static final char END_TYPESPEC = '>';

    private DescUtils() {
    }

    public static String getNonGenericClassName(String className) {
        if (className == null) {
            return null;
        }

        return new TypeToken(className).getBaseName();
    }
}
