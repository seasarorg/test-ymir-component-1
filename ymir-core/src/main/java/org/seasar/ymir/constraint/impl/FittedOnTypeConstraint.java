package org.seasar.ymir.constraint.impl;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.Request;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.constraint.ConstraintUtils;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.FittedOnType;
import org.seasar.ymir.converter.PropertyHandler;
import org.seasar.ymir.converter.TypeConversionException;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.converter.annotation.TypeConversionHint;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.util.ClassUtils;

public class FittedOnTypeConstraint extends AbstractConstraint<FittedOnType> {
    private AnnotationHandler annotationHandler_;

    private Messages messages_;

    private TypeConversionManager typeConversionManager_;

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Binding(bindingType = BindingType.MUST, value = "messages")
    public void setMessages(Messages messages) {
        messages_ = messages;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setTypeConversionManager(
            TypeConversionManager typeConversionManager) {
        typeConversionManager_ = typeConversionManager;
    }

    @Override
    protected String getConstraintKey() {
        return "fittedOnType";
    }

    public void confirm(Object component, Request request,
            FittedOnType annotation, AnnotatedElement element)
            throws ConstraintViolatedException {
        Notes notes = new Notes();
        if (element instanceof Class<?>) {
            Class<?> clazz = (Class<?>) element;
            Object bean;
            try {
                bean = clazz.newInstance();
            } catch (Throwable t) {
                throw new IllegalClientCodeRuntimeException(
                        "Can't add @FittedOnType annotation to a class that has no public default constructor: "
                                + clazz.getName(), t);
            }
            for (Iterator<String> itr = request.getParameterNames(); itr
                    .hasNext();) {
                String name = itr.next();
                PropertyHandler handler = typeConversionManager_
                        .getPropertyHandler(bean, name);
                if (handler == null) {
                    continue;
                }
                confirm(request, name, handler.getPropertyType(),
                        annotationHandler_.getMarkedAnnotations(handler
                                .getWriteMethod(), TypeConversionHint.class),
                        notes, annotation.messageKey(), annotation
                                .namePrefixOnNote());
            }
        } else if (element instanceof Method) {
            confirm(request, getPropertyName(element),
                    getPropertyType(element), annotationHandler_
                            .getMarkedAnnotations(element,
                                    TypeConversionHint.class), notes,
                    annotation.messageKey(), annotation.namePrefixOnNote());
        } else {
            throw new RuntimeException("May logic error");
        }

        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String name, Class<?> type,
            Annotation[] hint, Notes notes, String messageKey,
            String namePrefixOnNote) {
        String[] values = request.getParameterValues(name);
        if (values == null) {
            return;
        }
        for (String value : values) {
            if (value.length() == 0) {
                continue;
            }
            try {
                typeConversionManager_.tryToConvert(value, type, hint);
            } catch (TypeConversionException ex) {
                String typeName = getTypeName(type);
                String constraintKey = getConstraintKey() + "." + typeName;
                if (messageKey == null || messageKey.length() == 0) {
                    // メッセージキーが明示的に指定されていない場合は、
                    // 型名つきのキーが存在すればそれを使うようにする。存在しなければデフォルトのキーを使うようにする。
                    if (messages_.getMessage(ConstraintUtils
                            .getFullMessageKey(constraintKey)) == null) {
                        constraintKey = getConstraintKey();
                    }
                }
                notes.add(name, new Note(ConstraintUtils.getFullMessageKey(
                        constraintKey, messageKey), namePrefixOnNote + name,
                        typeName));
            }
        }
    }

    protected String getTypeName(Class<?> type) {
        // ラッパ型についてはprimitive型に補正する。
        if (ClassUtils.isWrapper(type)) {
            type = ClassUtils.toPrimitive(type);
        }
        return ClassUtils.getShortName(ClassUtils.toComponentType(type));
    }
}
