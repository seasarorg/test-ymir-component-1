package org.seasar.ymir.extension.creator.impl;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

public class MethodDescImplTest extends TestCase {
    public void testConstructor_Genericクラス() throws Exception {
        Method method = Hoe.class.getMethod("fuga", new Class[] { List.class });

        MethodDescImpl target = new MethodDescImpl(null, method);
        assertEquals("java.util.List<String>", target.getReturnTypeDesc()
                .getName());
        assertEquals("java.util.List<String>", target.getParameterDescs()[0]
                .getTypeDesc().getName());
    }

    public void testConstructor_通常のクラス() throws Exception {
        Method method = Hoe.class.getMethod("fuga2",
                new Class[] { String.class });

        MethodDescImpl target = new MethodDescImpl(null, method);
        assertEquals("String", target.getReturnTypeDesc().getName());
        assertEquals("String", target.getParameterDescs()[0].getTypeDesc()
                .getName());
    }

    public static class Hoe {
        public List<String> fuga(List<String> list) {
            return list;
        }

        public String fuga2(String string) {
            return string;
        }
    }
}
