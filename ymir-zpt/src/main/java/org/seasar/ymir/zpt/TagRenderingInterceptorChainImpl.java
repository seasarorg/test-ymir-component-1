package org.seasar.ymir.zpt;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TemplateContext;

public class TagRenderingInterceptorChainImpl implements
        TagRenderingInterceptorChain {
    private TagRenderingInterceptor tagRenderingInterceptor_;

    private TagRenderingInterceptorChain chain_;

    public TagRenderingInterceptorChainImpl(
            TagRenderingInterceptor tagRenderingInterceptor,
            TagRenderingInterceptorChain chain) {
        tagRenderingInterceptor_ = tagRenderingInterceptor;
        chain_ = chain;
    }

    public String render(TemplateContext context, String name,
            Attribute[] attributes, String body) {
        return tagRenderingInterceptor_.render(context, name, attributes, body,
                chain_);
    }
}
