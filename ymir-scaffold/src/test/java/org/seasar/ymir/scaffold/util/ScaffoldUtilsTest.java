package org.seasar.ymir.scaffold.util;

import junit.framework.TestCase;

import org.seasar.ymir.zpt.MutableTagElement;

import net.skirnir.freyja.Attribute;

public class ScaffoldUtilsTest extends TestCase {
    public void test_addClassAttribute() throws Exception {
        MutableTagElement element = MutableTagElement
                .newInstance("span", new Attribute[] { new Attribute("class",
                        "  hoe  fu&amp;ga  "), });

        ScaffoldUtils.addClassAttribute(element, "hoe", "fu&fu");

        assertEquals("hoe fu&ga fu&fu", element
                .getDefilteredAttributeValue("class"));
    }
}
