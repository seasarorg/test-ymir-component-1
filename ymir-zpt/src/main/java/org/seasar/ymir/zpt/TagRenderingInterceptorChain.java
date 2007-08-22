package org.seasar.ymir.zpt;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TemplateContext;

public interface TagRenderingInterceptorChain {
    String render(TemplateContext context, String name, Attribute[] attributes,
            String body);
}
