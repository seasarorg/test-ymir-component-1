package org.seasar.ymir.constraint.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;

public class AbstractConstraintTest extends TestCase {

    private AbstractConstraint target_ = new AbstractConstraint() {
        public void confirm(Object component, Request request,
                Annotation annotation, AnnotatedElement element)
                throws ConstraintViolatedException {
        }
    };

    public void testAdd() throws Exception {
        String[] actual = target_.add(new String[] { "a", "b" }, null);

        assertEquals(2, actual.length);
        assertEquals("a", actual[0]);
        assertEquals("b", actual[1]);
    }

    public void testAdd2() throws Exception {
        String[] actual = target_.add(new String[] { "a", "b" }, "c");

        assertEquals(3, actual.length);
        assertEquals("a", actual[0]);
        assertEquals("b", actual[1]);
        assertEquals("c", actual[2]);
    }

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
        assertNull(target_.toPropertyName(null));

        assertNull(target_.toPropertyName("is"));
        assertNull(target_.toPropertyName("get"));
        assertNull(target_.toPropertyName("set"));

        assertEquals("a", target_.toPropertyName("isA"));
        assertEquals("a", target_.toPropertyName("getA"));
        assertEquals("a", target_.toPropertyName("setA"));

        assertEquals("ABC", target_.toPropertyName("isABC"));
        assertEquals("ABC", target_.toPropertyName("getABC"));
        assertEquals("ABC", target_.toPropertyName("setABC"));

        assertEquals("abc", target_.toPropertyName("isAbc"));
        assertEquals("abc", target_.toPropertyName("getAbc"));
        assertEquals("abc", target_.toPropertyName("setAbc"));

        assertEquals("aBC", target_.toPropertyName("isaBC"));
        assertEquals("aBC", target_.toPropertyName("getaBC"));
        assertEquals("aBC", target_.toPropertyName("setaBC"));
    }
}
