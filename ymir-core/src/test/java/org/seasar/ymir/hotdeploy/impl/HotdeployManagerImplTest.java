package org.seasar.ymir.hotdeploy.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.seasar.cms.pluggable.hotdeploy.PluggableHotdeployClassLoader;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.ymir.hotdeploy.HotdeployManager;
import org.seasar.ymir.hotdeploy.fitter.HotdeployFitter;
import org.seasar.ymir.hotdeploy.fitter.impl.CollectionFitter;
import org.seasar.ymir.hotdeploy.fitter.impl.ListFitter;
import org.seasar.ymir.hotdeploy.fitter.impl.MapFitter;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl.HotdeployFitterBag;
import org.seasar.ymir.mock.MockApplicationManager;
import org.seasar.ymir.render.Candidate;
import org.seasar.ymir.render.Selector;

import com.example.HoeHolder;
import com.example.HoeHolder2;
import com.example.IHoe;
import com.example.IHoe2;
import com.example.hotdeploy.CandidateImpl;

public class HotdeployManagerImplTest extends TestCase {
    private HotdeployManagerImpl target_;

    private IHoe hoe_;

    private Object fuga_;

    private IHoe2 hoe2_;

    private Candidate candidate_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        target_ = new HotdeployManagerImpl();
        target_.setApplicationManager(new MockApplicationManager());
        target_.setHotdeployFitters(getHotdeployFitters(target_));

        NamingConventionImpl namingConverntion = new NamingConventionImpl();
        namingConverntion.addRootPackageName("com.example.hotdeploy");

        final ClassLoader delegated = getClass().getClassLoader();
        PluggableHotdeployClassLoader cl = new PluggableHotdeployClassLoader(
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

        hoe2_ = (IHoe2) cl.loadClass("com.example.hotdeploy.Hoe2")
                .newInstance();

        candidate_ = (Candidate) cl.loadClass(
                "com.example.hotdeploy.CandidateImpl").newInstance();
        BeanUtils.setProperty(candidate_, "value", "1");
    }

    @SuppressWarnings("unchecked")
    private HotdeployFitter<?>[] getHotdeployFitters(
            final HotdeployManager hotdeployManager) {
        ListFitter listFitter = new ListFitter();
        listFitter.setHotdeployManager(hotdeployManager);
        MapFitter mapFitter = new MapFitter();
        mapFitter.setHotdeployManager(hotdeployManager);
        CollectionFitter collectionFitter = new CollectionFitter();
        collectionFitter.setHotdeployManager(hotdeployManager);
        HotdeployFitter<ArrayList> arrayListFitter = new HotdeployFitter<ArrayList>() {
            public Class<ArrayList> getTargetClass() {
                return ArrayList.class;
            }

            public void fitContent(ArrayList value) {
                for (ListIterator itr = value.listIterator(); itr.hasNext();) {
                    itr.set(hotdeployManager.fit(itr.next()));
                }
            }
        };
        return new HotdeployFitter<?>[] { listFitter, mapFitter,
            collectionFitter, arrayListFitter };
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
        assertNotSame(fuga_, hoe.getMap().get("key"));
        assertNotNull(hoe.getFuga());
        assertNotSame(hoe_.getFuga(), hoe.getFuga());
        assertEquals(1, hoe.getFugas().length);
        assertNotSame(fuga_, hoe.getFugas()[0]);
    }

    public void testFindFitter() throws Exception {
        HotdeployFitterBag target = new HotdeployManagerImpl.HotdeployFitterBag(
                getHotdeployFitters(target_));

        assertTrue("Fitterの検索は完全一致→アサイン可能（登録順）のように行なわれること", target.findFitter(
                ArrayList.class).getTargetClass() == ArrayList.class);
        assertTrue("Fitterの検索は完全一致→アサイン可能（登録順）のように行なわれること", target.findFitter(
                List.class).getTargetClass() == List.class);
    }

    public void testFit4_JIRA_YMIR_125_final宣言されているフィールドを持っていても処理できること()
            throws Exception {
        IHoe2 hoe2 = null;
        try {
            hoe2 = (IHoe2) target_.fit(hoe2_);
        } catch (Exception ex) {
            fail();
        }

        assertEquals(hoe2_.getId(), hoe2.getId());
        assertEquals(hoe2_.getStaticId(), hoe2.getStaticId());
    }

    public void testFit5_インタフェース型のプロパティで実装型がHotdeploy型の場合に置き換えられること()
            throws Exception {
        HoeHolder hoeHolder = new HoeHolder();
        hoeHolder.setHoe(hoe_);

        HoeHolder actual = (HoeHolder) target_.fit(hoeHolder);

        assertNotSame(hoe_, actual.getHoe());
    }

    public void testFit6_オブジェクトグラフがループしていてもStackOverflowにならないこと()
            throws Exception {
        HoeHolder2 hoeHolder2 = new HoeHolder2();
        hoeHolder2.setHoe(hoeHolder2);

        try {
            target_.fit(hoeHolder2);
        } catch (StackOverflowError ex) {
            fail();
        }
    }

    public void testFit7_ListがループしていてもStackOverflowにならないこと() throws Exception {
        List<Object> list = new ArrayList<Object>();
        list.add(list);

        try {
            target_.fit(list);
        } catch (StackOverflowError ex) {
            fail();
        }
    }

    public void testFit8_Selector() throws Exception {
        Selector selector = new Selector();
        selector.setCandidates(candidate_);
        selector.setSelectedValue("1");

        Selector actual = (Selector) target_.fit(selector);

        Candidate selectedCandidate = actual.getSelectedCandidate();
        assertTrue(selectedCandidate.isSelected());
    }
}
