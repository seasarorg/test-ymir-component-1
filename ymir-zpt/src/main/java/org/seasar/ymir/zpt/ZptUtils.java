package org.seasar.ymir.zpt;

import org.seasar.ymir.Request;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;

public class ZptUtils {
    private ZptUtils() {
    }

    public static String getPageName(TemplateContext context,
            VariableResolver varResolver) {
        Request request = (Request) varResolver.getVariable(context,
                YmirVariableResolver.NAME_YMIRREQUEST);
        if (request == null) {
            return null;
        }

        String path = request.getPath();
        String name;
        int slash = path.lastIndexOf('/');
        if (slash < 0) {
            name = path;
        } else {
            name = path.substring(slash + 1);
        }
        int dot = name.lastIndexOf('.');
        if (dot >= 0) {
            name = name.substring(0, dot);
        }
        return name;
    }
}
