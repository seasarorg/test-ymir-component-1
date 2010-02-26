package org.seasar.ymir.extension.zpt;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseCreator;
import org.seasar.ymir.response.SelfContainedResponse;
import org.seasar.ymir.util.ResponseUtils;
import org.seasar.ymir.zpt.YmirPathResolver;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.TemplateEvaluator;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.impl.TemplateEvaluatorImpl;
import net.skirnir.freyja.impl.VariableResolverImpl;
import net.skirnir.freyja.zpt.MetalTagEvaluator;
import net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator;

public class ZptResponseCreator implements ResponseCreator {
    private static final String TEMPLATE_PREFIX = "template/";

    private static final String TEMPLATE_SUFFIX = ".zpt";

    private ApplicationManager applicationManager_;

    private final TemplateEvaluator evaluator_ = new TemplateEvaluatorImpl(
            new MetalTagEvaluator(), new TalesExpressionEvaluator()
                    .addPathResolver(new YmirPathResolver()
                            .setNoteLocalizer(new NoteLocalizerImpl())));

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    public Response createResponse(String templateName,
            Map<String, Object> variableMap) {
        return createResponse(getClass().getResource(
                TEMPLATE_PREFIX + templateName + TEMPLATE_SUFFIX), variableMap);
    }

    public Response createResponse(URL templateURL,
            Map<String, Object> variableMap) {
        TemplateContext context = evaluator_.newContext();
        context.setProperty(TemplateContext.PROP_CONTENT_TYPE, "text/html");
        if (variableMap != null) {
            VariableResolver resolver = new VariableResolverImpl();
            for (Iterator<Map.Entry<String, Object>> itr = variableMap
                    .entrySet().iterator(); itr.hasNext();) {
                Map.Entry<String, Object> entry = itr.next();
                resolver.setVariable(entry.getKey(), entry.getValue());
            }
            context.setVariableResolver(resolver);
        }
        try {
            SelfContainedResponse response = new SelfContainedResponse(
                    evaluator_.evaluate(context, new InputStreamReader(
                            templateURL.openStream(), "UTF-8")),
                    "text/html; charset=" + getEncoding());
            ResponseUtils.setNoCache(response);
            return response;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    String getEncoding() {
        return applicationManager_.findContextApplication()
                .getTemplateEncoding();
    }
}
