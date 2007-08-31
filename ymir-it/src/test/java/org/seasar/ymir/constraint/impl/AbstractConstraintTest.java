package org.seasar.ymir.constraint.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

import junit.framework.TestCase;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.mock.MockRequest;

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
