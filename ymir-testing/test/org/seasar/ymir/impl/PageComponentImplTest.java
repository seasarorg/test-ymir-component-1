package org.seasar.ymir.impl;

import org.seasar.ymir.PageComponent;

import junit.framework.TestCase;

public class PageComponentImplTest extends TestCase {
    public void testUpdateDescendants() throws Exception {
        PageComponentImpl target = new PageComponentImpl(null, null,
                new PageComponent[] {});

        assertEquals(1, target.getDescendants().length);
    }

    public void testUpdateDescendants2() throws Exception {
        PageComponentImpl target = new PageComponentImpl("1", null,
                new PageComponent[] {
                    new PageComponentImpl("2", null,
                            new PageComponent[] { new PageComponentImpl("4"
                                    .intern(), null), }),
                    new PageComponentImpl("3", null, new PageComponent[] {
                        new PageComponentImpl("4".intern(), null),
                        new PageComponentImpl("5", null), }), });

        assertEquals(5, target.getDescendants().length);
        int idx = 0;
        assertEquals("1", target.getDescendants()[idx++].getPage());
        assertEquals("2", target.getDescendants()[idx++].getPage());
        assertEquals("4", target.getDescendants()[idx++].getPage());
        assertEquals("3", target.getDescendants()[idx++].getPage());
        assertEquals("5", target.getDescendants()[idx++].getPage());
    }
}
