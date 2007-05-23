package org.seasar.ymir.constraint.impl;

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
import org.seasar.ymir.constraint.annotation.Matched;

public class MatchedConstraint extends AbstractConstraint<Matched> {
    @SuppressWarnings("unchecked")
    private Map<String, Pattern> compiledPatternMap_ = new LruHashMap(256);

    public void confirm(Object component, Request request, Matched annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        String[] names = add(annotation.property(), getPropertyName(element));
        if (names.length == 0) {
            throw new IllegalArgumentException(
                    "Please specify at least one property: " + element);
        }

        String pattern = annotation.pattern();
        if (pattern.length() == 0) {
            pattern = annotation.value();
        }
        if (pattern.length() == 0) {
            throw new IllegalArgumentException(
                    "Please specify either 'pattern' or 'value' property");
        }

        String keySuffix = annotation.messageKey();
        if (keySuffix.length() > 0) {
            keySuffix = "." + keySuffix;
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
        String key = PREFIX_MESSAGEKEY + "matched";
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
                notes.add(name,
                        new Note(key + keySuffix, new Object[] { name }));
            }
        }
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
