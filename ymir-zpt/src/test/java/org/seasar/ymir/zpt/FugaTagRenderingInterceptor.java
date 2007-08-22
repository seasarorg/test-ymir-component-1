package org.seasar.ymir.zpt;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TemplateContext;

public class FugaTagRenderingInterceptor implements TagRenderingInterceptor {
    public String[] getSpecialAttributePatternStrings() {
        return null;
    }

    public String[] getSpecialTagPatternStrings() {
        return new String[] { ".*" };
    }

    public String render(TemplateContext context, String name,
            Attribute[] attributes, String body,
            TagRenderingInterceptorChain chain) {
        return chain.render(context, name, attributes, "fuga");
    }
}
