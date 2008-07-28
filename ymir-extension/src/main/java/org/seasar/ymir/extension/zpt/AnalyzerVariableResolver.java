package org.seasar.ymir.extension.zpt;

import org.seasar.ymir.extension.creator.ClassDesc;

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
                && analyzerContext.isSystemVariable(name)) {
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
