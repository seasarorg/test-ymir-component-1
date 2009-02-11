package org.seasar.ymir.zpt;

import org.seasar.ymir.Request;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.webapp.PageTypePrefixHandler;

public class YmirPageTypePrefixHandler extends PageTypePrefixHandler {
    private static final String CURRENT_PAGE = ".";

    @Override
    protected String filterPath(TemplateContext context,
            VariableResolver varResolver, String path) {
        if (CURRENT_PAGE.equals(path)) {
            Request request = (Request) varResolver.getVariable(context,
                    YmirVariableResolver.NAME_YMIRREQUEST);
            return request.getPath();
        } else {
            return super.filterPath(context, varResolver, path);
        }
    }
}
