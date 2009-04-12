package org.seasar.ymir.render.html;

import junit.framework.TestCase;


public class AnchorTest extends TestCase
{
    public void testToString()
        throws Exception
    {
        Anchor target = new Anchor();
        target.setId("ID");
        target.setHref("HREF");
        target.setContent("CONTENT");

        assertEquals("<a id=\"ID\" href=\"HREF\">CONTENT</a>", target
            .toString());
    }
}
