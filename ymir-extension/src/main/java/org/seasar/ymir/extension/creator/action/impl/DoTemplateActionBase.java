package org.seasar.ymir.extension.creator.action.impl;

import javax.servlet.http.HttpServletRequest;

import org.seasar.ymir.Request;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.action.UpdateAction;

abstract public class DoTemplateActionBase extends AbstractAction implements
        UpdateAction {

    private static final String PARAM_PATH = "path";

    public DoTemplateActionBase(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    protected Template getTemplate(Request request) {
        String path = getPath(request);
        if (path == null) {
            return null;
        } else {
            return getSourceCreator().getTemplate(path);
        }
    }

    String getPath(Request request) {
        String path = request.getParameter(PARAM_PATH);
        if (path == null) {
            return null;
        }
        int question = path.indexOf('?');
        if (question >= 0) {
            path = path.substring(0, question);
        }

        HttpServletRequest httpRequest = getSourceCreator()
                .getHttpServletRequest();
        String requestURL = httpRequest.getRequestURL().toString();
        String webappRootURL = requestURL.substring(0, requestURL.length()
                - httpRequest.getRequestURI().length())
                + request.getContextPath();
        if (!path.startsWith(webappRootURL)) {
            return null;
        } else {
            return path.substring(webappRootURL.length());
        }
    }
}
