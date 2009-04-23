package org.seasar.ymir.extension.creator.impl;

import java.util.List;
import java.util.TreeSet;

import org.seasar.ymir.annotation.Meta;

import junit.framework.TestCase;

public class MetaAnnotationDescImplTest extends TestCase {
    public void test_addDependingClassNamesTo() throws Exception {
        MetaAnnotationDescImpl target = new MetaAnnotationDescImpl("name",
                new Class<?>[] { String.class, List.class });
        TreeSet<String> set = new TreeSet<String>();

        target.addDependingClassNamesTo(set);

        String[] actual = set.toArray(new String[0]);
        assertEquals(3, actual.length);
        int idx = 0;
        assertEquals(String.class.getName(), actual[idx++]);
        assertEquals(List.class.getName(), actual[idx++]);
        assertEquals(Meta.class.getName(), actual[idx++]);
    }
}
