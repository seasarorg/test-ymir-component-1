package org.seasar.ymir.constraint.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;

import junit.framework.TestCase;

public class AbstractConstraintTest extends TestCase {

    public void test_getConstraintKey() throws Exception {
        HoeConstraint target = new HoeConstraint();
        assertEquals("hoe", target.getConstraintKey());
    }

    private class HoeConstraint extends AbstractConstraint<Annotation> {
        public void confirm(Object component, Request request,
                Annotation annotation, AnnotatedElement element)
                throws ConstraintViolatedException {
        }
    }
}
