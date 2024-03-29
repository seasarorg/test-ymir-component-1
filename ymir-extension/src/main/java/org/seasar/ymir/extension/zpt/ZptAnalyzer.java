package org.seasar.ymir.extension.zpt;

import static net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator.TYPE_JAVA;
import static net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator.TYPE_NOT;
import static net.skirnir.freyja.zpt.webapp.ServletTalesExpressionEvaluator.TYPE_PAGE;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.Request;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.TemplateAnalyzer;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.util.ServletUtils;
import org.seasar.ymir.zpt.YmirTalesExpressionEvaluator;
import org.seasar.ymir.zpt.YmirVariableResolver;

import net.skirnir.freyja.EvaluationRuntimeException;
import net.skirnir.freyja.ExpressionEvaluator;
import net.skirnir.freyja.TagEvaluator;
import net.skirnir.freyja.TagEvaluatorWrapper;
import net.skirnir.freyja.TemplateEvaluator;
import net.skirnir.freyja.impl.TemplateEvaluatorImpl;
import net.skirnir.freyja.webapp.FreyjaServlet;
import net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator;
import net.skirnir.freyja.zpt.webapp.PageTypePrefixHandler;

public class ZptAnalyzer implements TemplateAnalyzer {
    private TemplateEvaluator evaluator_;

    private SourceCreator sourceCreator_;

    private Zpt zpt_;

    @Binding(bindingType = BindingType.MAY)
    public void setZpt(Zpt zpt) {
        zpt_ = zpt;
        setTemplateEvaluator(zpt_.getTemplateEvaluator());
    }

    @Binding(bindingType = BindingType.MUST)
    public void setSourceCreator(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }

    /**
     * テンプレートの解析に用いるTemplateEvaluatorを設定します。
     *
     * @param templateEvaluator TemplateEvaluatorオブジェクト。
     * nullを指定することはできません。
     */
    @Binding(bindingType = BindingType.NONE)
    public void setTemplateEvaluator(TemplateEvaluator templateEvaluator) {
        TagEvaluator tagEvaluator = templateEvaluator.getTagEvaluator();
        if (tagEvaluator instanceof TagEvaluatorWrapper) {
            ((TagEvaluatorWrapper) tagEvaluator)
                    .setTagEvaluator(newAnalyzerTalTagEvaluator());
        } else {
            tagEvaluator = newAnalyzerTalTagEvaluator();
        }
        ExpressionEvaluator expressionEvaluator = templateEvaluator
                .getExpressionEvaluator();
        if (expressionEvaluator instanceof TalesExpressionEvaluator) {
            TalesExpressionEvaluator evaluator = ((TalesExpressionEvaluator) expressionEvaluator);
            evaluator.addTypePrefix(TYPE_NOT,
                    new AnalyzerNotTypePrefixHandler()).addTypePrefix(
                    TYPE_PAGE,
                    new AnalyzerPageTypePrefixHandler(
                            (PageTypePrefixHandler) evaluator
                                    .getTypePrefixHandler(TYPE_PAGE)))
                    .addTypePrefix(TYPE_JAVA,
                            new AnalyzerJavaTypePrefixHandler()).addTypePrefix(
                            YmirTalesExpressionEvaluator.TYPE_FORMAT,
                            new AnalyzerFormatTypePrefixHandler());
            evaluator.addPathResolver(new AnalyzerPathResolver());
        }
        evaluator_ = new TemplateEvaluatorImpl(templateEvaluator
                .getTemplateParser(), tagEvaluator, expressionEvaluator,
                templateEvaluator.getTagRenderer());
    }

    synchronized Zpt getZpt() {
        if (zpt_ == null) {
            setZpt(new DefaultZpt());
        }
        return zpt_;
    }

    AnalyzerTalTagEvaluator newAnalyzerTalTagEvaluator() {
        return new AnalyzerTalTagEvaluator();
    }

    public void analyze(ServletContext servletContext,
            HttpServletRequest request, HttpServletResponse response,
            Request ymirRequest, String path, HttpMethod method,
            Template template, String className, String[] ignoreVariables,
            DescPool pool, Notes warnings) {
        try {
            DescPool.setDefault(pool);
            Zpt zpt = getZpt();
            // 本来ZptYmir#processResponse()でやってくれることであるが、
            // 自動生成処理がprocessResponse()より前に動くためにここでprocessResponse()
            // と同じことをするようにしている。
            emulateZptYmirProcessResponse(request, response, ymirRequest);

            path = zpt.getTemplatePathResolver().resolve(
                    ServletUtils.normalizePath(path), request);
            AnalyzerContext context = (AnalyzerContext) evaluator_.newContext();
            context.setPath(path);
            context.setIgnoreVariables(ignoreVariables);
            zpt.buildTemplateContext(context, servletContext, request,
                    response, Locale.getDefault(), path);
            context.setTemplateSet(new AnalyzerTemplateSet(evaluator_, context
                    .getTemplateSet(), sourceCreator_, zpt
                    .getTemplatePathResolver()));
            context.setSourceCreator(sourceCreator_);
            context.setMethod(method);
            context.setPageClassName(className);
            context.setRepeatedPropertyGeneratedAsList(sourceCreator_
                    .getSourceCreatorSetting()
                    .isRepeatedPropertyGeneratedAsList());
            if (warnings != null) {
                context.setWarnings(warnings);
            }

            InputStream inputStream = null;
            try {
                inputStream = template.getInputStream();
                try {
                    evaluator_.evaluate(context, new InputStreamReader(
                            inputStream, template.getEncoding()));
                } catch (EvaluationRuntimeException ex) {
                    ex.setTemplateName(template.getName());
                    throw ex;
                }
                context.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ignore) {
                    }
                }
            }
        } finally {
            DescPool.setDefault(null);
        }
    }

    private void emulateZptYmirProcessResponse(HttpServletRequest request,
            HttpServletResponse response, Request ymirRequest) {
        String contentType = response.getContentType();
        if (contentType != null) {
            request.setAttribute(FreyjaServlet.ATTR_RESPONSECONTENTTYPE,
                    contentType);
        }

        request.setAttribute(FreyjaServlet.ATTR_VARIABLERESOLVER,
                new YmirVariableResolver(ymirRequest, request, sourceCreator_
                        .getApplication().getS2Container()));
    }
}
