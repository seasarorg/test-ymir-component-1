package org.seasar.ymir.extension.zpt;

import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.zpt.tales.PathResolver;

public class AnalyzerPathResolver implements PathResolver {

    public boolean accept(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        return true;
    }

    public Object resolve(TemplateContext context,
            VariableResolver varResolver, Object obj, String child) {
        if (obj instanceof DescWrapper) {
            return ((DescWrapper) obj).get(child);
        } else {
            return null;
        }
    }
}
