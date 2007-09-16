package org.seasar.ymir.extension.creator.action.impl;

import java.io.IOException;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.PathMetaData;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.impl.AsIsInputStreamFactory;
import org.seasar.ymir.response.SelfContainedResponse;
import org.seasar.ymir.response.VoidResponse;

public class DoEditTemplateAction extends DoTemplateActionBase {

    public DoEditTemplateAction(SourceCreator sourceCreator) {
        super(sourceCreator);
    }

    public Response act(Request request, PathMetaData pathMetaData) {
        Template template = getTemplate(request);
        if (template == null || !template.exists()) {
            return VoidResponse.INSTANCE;
        }

        try {
            return new SelfContainedResponse(new AsIsInputStreamFactory(
                    template.getInputStream()), "text/html;charset="
                    + template.getEncoding());
        } catch (IOException ex) {
            return VoidResponse.INSTANCE;
        }
    }
}
