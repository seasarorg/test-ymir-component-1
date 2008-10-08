package org.seasar.ymir.constraint.impl;

import java.lang.reflect.AnnotatedElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Datetime;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

public class DatetimeConstraint extends AbstractConstraint<Datetime> {
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public void confirm(Object component, Request request, Datetime annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        String[] names = getParameterNames(request, getPropertyName(element),
                annotation.property());
        if (names.length == 0) {
            return;
        }

        String pattern = annotation.pattern();
        if (pattern.length() == 0) {
            pattern = annotation.value();
            if (pattern.length() == 0) {
                pattern = PATTERN;
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        Notes notes = new Notes();
        for (int i = 0; i < names.length; i++) {
            confirm(request, names[i], pattern, sdf, notes);
        }
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String name, String pattern,
            SimpleDateFormat sdf, Notes notes) {
        String key = PREFIX_MESSAGEKEY + "datetime";
        String[] values = request.getParameterValues(name);
        if (values == null) {
            return;
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i].length() == 0) {
                continue;
            }
            java.util.Date parsed;
            try {
                parsed = sdf.parse(values[i]);
            } catch (ParseException ex) {
                notes.add(name, new Note(key, name, pattern));
                continue;
            }
            if (!values[i].equals(sdf.format(parsed))) {
                notes.add(name, new Note(key, name, pattern));
                continue;
            }
        }
    }
}
