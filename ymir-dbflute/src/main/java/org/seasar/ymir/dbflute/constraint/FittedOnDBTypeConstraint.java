package org.seasar.ymir.dbflute.constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.Request;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.constraint.ConstraintUtils;
import org.seasar.ymir.constraint.ConstraintViolatedException;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.impl.AbstractConstraint;
import org.seasar.ymir.converter.PropertyHandler;
import org.seasar.ymir.converter.TypeConversionException;
import org.seasar.ymir.converter.TypeConversionManager;
import org.seasar.ymir.converter.annotation.TypeConversionHint;
import org.seasar.ymir.dbflute.EntityManager;
import org.seasar.ymir.dbflute.constraint.annotation.FittedOnDBType;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.util.ClassUtils;

public class FittedOnDBTypeConstraint extends
        AbstractConstraint<FittedOnDBType> {
    @Binding(bindingType = BindingType.MUST)
    protected AnnotationHandler annotationHandler;

    @Binding(bindingType = BindingType.MUST)
    protected EntityManager entityManager;

    @Binding(bindingType = BindingType.MUST, value = "messages")
    protected Messages messages;

    @Binding(bindingType = BindingType.MUST)
    protected TypeConversionManager typeConversionManager_;

    @Override
    protected String getConstraintKey() {
        return "fittedOnDBType";
    }

    public void confirm(Object component, Request request,
            FittedOnDBType annotation, AnnotatedElement element)
            throws ConstraintViolatedException {
        Class<?> clazz = null;
        if (element instanceof Class<?>) {
            clazz = ((Class<?>) element);
        } else if (element instanceof Method) {
            clazz = ((Method) element).getDeclaringClass();
        }
        if (!Entity.class.isAssignableFrom(clazz)) {
            throw new IllegalClientCodeRuntimeException(
                    "Cannot add @FittedOnType annotation to non-entity class element: "
                            + clazz.getName());
        }
        @SuppressWarnings("unchecked")
        Class<? extends Entity> entityClass = (Class<? extends Entity>) clazz;

        Notes notes = new Notes();

        Set<String> suppressTypeCheckSet = new HashSet<String>(Arrays
                .asList(annotation.suppressTypeCheckFor()));
        Set<String> suppressEmptyCheckSet = new HashSet<String>(Arrays
                .asList(annotation.suppressEmptyCheckFor()));
        Set<String> suppressSizeCheckSet = new HashSet<String>(Arrays
                .asList(annotation.suppressSizeCheckFor()));

        if (element instanceof Class<?>) {
            Entity entity = entityManager.newEntity(entityClass);
            for (Iterator<String> itr = request.getParameterNames(); itr
                    .hasNext();) {
                String name = itr.next();
                PropertyHandler handler = typeConversionManager_
                        .getPropertyHandler(entity, name);
                if (handler == null) {
                    continue;
                }
                confirm(request, name, entityManager.getColumnInfo(entityClass,
                        name), handler.getPropertyType(), annotationHandler
                        .getMarkedAnnotations(handler.getWriteMethod(),
                                TypeConversionHint.class), suppressTypeCheckSet
                        .contains(name), suppressEmptyCheckSet.contains(name),
                        suppressSizeCheckSet.contains(name), notes, annotation
                                .messageKey());
            }
        } else if (element instanceof Method) {
            String name = getPropertyName(element);
            confirm(request, name, entityManager.getColumnInfo(entityClass,
                    name), getPropertyType(element), annotationHandler
                    .getMarkedAnnotations(element, TypeConversionHint.class),
                    suppressTypeCheckSet.contains(name), suppressEmptyCheckSet
                            .contains(name), suppressSizeCheckSet
                            .contains(name), notes, annotation.messageKey());
        } else {
            throw new RuntimeException("May logic error");
        }

        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String name, ColumnInfo columnInfo,
            Class<?> type, Annotation[] hint, boolean suppressCheckForType,
            boolean suppressCheckForEmpty, boolean suppressCheckForSize,
            Notes notes, String messageKey) {
        if (columnInfo == null) {
            return;
        }

        String[] values = request.getParameterValues(name);
        if (values == null) {
            return;
        }

        if (!suppressCheckForEmpty) {
            // 必須条件をチェックする。
            if (columnInfo.isNotNull()) {
                boolean exist = false;
                for (String value : values) {
                    if (value.length() > 0) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    notes.add(name, new Note(ConstraintUtils.getFullMessageKey(
                            "required", messageKey), name));
                    return;
                }
            }
        }

        for (String value : values) {
            if (value.length() == 0) {
                continue;
            }

            if (!suppressCheckForType) {
                // 型をチェックする。
                try {
                    typeConversionManager_.tryToConvert(value, type, hint);
                } catch (TypeConversionException ex) {
                    String typeName = getTypeName(type);
                    String constraintKey = getConstraintKey() + "." + typeName;
                    if (messageKey == null || messageKey.length() == 0) {
                        // メッセージキーが明示的に指定されていない場合は、
                        // 型名つきのキーが存在すればそれを使うようにする。存在しなければデフォルトのキーを使うようにする。
                        if (messages.getMessage(ConstraintUtils
                                .getFullMessageKey(constraintKey)) == null) {
                            constraintKey = getConstraintKey();
                        }
                    }
                    notes.add(name, new Note(ConstraintUtils.getFullMessageKey(
                            constraintKey, messageKey), name, typeName));
                }
            }

            if (!suppressCheckForSize) {
                // 長さをチェックする。
                if (columnInfo.getPropertyType() == String.class) {
                    Integer size = columnInfo.getColumnSize();
                    if (size != null && value.length() > size) {
                        notes.add(name, new Note(ConstraintUtils
                                .getFullMessageKey(
                                        getConstraintKey() + ".size",
                                        messageKey), name, size));
                    }
                }
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
