package org.seasar.ymir.impl;

import org.seasar.ymir.testing.YmirTestCase;

import com.example.web.HoePage;

public class YmirImplITest extends YmirTestCase {
    public void testGetPathOfPageClass() throws Exception {
        assertEquals("/hoe.html", getYmir().getPathOfPageClass(HoePage.class));
    }
}
