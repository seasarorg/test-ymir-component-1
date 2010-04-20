package org.seasar.ymir.zpt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.FollowingURLResolver;
import org.seasar.ymir.Request;
import org.seasar.ymir.YmirContext;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.webapp.ServletVariableResolver;
import net.skirnir.freyja.zpt.webapp.PageTypePrefixHandler;

public class YmirPageTypePrefixHandler extends PageTypePrefixHandler {
    private FollowingURLResolver followingURLResolver_;

    public YmirPageTypePrefixHandler() {
        followingURLResolver_ = (FollowingURLResolver) getS2Container()
                .getComponent(FollowingURLResolver.class);
    }

    @Override
    public Object handle(TemplateContext context, VariableResolver varResolver,
            String expr) {
        String url = (String) super.handle(context, varResolver, expr);

        HttpServletRequest httpRequest = (HttpServletRequest) varResolver
                .getVariable(context, ServletVariableResolver.VAR_REQUEST);
        HttpServletResponse httpResponse = (HttpServletResponse) varResolver
                .getVariable(context, ServletVariableResolver.VAR_RESPONSE);
        Request request = (Request) varResolver.getVariable(context,
                YmirVariableResolver.NAME_YMIRREQUEST);
        if (followingURLResolver_.isResolved(url, httpRequest, httpResponse,
                request)) {
            return url;
        } else {
            return followingURLResolver_.resolveURL(url, httpRequest,
                    httpResponse, request);
        }
    }

    S2Container getS2Container() {
        return YmirContext.getYmir().getApplication().getS2Container();
    }
}
