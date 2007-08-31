package org.seasar.ymir.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
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
}
