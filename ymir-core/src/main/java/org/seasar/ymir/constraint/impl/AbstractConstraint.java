package org.seasar.ymir.constraint.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.Constraint;
import org.seasar.ymir.util.BeanUtils;

abstract public class AbstractConstraint<T extends Annotation> implements
        Constraint<T> {

    private static final String PREFIX_REGEX = "#";

    protected String getPropertyName(AnnotatedElement element) {
        if (!(element instanceof Method)) {
            return null;
        }
        return BeanUtils.toPropertyName(((Method) element).getName());
    }

    protected String[] getParameterNames(Request request, String[] strings,
            String string) {
        return expand(add(strings, string), request);

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

    protected String[] getParameterNames(Request request, String[] strings,
            String[] strings2, String string) {
        return expand(add(strings, strings2, string), request);
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