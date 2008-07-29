package org.seasar.ymir.extension.creator.action.impl;

import java.io.IOException;

import org.seasar.kvasir.util.io.IORuntimeException;
import org.seasar.kvasir.util.io.IOUtils;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.response.VoidResponse;

public class DoUpdateTemplateAction extends DoEditTemplateAction {

    private static final String PARAM_BODY = "body";

    public DoUpdateTemplateAction(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, PathMetaData pathMetaData) {
        Template template = getTemplate(request);
        if (template == null || !template.exists()) {
            return VoidResponse.INSTANCE;
        }

        String body = request.getParameter(PARAM_BODY);
        if (body == null) {
            return VoidResponse.INSTANCE;
        }

        try {
            IOUtils.writeString(template.getOutputStream(), body, template
                    .getEncoding(), false);
        } catch (IOException ex) {
            throw new IORuntimeException("Can't write template: path="
                    + template.getPath(), ex);
        }

        return VoidResponse.INSTANCE;
    }
}
