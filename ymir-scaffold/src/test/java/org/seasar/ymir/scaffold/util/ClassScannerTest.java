package org.seasar.ymir.scaffold.util;

import java.util.List;

import junit.framework.TestCase;

import org.seasar.ymir.scaffold.util.sub.Hoe1;
import org.seasar.ymir.scaffold.util.sub.sub.Hoe2;

public class ClassScannerTest extends TestCase {
    public void test_scan() throws Exception {
        List<Class<?>> actual = new ClassScanner().scan(
                "org.seasar.ymir.scaffold.util", ".+\\.Hoe.*");
        assertEquals(2, actual.size());
        int idx = 0;
        assertEquals(Hoe1.class, actual.get(idx++));
        assertEquals(Hoe2.class, actual.get(idx++));
    }
}
