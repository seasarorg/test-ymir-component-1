package org.seasar.ymir.hotdeploy.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.hotdeploy.HotdeployClassLoader;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.ymir.hotdeploy.fitter.HotdeployFitter;
import org.seasar.ymir.hotdeploy.fitter.impl.CollectionFitter;
import org.seasar.ymir.hotdeploy.fitter.impl.ListFitter;
import org.seasar.ymir.hotdeploy.fitter.impl.MapFitter;

import com.example.IHoe;

public class HotdeployManagerImplTest extends TestCase {
    private HotdeployManagerImpl target_;

    private IHoe hoe_;

    private Object fuga_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        target_ = new HotdeployManagerImpl();
        ListFitter listFitter = new ListFitter();
        listFitter.setHotdeployManager(target_);
        MapFitter mapFitter = new MapFitter();
        mapFitter.setHotdeployManager(target_);
        CollectionFitter collectionFitter = new CollectionFitter();
        collectionFitter.setHotdeployManager(target_);
        HotdeployFitter<ArrayList> arrayListFitter = new HotdeployFitter<ArrayList>() {
            public Class<ArrayList> getTargetClass() {
                return ArrayList.class;
            }

            @SuppressWarnings("unchecked")
            public ArrayList copy(ArrayList value) {
                for (ListIterator itr = value.listIterator(); itr.hasNext();) {
                    itr.set(target_.fit(itr.next()));
                }
                return value;
            }
        };
        target_.setHotdeployFitters(new HotdeployFitter<?>[] { listFitter,
            mapFitter, collectionFitter, arrayListFitter });

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
        fuga_ = fugaClass.newInstance();
        hoe_.setInt(10);
        hoe_.setFuga(fuga_);
        List<Object> list = new ArrayList<Object>();
        list.add(fuga_);
        hoe_.setList(list);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", fuga_);
        hoe_.setMap(map);
        Object[] fugas = (Object[]) Array.newInstance(fugaClass, 1);
        fugas[0] = fuga_;
        hoe_.setFugas(fugas);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFit1() throws Exception {
        Object actual = target_.fit(null);

        assertNull(actual);
    }

    public void testFit2() throws Exception {
        Integer expected = Integer.valueOf(1);
        Object actual = target_.fit(expected);

        assertSame(expected, actual);
    }

    public void testFit3() throws Exception {
        Object actual = target_.fit(hoe_);

        assertNotSame(hoe_, actual);
        assertTrue(actual instanceof IHoe);
        IHoe hoe = (IHoe) actual;
        assertEquals(10, hoe.getInt());
        assertNotNull(hoe.getList());
        assertEquals(1, hoe.getList().size());
        assertNotNull(hoe.getList().get(0));
        assertNotSame(hoe_.getList().get(0), fuga_);
        assertNotNull(hoe.getMap());
        assertNotNull(hoe.getMap().get("key"));
        assertNotSame(hoe_.getMap().get("key"), hoe.getMap().get("key"));
        assertNotNull(hoe.getFuga());
        assertNotSame(hoe_.getFuga(), hoe.getFuga());
        assertEquals(1, hoe.getFugas().length);
        assertNotSame(hoe_.getFugas()[0], hoe.getFugas()[0]);
    }

    public void testFindFitter() throws Exception {
        assertTrue("Fitterの検索は完全一致→アサイン可能（登録順）のように行なわれること", target_.findFitter(
                ArrayList.class).getTargetClass() == ArrayList.class);
        assertTrue("Fitterの検索は完全一致→アサイン可能（登録順）のように行なわれること", target_.findFitter(
                List.class).getTargetClass() == List.class);
    }
}
