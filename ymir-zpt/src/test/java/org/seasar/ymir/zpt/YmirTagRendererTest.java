package org.seasar.ymir.zpt;

import net.skirnir.freyja.Attribute;

import junit.framework.TestCase;

public class YmirTagRendererTest extends TestCase {
    public void testRender() throws Exception {
        YmirTagRenderer target = new YmirTagRenderer() {
            @Override
            TagRenderingInterceptor[] gatherTagRenderingInterceptors() {
                return new TagRenderingInterceptor[] {
                    new HoeTagRenderingInterceptor(),
                    new FugaTagRenderingInterceptor() };
            }
        };

        String actual = target.render(null, "name",
                new Attribute[] { new Attribute("attr1", "value1", "\""), },
                "BODY");

        assertEquals("<hoe attr1=\"value1\">fuga</hoe>", actual);
    }
}
