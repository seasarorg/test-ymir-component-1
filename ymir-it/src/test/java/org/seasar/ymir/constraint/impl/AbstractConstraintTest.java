package org.seasar.ymir.constraint.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

import junit.framework.TestCase;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.mock.MockRequest;

public class AbstractConstraintTest extends TestCase {
    @SuppressWarnings("unchecked")
    private AbstractConstraint target_ = new AbstractConstraint() {
        public void confirm(Object component, Request request,
                Annotation annotation, AnnotatedElement element)
                throws ConstraintViolatedException {
        }
    };

    public void testAdd() throws Exception {
        String[] actual = target_.add(null, new String[] { "a", "b" });

        assertEquals(2, actual.length);
        assertEquals("a", actual[0]);
        assertEquals("b", actual[1]);
    }

    public void testAdd2() throws Exception {
        String[] actual = target_.add("a", new String[] { "b", "c" });

        assertEquals(3, actual.length);
        assertEquals("a", actual[0]);
        assertEquals("b", actual[1]);
        assertEquals("c", actual[2]);
    }

    public void testAdd3() throws Exception {
        String[] actual = target_.add(null, new String[] { "a", "b" },
                new String[] { "c", "d" });

        assertEquals(4, actual.length);
        int idx = 0;
        assertEquals("a", actual[idx++]);
        assertEquals("b", actual[idx++]);
        assertEquals("c", actual[idx++]);
        assertEquals("d", actual[idx++]);
    }

    public void testAdd4() throws Exception {
        String[] actual = target_.add("a", new String[] { "b", "c" },
                new String[] { "d", "e" });

        assertEquals(5, actual.length);
        int idx = 0;
        assertEquals("a", actual[idx++]);
        assertEquals("b", actual[idx++]);
        assertEquals("c", actual[idx++]);
        assertEquals("d", actual[idx++]);
        assertEquals("e", actual[idx++]);
    }

    public void testExpand() throws Exception {
        String[] actual = target_.expand(new String[] { "hoehoe",
            "#values\\[\\d+\\]\\.value" }, new MockRequest().setParameter(
                "value", "a").setParameter("values[0].value", "b")
                .setParameter("values[1].value", "c"));
        Arrays.sort(actual);

        assertEquals(3, actual.length);
        assertEquals("hoehoe", actual[0]);
        assertEquals("values[0].value", actual[1]);
        assertEquals("values[1].value", actual[2]);
    }
}
