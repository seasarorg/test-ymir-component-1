package org.seasar.ymir.extension.creator.impl;

import java.util.List;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.seasar.ymir.Application;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.mock.MockApplication;

public class ParameterDescImplTest extends TestCase {
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

    public void test_addDependingClassNamesTo() throws Exception {
        ParameterDescImpl target = new ParameterDescImpl(pool_,
                new TypeDescImpl(pool_, "java.util.List<"
                        + ParameterDescImplTest.class.getName() + ">"));

        TreeSet<String> set = new TreeSet<String>();

        target.addDependingClassNamesTo(set);

        String[] actual = set.toArray(new String[0]);
        assertEquals(2, actual.length);
        int idx = 0;
        assertEquals(List.class.getName(), actual[idx++]);
        assertEquals(ParameterDescImplTest.class.getName(), actual[idx++]);
    }
}
