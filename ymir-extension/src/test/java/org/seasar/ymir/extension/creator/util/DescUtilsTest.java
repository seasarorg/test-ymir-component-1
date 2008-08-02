package org.seasar.ymir.extension.creator.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.ymir.extension.creator.AnnotationDesc;

public class DescUtilsTest extends TestCase {
    public void testGetNonGenericClassName() throws Exception {
        assertNull(DescUtils.getNonGenericClassName(null));

        assertEquals("java.lang.List", DescUtils
                .getNonGenericClassName("java.lang.List"));

        assertEquals("java.lang.List", DescUtils
                .getNonGenericClassName("java.lang.List<String>"));

        assertEquals("Iterator", DescUtils
                .getNonGenericClassName("Iterator<Map.Entry<String, String>>"));
    }

    public void testGetGenericPropertyTypeName() throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(Hoe.class);
        PropertyDescriptor descriptor = null;
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            if ("list".equals(pd.getName())) {
                descriptor = pd;
                break;
            }
        }

        assertEquals("java.util.List<java.lang.String>[]", DescUtils
                .getGenericPropertyTypeName(descriptor));
    }

    public static class Hoe {
        private List<String>[] list_;

        public List<String>[] getList() {
            return list_;
        }

        public void setList(List<String>[] list) {
            list_ = list;
        }
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

        Method method = Hoe.class.getMethod("getList", new Class[0]);
        assertEquals("java.util.List<java.lang.String>[]", DescUtils
                .toString(method.getGenericReturnType()));
    }
}
