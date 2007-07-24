package org.seasar.ymir.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.framework.container.hotdeploy.HotdeployClassLoader;
import org.seasar.framework.convention.impl.NamingConventionImpl;

import com.example.IHoe;

public class HotdeployUtilsTest extends TestCase {
    private IHoe hoe_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        NamingConventionImpl namingConverntion = new NamingConventionImpl();
        namingConverntion.addRootPackageName("com.example.hotdeploy");

        final ClassLoader delegated = getClass().getClassLoader();
        HotdeployClassLoader cl = new HotdeployClassLoader(
                new ClassLoader(null) {
                    @Override
                    protected synchronized Class<?> loadClass(String name,
                            boolean resolve) throws ClassNotFoundException {
                        if (name.startsWith("com.example.hotdeploy.")) {
                            throw new ClassNotFoundException();
                        }
                        return new ClassLoader(delegated) {
                            @Override
                            public synchronized Class<?> loadClass(String name,
                                    boolean resolve)
                                    throws ClassNotFoundException {
                                return super.loadClass(name, resolve);
                            }
                        }.loadClass(name, resolve);
                    }
                }, namingConverntion);
        hoe_ = (IHoe) cl.loadClass("com.example.hotdeploy.Hoe").newInstance();
        Class<?> fugaClass = cl.loadClass("com.example.hotdeploy.Fuga");
        Object fuga = fugaClass.newInstance();
        hoe_.setInt(10);
        hoe_.setFuga(fuga);
        List<Object> list = new ArrayList<Object>();
        list.add(fuga);
        hoe_.setList(list);
        Object[] fugas = (Object[]) Array.newInstance(fugaClass, 1);
        fugas[0] = fuga;
        hoe_.setFugas(fugas);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFit1() throws Exception {
        Object actual = HotdeployUtils.fit(null);

        assertNull(actual);
    }

    public void testFit2() throws Exception {
        Integer expected = Integer.valueOf(1);
        Object actual = HotdeployUtils.fit(expected);

        assertSame(expected, actual);
    }

    public void testFit3() throws Exception {
        Object actual = HotdeployUtils.fit(hoe_);

        assertNotSame(hoe_, actual);
        assertTrue(actual instanceof IHoe);
        IHoe hoe = (IHoe) actual;
        assertEquals(10, hoe.getInt());
        assertNotNull(hoe.getList());
        assertEquals(1, hoe.getList().size());
        assertNotSame(hoe_.getList().get(0), hoe.getList().get(0));
        assertNotNull(hoe.getFuga());
        assertNotSame(hoe_.getFuga(), hoe.getFuga());
        assertEquals(1, hoe.getFugas().length);
        assertNotSame(hoe_.getFugas()[0], hoe.getFugas()[0]);
    }
}
