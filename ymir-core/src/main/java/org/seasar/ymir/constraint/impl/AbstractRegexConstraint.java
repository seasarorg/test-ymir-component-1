package org.seasar.ymir.constraint.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.framework.util.LruHashMap;
import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;

abstract public class AbstractRegexConstraint<T extends Annotation> extends
        AbstractConstraint<T> {
    @SuppressWarnings("unchecked")
    private Map<String, Pattern> compiledPatternMap_ = new LruHashMap(256);

    abstract protected String getConstraintKey();

    protected void confirm(Request request, String[] property, String pattern,
            String messageKey, AnnotatedElement element)
            throws ConstraintViolatedException {
        String[] names = add(property, getPropertyName(element));
        if (names.length == 0) {
            throw new IllegalArgumentException(
                    "Please specify at least one property: " + element);
        }

        if (pattern.length() == 0) {
            throw new IllegalArgumentException(
                    "Please specify either 'pattern' or 'value' property");
        }

        String keySuffix;
        if (messageKey == null || messageKey.length() == 0) {
            keySuffix = "";
        } else {
            keySuffix = "." + messageKey;
        }

        Notes notes = new Notes();
        for (int i = 0; i < names.length; i++) {
            confirm(request, names[i], keySuffix, pattern, notes);
        }
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String name, String keySuffix,
            String pattern, Notes notes) {
        Pattern compiled = getCompiledPattern(pattern);
        String key = PREFIX_MESSAGEKEY + getConstraintKey() + keySuffix;
        String[] values = request.getParameterValues(name);
        if (values == null) {
            return;
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() == 0) {
                continue;
            }
            Matcher matcher = compiled.matcher(values[i]);
            if (!matcher.matches()) {
                notes
                        .add(name, newNote(request, name, key, pattern,
                                values[i]));
            }
        }
    }

    protected Note newNote(Request request, String name, String key,
            String pattern, String value) {
        return new Note(key, new Object[] { name });
    }

    Pattern getCompiledPattern(String pattern) {
        Pattern compiled = compiledPatternMap_.get(pattern);
        if (compiled == null) {
            compiled = Pattern.compile(pattern);
            synchronized (compiledPatternMap_) {
                compiledPatternMap_.put(pattern, compiled);
            }
        }
        return compiled;
    }
}
