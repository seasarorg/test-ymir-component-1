package org.seasar.ymir.constraint.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.util.BeanUtils;

abstract public class AbstractConstraint<T extends Annotation> implements
        Constraint<T> {

    protected String getPropertyName(AnnotatedElement element) {
        if (!(element instanceof Method)) {
            return null;
        }
        return BeanUtils.toPropertyName(((Method) element).getName());
    }

    protected String[] add(String[] strings, String string) {
        if (string != null) {
            String[] newStrings = new String[strings.length + 1];
            System.arraycopy(strings, 0, newStrings, 0, strings.length);
            newStrings[strings.length] = string;
            return newStrings;
        } else {
            return strings;
        }
    }

    protected String[] add(String[] strings, String[] strings2, String string) {
        String[] newStrings = new String[strings.length + strings2.length
                + (string != null ? 1 : 0)];
        System.arraycopy(strings, 0, newStrings, 0, strings.length);
        System.arraycopy(strings2, 0, newStrings, strings.length,
                strings2.length);
        if (string != null) {
            newStrings[strings.length + strings2.length] = string;
        }
        return newStrings;
    }
}