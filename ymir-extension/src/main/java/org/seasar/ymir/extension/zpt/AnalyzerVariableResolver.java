package org.seasar.ymir.extension.zpt;

import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.zpt.YmirVariableResolver;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;

public class AnalyzerVariableResolver implements VariableResolver {

    private VariableResolver delegated_;

    public AnalyzerVariableResolver(VariableResolver delegated) {
        delegated_ = delegated;
    }

    public boolean containsVariable(String name) {
        return delegated_.containsVariable(name);
    }

    public Object getVariable(TemplateContext context, String name) {
        AnalyzerContext analyzerContext = (AnalyzerContext) context;

        if (analyzerContext != null
                && !analyzerContext.shouldIgnoreVariable(name)
                && isPageSystemVariable(name)) {
            ClassDesc classDesc = analyzerContext
                    .getTemporaryClassDesc(analyzerContext
                            .fromPropertyNameToClassName(analyzerContext
                                    .getPageClassDesc(), name));
            analyzerContext.setUsedAsVariable(classDesc.getName());
            return new DescWrapper(analyzerContext, classDesc);
        } else {
            return delegated_.getVariable(context, name);
        }
    }

    boolean isPageSystemVariable(String name) {
        return YmirVariableResolver.NAME_PARAM_SELF.equals(name)
                || RequestProcessor.ATTR_SELF.equals(name);
    }

    public Entry getVariableEntry(TemplateContext context, String name) {
        return delegated_.getVariableEntry(context, name);
    }

    public String[] getVariableNames() {
        return delegated_.getVariableNames();
    }

    public void removeVariable(String name) {
        delegated_.removeVariable(name);
    }

    public void setVariable(String name, Object value) {
        delegated_.setVariable(name, value);
    }
}
