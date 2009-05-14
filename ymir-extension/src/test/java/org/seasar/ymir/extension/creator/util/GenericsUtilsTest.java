package org.seasar.ymir.extension.creator.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class GenericsUtilsTest extends TestCase {
    public void testGetNonGenericClassName() throws Exception {
        assertNull(GenericsUtils.getNonGenericClassName(null));

        assertEquals("java.lang.List", GenericsUtils
                .getNonGenericClassName("java.lang.List"));

        assertEquals("java.lang.List", GenericsUtils
                .getNonGenericClassName("java.lang.List<String>"));

        assertEquals("Iterator", GenericsUtils
                .getNonGenericClassName("Iterator<Map.Entry<String, String>>"));
    }

    public void testToString() throws Exception {
        assertNull(GenericsUtils.toString(null));

        assertEquals("void", GenericsUtils.toString(Void.TYPE));

        assertEquals("int", GenericsUtils.toString(Integer.TYPE));
        assertEquals("int[]", GenericsUtils.toString(int[].class));
        assertEquals("int[][]", GenericsUtils.toString(int[][].class));

        assertEquals("java.lang.String", GenericsUtils.toString(String.class));
        assertEquals("java.lang.String[]", GenericsUtils
                .toString(String[].class));
        assertEquals("java.lang.String[][]", GenericsUtils
                .toString(String[][].class));

        Method method = GenericsUtilsBean.class.getMethod("getList",
                new Class[0]);
        assertEquals("java.util.List<java.lang.String>[]", GenericsUtils
                .toString(method.getGenericReturnType()));
    }

    public void test_getComponentPropertyTypeName() throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(GenericsUtilsBean.class);
        Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            map.put(pd.getName(), pd);
        }

        assertEquals("java.lang.String", GenericsUtils
                .getComponentPropertyTypeName(map.get("value1")));

        assertEquals("java.lang.String", GenericsUtils
                .getComponentPropertyTypeName(map.get("value2")));

        assertEquals("java.lang.String", GenericsUtils
                .getComponentPropertyTypeName(map.get("value3")));
    }

    public void test_getGenericPropertyTypeName() throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(GenericsUtilsBean.class);
        Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            map.put(pd.getName(), pd);
        }

        assertEquals("java.util.List<java.lang.String>[]", GenericsUtils
                .getGenericPropertyTypeName(map.get("list")));

        assertEquals("java.lang.Object", GenericsUtils
                .getGenericPropertyTypeName(map.get("value4")));

        assertEquals("java.util.List<java.lang.String>", GenericsUtils
                .getGenericPropertyTypeName(map.get("value5")));

        assertEquals("java.util.List<java.lang.String>[]", GenericsUtils
                .getGenericPropertyTypeName(map.get("value6")));
    }

    public void test_getUpperBoundType() throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(GenericsUtilsBean.class);
        Map<String, Type> map = new HashMap<String, Type>();
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            map.put(pd.getName(), GenericsUtils.getGenericPropertyType(pd));
        }

        assertEquals(Object.class, GenericsUtils.getUpperBoundType(map
                .get("value7")));

        assertEquals(String.class, GenericsUtils.getUpperBoundType(map
                .get("value8")));
    }
}
