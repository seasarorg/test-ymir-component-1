package org.seasar.ymir.constraint.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

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
}
