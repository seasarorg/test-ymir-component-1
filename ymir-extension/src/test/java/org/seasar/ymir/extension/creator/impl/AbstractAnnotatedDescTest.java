package org.seasar.ymir.extension.creator.impl;

import java.util.Set;

import junit.framework.TestCase;

import org.seasar.ymir.extension.creator.DescPool;

public class AbstractAnnotatedDescTest extends TestCase {
    public void test_getImportClassNames() throws Exception {
        AbstractAnnotatedDesc target = new AbstractAnnotatedDesc() {
            @Override
            public void addDependingClassNamesTo(Set<String> set) {
                addDependingClassNamesTo0(set);
            }

            @Override
            public DescPool getDescPool() {
                return null;
            }

            @Override
            public void setTouchedClassNameSet(Set<String> set) {
            }
        };

        target.setAnnotationDesc(new AnnotationDescImpl("org.example.Noe",
                "value"));
        target.setAnnotationDesc(new AnnotationDescImpl("org.example.Hoe",
                "value"));
        target.setAnnotationDesc(new AnnotationDescImpl("org.example.Hoe",
                "value"));
        target.setAnnotationDesc(new AnnotationDescImpl("java.lang.String",
                "value"));

        String[] actual = target.getImportClassNames();

        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals("org.example.Hoe", actual[idx++]);
        assertEquals("org.example.Noe", actual[idx++]);
    }
}
