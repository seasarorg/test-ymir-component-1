package org.seasar.ymir.extension.creator.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.ymir.Application;
import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.impl.MetaAnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.MetasAnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.SourceCreatorImpl;
import org.seasar.ymir.extension.creator.impl.TypeDescImpl;
import org.seasar.ymir.mock.MockApplication;

public class DescUtilsTest extends TestCase {
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

    @SuppressWarnings("deprecation")
    public void testNewAnnotationDesc() throws Exception {
        AnnotationDesc[] ads = DescUtils
                .newAnnotationDescs(org.seasar.ymir.extension.creator.impl.Hoe2.class);
        assertEquals(1, ads.length);
        assertEquals(Deprecated.class.getName(), ads[0].getName());
    }

    public void test_transcript() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_, "java.util.List<String>");

        TypeDescImpl actual = new TypeDescImpl(pool_, "");
        DescUtils.transcript(actual, target);

        assertEquals("java.util.List<String>", actual.getName());
    }

    public void test_isCapableParameter() throws Exception {
        assertTrue(DescUtils.isCapableParameter("param_name1.value"));

        assertFalse(DescUtils.isCapableParameter("param[0]"));
    }

    public void test_setAnnotationDesc_Metaがある場合にMetaのマージをした後にMetaがなくなっていること()
            throws Exception {
        Map<String, AnnotationDesc> map = new HashMap<String, AnnotationDesc>();
        DescUtils.setAnnotationDesc(map, new MetaAnnotationDescImpl("name",
                new String[0]));
        DescUtils.setAnnotationDesc(map, new MetaAnnotationDescImpl("name2",
                new String[0]));

        assertNotNull(map.get(Metas.class.getName()));
        assertNull(map.get(Meta.class.getName()));
    }

    public void test_setAnnotationDesc_Metaがある場合にMetasのマージをした後にMetaがなくなっていること()
            throws Exception {
        Map<String, AnnotationDesc> map = new HashMap<String, AnnotationDesc>();
        DescUtils.setAnnotationDesc(map, new MetaAnnotationDescImpl("name",
                new String[0]));
        DescUtils.setAnnotationDesc(map, new MetasAnnotationDescImpl(
                new MetaAnnotationDescImpl("name", new String[0]),
                new MetaAnnotationDescImpl("name2", new String[0])));

        assertNotNull(map.get(Metas.class.getName()));
        assertNull(map.get(Meta.class.getName()));
    }
}
