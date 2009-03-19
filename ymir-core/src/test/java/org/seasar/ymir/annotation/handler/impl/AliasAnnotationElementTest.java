package org.seasar.ymir.annotation.handler.impl;

import junit.framework.TestCase;

import org.seasar.ymir.annotation.handler.AnnotationElement;
import org.seasar.ymir.impl.Hoe;
import org.seasar.ymir.impl.HoeAlias;

public class AliasAnnotationElementTest extends TestCase {
    private AliasAnnotationElement target_ = new AliasAnnotationElement();

    @HoeAlias(value2 = "value2_specified")
    public void testExpand() throws Exception {
        target_.expand(getClass().getMethod("testExpand", new Class[0])
                .getAnnotation(HoeAlias.class));
        AnnotationElement actual = target_.getExpandedElement();

        assertEquals("固定値が上書きされていること", "value1_overwritten", ((Hoe) actual
                .getAnnotation()).value1());
        assertEquals("可変値が上書きされていること", "value2_specified", ((Hoe) actual
                .getAnnotation()).value2());
        assertEquals("指定していない値についてはもともとのアノテーションのデフォルト値が使われること", "value3",
                ((Hoe) actual.getAnnotation()).value3());
    }

    @Alia
    public void testExpand2_ElementAliasが正しく機能すること() throws Exception {
        target_.expand(getClass().getMethod(
                "testExpand2_ElementAliasが正しく機能すること", new Class[0])
                .getAnnotation(Alia.class));
        AnnotationElement actual = target_.getExpandedElement();

        assertEquals("orig1", ((Orig) actual.getAnnotation()).name1());
        assertEquals("alia1", ((Orig) actual.getAnnotation()).name2());
        assertEquals("alia5", ((Orig) actual.getAnnotation()).name3());
        assertEquals("alia4", ((Orig) actual.getAnnotation()).name4());
    }
}
