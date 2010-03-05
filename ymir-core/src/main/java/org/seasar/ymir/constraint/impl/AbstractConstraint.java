package org.seasar.ymir.constraint.impl;

import static org.seasar.ymir.constraint.Globals.PREFIX_REGEX;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.constraint.ConstraintUtils;
import org.seasar.ymir.constraint.Globals;
import org.seasar.ymir.util.BeanUtils;

abstract public class AbstractConstraint<T extends Annotation> implements
        Constraint<T> {
    private static final String SUFFIX_CONSTRAINT = "Constraint";

    protected String getConstraintKey() {
        String name = getClass().getSimpleName();
        if (name.endsWith(SUFFIX_CONSTRAINT)) {
            name = name
                    .substring(0, name.length() - SUFFIX_CONSTRAINT.length());
        }
        return Introspector.decapitalize(name);
    }

    protected String getFullMessageKey(String messageKey) {
        return ConstraintUtils
                .getFullMessageKey(getConstraintKey(), messageKey);
    }

    protected String getPropertyName(AnnotatedElement element) {
        if (!(element instanceof Method)) {
            return null;
        }
        return BeanUtils.toPropertyName(((Method) element).getName());
    }

    protected Class<?> getPropertyType(AnnotatedElement element) {
        if (!(element instanceof Method)) {
            return null;
        }
        return BeanUtils.getPropertyType((Method) element);
    }

    protected String[] getParameterNames(Request request, String string,
            String[] strings) {
        return expand(add(string, strings), request);

    }

    String[] expand(String[] names, Request request) {
        Set<String> expandedNameSet = new LinkedHashSet<String>();
        for (int i = 0; i < names.length; i++) {
            if (names[i].startsWith(PREFIX_REGEX)) {
                // 正規表現。
                Pattern pattern = Pattern.compile(names[i]
                        .substring(PREFIX_REGEX.length()));
                for (Iterator<String> itr = request.getParameterNames(); itr
                        .hasNext();) {
                    String name = itr.next();
                    if (pattern.matcher(name).matches()) {
                        expandedNameSet.add(name);
                    }
                }
            } else {
                expandedNameSet.add(names[i]);
            }
        }
        return expandedNameSet.toArray(new String[0]);
    }

    protected String[] add(String string, String[] strings) {
        if (string != null) {
            String[] newStrings = new String[strings.length + 1];
            newStrings[0] = string;
            System.arraycopy(strings, 0, newStrings, 1, strings.length);
            return newStrings;
        } else {
            return strings;
        }
    }

    protected String[] getParameterNames(Request request, String string,
            String[] strings, String[] strings2) {
        return expand(add(string, strings, strings2), request);
    }

    protected String[] add(String string, String[] strings, String[] strings2) {
        String[] newStrings = new String[strings.length + strings2.length
                + (string != null ? 1 : 0)];
        int offset = 0;
        if (string != null) {
            newStrings[offset++] = string;
        }
        System.arraycopy(strings, 0, newStrings, offset, strings.length);
        System.arraycopy(strings2, 0, newStrings, offset + strings.length,
                strings2.length);
        return newStrings;
    }
}