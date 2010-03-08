package org.seasar.ymir.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

public class BeanUtilsTest extends TestCase {
    public void testSpike() throws Exception {

        BeanInfo beanInfo = Introspector.getBeanInfo(Hoe.class);
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        Set<String> nameSet = new TreeSet<String>();
        for (int i = 0; i < pds.length; i++) {
            nameSet.add(pds[i].getName());
        }
        String[] actual = nameSet.toArray(new String[0]);

        assertEquals("プロパティ名は2文字目が大文字なら1文字目は変換されない", "URL", actual[0]);
        assertEquals("プロパティ名は2文字目が大文字なら1文字目は変換されない", "URl", actual[1]);
        assertEquals("class", actual[2]);
        assertEquals("長さが1のプロパティ名は小文字になる", "u", actual[3]);
        assertEquals("uRL", actual[4]);
        assertEquals("プロパティ名は2文字目が大文字なら1文字目は変換されない", "url", actual[5]);
    }

    public void testSpike2() throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(new Object() {
            @SuppressWarnings("unused")
            public boolean isHoe() {
                return false;
            }

            @SuppressWarnings("unused")
            public boolean getHoe() {
                return false;
            }

            @SuppressWarnings("unused")
            public void setHoe(boolean hoe) {
            }
        }.getClass());
        Method method = null;
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < pds.length; i++) {
            if ("hoe".equals(pds[i].getName())) {
                method = pds[i].getReadMethod();
            }
        }

        assertNotNull(method);
        assertEquals("isHoe", method.getName());
    }

    public static class Hoe {
        public void setURL(String url) {
        }

        public void setURl(String url) {
        }

        public void setUrl(String url) {
        }

        public void setU(String url) {
        }

        public void setuRL(String url) {
        }
    }

    public void testToPropertyName() throws Exception {
        assertNull(BeanUtils.toPropertyName(null));

        assertNull(BeanUtils.toPropertyName("is"));
        assertNull(BeanUtils.toPropertyName("get"));
        assertNull(BeanUtils.toPropertyName("set"));

        assertEquals("a", BeanUtils.toPropertyName("isA"));
        assertEquals("a", BeanUtils.toPropertyName("getA"));
        assertEquals("a", BeanUtils.toPropertyName("setA"));

        assertEquals("ABC", BeanUtils.toPropertyName("isABC"));
        assertEquals("ABC", BeanUtils.toPropertyName("getABC"));
        assertEquals("ABC", BeanUtils.toPropertyName("setABC"));

        assertEquals("abc", BeanUtils.toPropertyName("isAbc"));
        assertEquals("abc", BeanUtils.toPropertyName("getAbc"));
        assertEquals("abc", BeanUtils.toPropertyName("setAbc"));

        assertEquals("aBC", BeanUtils.toPropertyName("isaBC"));
        assertEquals("aBC", BeanUtils.toPropertyName("getaBC"));
        assertEquals("aBC", BeanUtils.toPropertyName("setaBC"));

        assertNull("a", BeanUtils.toPropertyName("injectA"));
        assertEquals("a", BeanUtils.toPropertyName("injectA", false));
    }

    public void testGetFirstSegment() throws Exception {
        assertNull(BeanUtils.getFirstSegment(null));

        assertEquals("aaa", BeanUtils.getFirstSegment("aaa"));
        assertEquals("aaa[0]", BeanUtils.getFirstSegment("aaa[0].bbb[1].ccc"));
        assertEquals("aaa(a.b.c)", BeanUtils
                .getFirstSegment("aaa(a.b.c).bbb(d.e.f)"));
    }

    public void testGetFirstSimpleSegment() throws Exception {
        assertNull(BeanUtils.getFirstSimpleSegment(null));

        assertEquals("aaa", BeanUtils.getFirstSimpleSegment("aaa"));
        assertEquals("aaa", BeanUtils
                .getFirstSimpleSegment("aaa[0].bbb[1].ccc"));
        assertEquals("aaa", BeanUtils
                .getFirstSimpleSegment("aaa(a.b.c).bbb(d.e.f)"));
    }

    public void testIsAmbiguousPropertyName() throws Exception {
        assertFalse(BeanUtils.isAmbiguousPropertyName(null));
        assertFalse(BeanUtils.isAmbiguousPropertyName(""));
        assertFalse(BeanUtils.isAmbiguousPropertyName("a"));
        assertFalse(BeanUtils.isAmbiguousPropertyName("abc"));
        assertFalse(BeanUtils.isAmbiguousPropertyName("URL"));
        assertTrue(BeanUtils.isAmbiguousPropertyName("A"));
        assertTrue(BeanUtils.isAmbiguousPropertyName("Abc"));
        assertTrue(BeanUtils.isAmbiguousPropertyName("uRL"));
    }

    public void testNormalizePropertyName() throws Exception {
        assertNull(BeanUtils.normalizePropertyName(null));
        assertEquals("", BeanUtils.normalizePropertyName(""));
        assertEquals("a", BeanUtils.normalizePropertyName("a"));
        assertEquals("abc", BeanUtils.normalizePropertyName("abc"));
        assertEquals("URL", BeanUtils.normalizePropertyName("URL"));
        assertEquals("a", BeanUtils.normalizePropertyName("A"));
        assertEquals("abc", BeanUtils.normalizePropertyName("Abc"));
        assertEquals("URL", BeanUtils.normalizePropertyName("uRL"));
    }

    public void test_capitalize() throws Exception {
        assertNull(BeanUtils.capitalize(null));
        assertEquals("", BeanUtils.capitalize(""));
        assertEquals("A", BeanUtils.capitalize("a"));
        assertEquals("Abc", BeanUtils.capitalize("abc"));
        assertEquals("URL", BeanUtils.capitalize("URL"));
        assertEquals("A", BeanUtils.capitalize("A"));
        assertEquals("Abc", BeanUtils.capitalize("Abc"));
        assertEquals("URL", BeanUtils.capitalize("uRL"));
    }
}
