package org.seasar.ymir.extension.zpt;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;

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
        AnalyzerContext analyzerContext = (AnalyzerContext) context;
        if (obj instanceof DescWrapper) {
            DescWrapper wrapper = (DescWrapper) obj;
            if (wrapper.isArray()) {
                // 配列にはプロパティを追加できないのでなにもしない。
                return null;
            }
            ClassDesc classDesc = wrapper.getClassDesc();
            int mode = (classDesc.isKindOf(ClassDesc.KIND_DTO) ? (PropertyDesc.READ | PropertyDesc.WRITE)
                    : PropertyDesc.READ);
            return new DescWrapper(analyzerContext, analyzerContext
                    .adjustPropertyType(classDesc.getName(), classDesc
                            .addProperty(child, mode)));
        } else {
            return null;
        }
    }
}
