package org.seasar.ymir.extension.zpt;

import static net.skirnir.freyja.zpt.tales.TalesExpressionEvaluator.TYPE_NOT;
import static net.skirnir.freyja.zpt.webapp.ServletTalesExpressionEvaluator.TYPE_PAGE;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.ClassCreationHintBag;
import org.seasar.ymir.extension.creator.SourceCreator;
import org.seasar.ymir.extension.creator.Template;
import org.seasar.ymir.extension.creator.TemplateAnalyzer;
import org.seasar.ymir.util.ServletUtils;

import net.skirnir.freyja.ExpressionEvaluator;
import net.skirnir.freyja.IllegalSyntaxException;
import net.skirnir.freyja.TagEvaluator;
import net.skirnir.freyja.TagEvaluatorWrapper;
import net.skirnir.freyja.TemplateEvaluator;
import net.skirnir.freyja.impl.TemplateEvaluatorImpl;
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

    synchronized Zpt getZpt() {
        if (zpt_ == null) {
            setZpt(new DefaultZpt());
        }
        return zpt_;
    }

    public void analyze(ServletContext servletContext,
            HttpServletRequest request, HttpServletResponse response,
            String path, String method, Map<String, ClassDesc> classDescMap,
            Template template, String className, ClassCreationHintBag hintBag,
            String[] ignoreVariables) {
        Zpt zpt = getZpt();

        path = zpt.getTemplatePathResolver().resolve(
                ServletUtils.normalizePath(path), request);
        AnalyzerContext context = (AnalyzerContext) evaluator_.newContext();
        context.setPath(path);
        context.setIgnoreVariables(ignoreVariables);
        zpt.buildTemplateContext(context, servletContext, request, response,
                Locale.getDefault(), path);
        context.setTemplateSet(new AnalyzerTemplateSet(evaluator_, context
                .getTemplateSet(), sourceCreator_, zpt
                .getTemplatePathResolver()));
        context.setSourceCreator(sourceCreator_);
        context.setMethod(method);
        context.setClassDescMap(classDescMap);
        context.setPageClassName(className);
        context.setUsingFreyjaRenderClasses(sourceCreator_
                .getSourceCreatorSetting().isUsingFreyjaRenderClasses());
        context.setPropertyTypeHintBag(hintBag);

        InputStream inputStream = null;
        try {
            inputStream = template.getInputStream();
            evaluator_.evaluate(context, new InputStreamReader(inputStream,
                    template.getEncoding()));
            context.close();
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof IllegalSyntaxException) {
                // TODO 文法エラーがあったのでスキップする旨
                // 通知しよう。
                ex.printStackTrace();
            } else {
                throw ex;
            }
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
    }

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
                                    .getTypePrefixHandler(TYPE_PAGE)));
            evaluator.addPathResolver(new AnalyzerPathResolver());
        }
        evaluator_ = new TemplateEvaluatorImpl(templateEvaluator
                .getTemplateParser(), tagEvaluator, expressionEvaluator,
                templateEvaluator.getTagRenderer());
    }

    AnalyzerTalTagEvaluator newAnalyzerTalTagEvaluator() {
        return new AnalyzerTalTagEvaluator();
    }
}
