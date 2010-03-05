package org.seasar.ymir.constraint.impl;

import static org.seasar.ymir.constraint.Globals.PREFIX_REGEX;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.seasar.ymir.FormFile;
import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.ConstraintUtils;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.NotEmpty;
import org.seasar.ymir.constraint.annotation.Required;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;

/**
 * @since 1.0.7
 */
public class NotEmptyConstraint extends AbstractConstraint<NotEmpty> {
    @Override
    protected String getConstraintKey() {
        return "notEmpty";
    }

    public void confirm(Object component, Request request, NotEmpty annotation,
            AnnotatedElement element) throws ConstraintViolatedException {
        String[] names = getParameterNames(request, getPropertyName(element),
                annotation.value());
        if (names.length == 0) {
            return;
        }

        String fullMessageKey = getFullMessageKey(annotation.messageKey());
        Notes notes = new Notes();
        for (String name : names) {
            if (request.getParameter(name) == null
                    && request.getFileParameter(name) == null) {
                continue;
            }

            if (ConstraintUtils.isEmpty(request, name, annotation.completely(),
                    annotation.allowWhitespace(), annotation
                            .allowFullWidthWhitespace())) {
                notes
                        .add(name, new Note(fullMessageKey,
                                new Object[] { name }));
            }
        }
        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }
}
