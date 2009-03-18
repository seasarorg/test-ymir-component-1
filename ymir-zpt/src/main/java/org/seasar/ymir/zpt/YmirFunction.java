package org.seasar.ymir.zpt;

import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.message.Notes;

import net.skirnir.freyja.zpt.Default;
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

    public static boolean existsNotes() {
        Notes notes = (Notes) InternalFunction
                .resolve(RequestProcessor.ATTR_NOTES);
        if (notes == null) {
            return false;
        }
        return !notes.isEmpty();
    }

    public static boolean existsNotes(String category) {
        Notes notes = (Notes) InternalFunction
                .resolve(RequestProcessor.ATTR_NOTES);
        if (notes == null) {
            return false;
        }
        return notes.contains(category);
    }

    public static boolean peq(String name, Object value) {
        return Function.eq(paramSelf(name), value);
    }

    public static Object nvl(Object classWhenNoted) {
        return nvl(classWhenNoted, Default.instance);
    }

    public static Object nvl(Object classWhenNoted,
            Object classWhenNotNoted) {
        return existsNotes() ? classWhenNoted : classWhenNotNoted;
    }

    public static Object pnvl(String paramName,
            Object classWhenNoted) {
        return pnvl(paramName, classWhenNoted, Default.instance);
    }

    public static Object pnvl(String paramName,
            Object classWhenNoted, Object classWhenNotNoted) {
        return existsNotes(paramName) ? classWhenNoted : classWhenNotNoted;
    }
}
