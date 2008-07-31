package org.seasar.ymir.constraint.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.util.map.LruHashMap;

abstract public class AbstractRegexConstraint<T extends Annotation> extends
        AbstractConstraint<T> {
    @SuppressWarnings("unchecked")
    private Map<String, Pattern> compiledPatternMap_ = new LruHashMap(256);

    abstract protected String getConstraintKey();

    protected void confirm(Request request, T annotation,
            AnnotatedElement element, String[] property, String pattern,
            String messageKey) throws ConstraintViolatedException {
        String[] names = getParameterNames(request, getPropertyName(element),
                property);
        if (names.length == 0) {
            return;
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
            confirm(request, annotation, names[i], keySuffix, pattern, notes);
        }
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, T annotation, String name, String keySuffix,
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
            confirm(request, annotation, name, key, pattern, notes, values[i],
                    compiled.matcher(values[i]));
        }
    }

    protected boolean confirm(Request request, T annotation, String name,
            String key, String pattern, Notes notes, String value,
            Matcher matcher) {
        if (matcher.matches()) {
            return true;
        } else {
            notes.add(name, newNote(request, name, key, pattern, value));
            return false;
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
