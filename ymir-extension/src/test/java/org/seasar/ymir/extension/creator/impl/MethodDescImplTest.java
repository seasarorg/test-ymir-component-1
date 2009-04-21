package org.seasar.ymir.extension.creator.impl;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.ymir.Application;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.mock.MockApplication;

public class MethodDescImplTest extends TestCase {
    private DescPool pool_;

    @Override
    protected void setUp() throws Exception {
        pool_ = DescPool.newInstance(new SourceCreatorImpl() {
            @Override
            public Application getApplication() {
                return new MockApplication();
            }

            @Override
            protected ClassLoader getClassLoader() {
                return getClass().getClassLoader();
            }
        }, null);
    }

    public void testConstructor_Genericクラス() throws Exception {
        Method method = Hoe.class.getMethod("fuga", new Class[] { List.class });

        MethodDescImpl target = new MethodDescImpl(pool_, method);
        assertEquals("java.util.List<String>", target.getReturnTypeDesc()
                .getName());
        assertEquals("java.util.List<String>", target.getParameterDescs()[0]
                .getTypeDesc().getName());
    }

    public void testConstructor_通常のクラス() throws Exception {
        Method method = Hoe.class.getMethod("fuga2",
                new Class[] { String.class });

        MethodDescImpl target = new MethodDescImpl(pool_, method);
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
