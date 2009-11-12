package org.seasar.ymir.zpt.util;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TagElement;
import net.skirnir.freyja.TagEvaluatorUtils;
import net.skirnir.freyja.zpt.tales.Function;

public class DecorateUtils {
    private static final String PREFIX_REPLACE = "!";

    private DecorateUtils() {
    }

    public static String decorate(String defaultValue, String tkn) {
        boolean replaced = false;
        if (tkn.startsWith(PREFIX_REPLACE)) {
            replaced = true;
            tkn = tkn.substring(PREFIX_REPLACE.length());
        }
        if (defaultValue == null || replaced) {
            return tkn;
        } else {
            return Function.add(defaultValue, tkn);
        }
    }

    public static String getDefaultValue(TagElement element, String attrName) {
        Attribute[] attrs = element.getAttributes();
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i].getName().equals(attrName)) {
                return TagEvaluatorUtils.defilter(attrs[i].getValue());
            }
        }
        return null;
    }
}
