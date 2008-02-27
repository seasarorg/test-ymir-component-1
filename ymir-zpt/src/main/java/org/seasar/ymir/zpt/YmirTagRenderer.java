package org.seasar.ymir.zpt;

import org.seasar.framework.container.S2Container;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.util.ContainerUtils;

import net.skirnir.freyja.Attribute;
import net.skirnir.freyja.TagRenderer;
import net.skirnir.freyja.TemplateContext;
import net.skirnir.freyja.impl.XHTMLTagRenderer;

public class YmirTagRenderer implements TagRenderer {
    private TagRenderer tagRenderer_;

    private TagRenderingInterceptorChain chain_;

    public YmirTagRenderer() {
        this(new XHTMLTagRenderer());
    }

    public YmirTagRenderer(XHTMLTagRenderer tagRenderer) {
        tagRenderer_ = tagRenderer;

        chain_ = newChain(gatherTagRenderingInterceptors(), 0, tagRenderer_);
    }

    TagRenderingInterceptor[] gatherTagRenderingInterceptors() {
        return (TagRenderingInterceptor[]) ContainerUtils.findAllComponents(
                getS2Container(), TagRenderingInterceptor.class);
    }

    private TagRenderingInterceptorChain newChain(
            TagRenderingInterceptor[] tagRenderingInterceptors, int idx,
            TagRenderer tagRenderer) {
        if (idx < tagRenderingInterceptors.length) {
            return new TagRenderingInterceptorChainImpl(
                    tagRenderingInterceptors[idx], newChain(
                            tagRenderingInterceptors, idx + 1, tagRenderer));
        } else {
            return new TagRendererAdapter();
        }
    }

    S2Container getS2Container() {
        return YmirContext.getYmir().getApplication().getS2Container();
    }

    public String render(TemplateContext context, String name,
            Attribute[] attributes, String body) {
        return chain_.render(context, name, attributes, body);
    }

    class TagRendererAdapter implements TagRenderingInterceptorChain {
        public String render(TemplateContext context, String name,
                Attribute[] attributes, String body) {
            return tagRenderer_.render(context, name, attributes, body);
        }
    }
}
