package org.seasar.ymir.extension.creator.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;

public class MetasAnnotationDescImplTest extends TestCase {
    public void test_addDependingClassNamesTo() throws Exception {
        MetasAnnotationDescImpl target = new MetasAnnotationDescImpl(
                new MetaAnnotationDesc[] {
                    new MetaAnnotationDescImpl("name1", new Class<?>[] {
                        String.class, List.class }),
                    new MetaAnnotationDescImpl("name1", new Class<?>[] {
                        String.class, Map.class }), });

        TreeSet<String> set = new TreeSet<String>();

        target.addDependingClassNamesTo(set);

        String[] actual = set.toArray(new String[0]);
        assertEquals(5, actual.length);
        int idx = 0;
        assertEquals(String.class.getName(), actual[idx++]);
        assertEquals(List.class.getName(), actual[idx++]);
        assertEquals(Map.class.getName(), actual[idx++]);
        assertEquals(Meta.class.getName(), actual[idx++]);
        assertEquals(Metas.class.getName(), actual[idx++]);
    }
}
