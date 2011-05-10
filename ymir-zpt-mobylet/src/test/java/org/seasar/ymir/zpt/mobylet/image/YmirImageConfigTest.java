package org.seasar.ymir.zpt.mobylet.image;

import junit.framework.TestCase;

public class YmirImageConfigTest extends TestCase {
    private YmirImageConfig target = new YmirImageConfig();

    public void test_getScaleServletPath() throws Exception {
        assertEquals("/hoe/imageScaleServlet", target.getScaleServletPath(
                "/hoe/imageScaleServlet", "/hoe"));

        assertEquals("/imageScaleServlet", target.getScaleServletPath(
                "/imageScaleServlet", ""));

        assertEquals("/hoe/imageScaleServlet", target.getScaleServletPath(
                "/imageScaleServlet", "/hoe"));

        assertEquals("/hoe/imageScaleServlet", target.getScaleServletPath(
                "/hoe/imageScaleServlet", "/fuga"));
    }
}
