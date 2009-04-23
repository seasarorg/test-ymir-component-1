package org.seasar.ymir.extension.creator.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.ymir.Application;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.DescPool;
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

    public void testGetNonGenericClassName() throws Exception {
        assertNull(DescUtils.getNonGenericClassName(null));

        assertEquals("java.lang.List", DescUtils
                .getNonGenericClassName("java.lang.List"));

        assertEquals("java.lang.List", DescUtils
                .getNonGenericClassName("java.lang.List<String>"));

        assertEquals("Iterator", DescUtils
                .getNonGenericClassName("Iterator<Map.Entry<String, String>>"));
    }

    @SuppressWarnings("deprecation")
    public void testNewAnnotationDesc() throws Exception {
        AnnotationDesc[] ads = DescUtils
                .newAnnotationDescs(org.seasar.ymir.extension.creator.impl.Hoe2.class);
        assertEquals(1, ads.length);
        assertEquals(Deprecated.class.getName(), ads[0].getName());
    }

    public void testToString() throws Exception {
        assertNull(DescUtils.toString(null));

        assertEquals("void", DescUtils.toString(Void.TYPE));

        assertEquals("int", DescUtils.toString(Integer.TYPE));
        assertEquals("int[]", DescUtils.toString(int[].class));
        assertEquals("int[][]", DescUtils.toString(int[][].class));

        assertEquals("java.lang.String", DescUtils.toString(String.class));
        assertEquals("java.lang.String[]", DescUtils.toString(String[].class));
        assertEquals("java.lang.String[][]", DescUtils
                .toString(String[][].class));

        Method method = DescUtilsBean.class.getMethod("getList", new Class[0]);
        assertEquals("java.util.List<java.lang.String>[]", DescUtils
                .toString(method.getGenericReturnType()));
    }

    public void test_getComponentPropertyTypeName() throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(DescUtilsBean.class);
        Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            map.put(pd.getName(), pd);
        }

        assertEquals("java.lang.String", DescUtils
                .getComponentPropertyTypeName(map.get("value1")));

        assertEquals("java.lang.String", DescUtils
                .getComponentPropertyTypeName(map.get("value2")));

        assertEquals("java.lang.String", DescUtils
                .getComponentPropertyTypeName(map.get("value3")));
    }

    public void test_getGenericPropertyTypeName() throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(DescUtilsBean.class);
        Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            map.put(pd.getName(), pd);
        }

        assertEquals("java.util.List<java.lang.String>[]", DescUtils
                .getGenericPropertyTypeName(map.get("list")));

        assertEquals("java.lang.Object", DescUtils
                .getGenericPropertyTypeName(map.get("value4")));

        assertEquals("java.util.List<java.lang.String>", DescUtils
                .getGenericPropertyTypeName(map.get("value5")));

        assertEquals("java.util.List<java.lang.String>[]", DescUtils
                .getGenericPropertyTypeName(map.get("value6")));
    }

    public void test_transcript() throws Exception {
        TypeDescImpl target = new TypeDescImpl(pool_, "java.util.List<String>");

        TypeDescImpl actual = new TypeDescImpl(pool_, "");
        DescUtils.transcript(actual, target);

        assertEquals("java.util.List<String>", actual.getName());
    }
}
