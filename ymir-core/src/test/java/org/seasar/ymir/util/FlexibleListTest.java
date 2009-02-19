package org.seasar.ymir.util;

import junit.framework.TestCase;

public class FlexibleListTest extends TestCase {
    FlexibleList<String> target_ = new FlexibleList<String>();

    public void test() throws Exception {
        assertTrue(target_.isEmpty());

        assertEquals(0, target_.size());

        target_.add("0");
        target_.add("1");

        assertEquals(2, target_.size());
        int index = 0;
        assertEquals("0", target_.get(index++));
        assertEquals("1", target_.get(index++));

        target_.set(3, "3");

        assertEquals(4, target_.size());
        index = 0;
        assertEquals("0", target_.get(index++));
        assertEquals("1", target_.get(index++));
        assertNull(target_.get(index++));
        assertEquals("3", target_.get(index++));

        target_.add("4");

        assertEquals(5, target_.size());
        index = 0;
        assertEquals("0", target_.get(index++));
        assertEquals("1", target_.get(index++));
        assertNull(target_.get(index++));
        assertEquals("3", target_.get(index++));
        assertEquals("4", target_.get(index++));

        target_.add(2, "2");

        assertEquals(6, target_.size());
        index = 0;
        assertEquals("0", target_.get(index++));
        assertEquals("1", target_.get(index++));
        assertEquals("2", target_.get(index++));
        assertNull(target_.get(index++));
        assertEquals("3", target_.get(index++));
        assertEquals("4", target_.get(index++));
    }
}
