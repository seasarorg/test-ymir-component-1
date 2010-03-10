package org.seasar.ymir.dbflute.constraint;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.dbmeta.DBMeta;
import org.seasar.dbflute.dbmeta.info.ColumnInfo;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
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
import org.seasar.ymir.dbflute.constraint.annotation.FittedOnDBType;
import org.seasar.ymir.message.Messages;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.util.ClassUtils;

public class FittedOnDBTypeConstraint extends
        AbstractConstraint<FittedOnDBType> {
    @Binding(bindingType = BindingType.MUST)
    protected AnnotationHandler annotationHandler;

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
        Class<?> entityClass = null;
        Map<String, ColumnInfo> columnInfoMap = new HashMap<String, ColumnInfo>();
        if (element instanceof Class<?>) {
            entityClass = ((Class<?>) element);
        } else if (element instanceof Method) {
            entityClass = ((Method) element).getDeclaringClass();
        }
        if (Entity.class.isAssignableFrom(entityClass)) {
            DBMeta meta;
            try {
                meta = ((Entity) entityClass.newInstance()).getDBMeta();
            } catch (Throwable t) {
                throw new RuntimeException("Cannot instanciate entity: "
                        + entityClass.getName(), t);
            }
            for (ColumnInfo columnInfo : meta.getColumnInfoList()) {
                columnInfoMap.put(columnInfo.getPropertyName(), columnInfo);
            }
        }

        Notes notes = new Notes();

        if (element instanceof Class<?>) {
            for (Iterator<String> itr = request.getParameterNames(); itr
                    .hasNext();) {
                String name = itr.next();
                PropertyHandler handler = typeConversionManager_
                        .getPropertyHandler(component, name);
                if (handler == null) {
                    continue;
                }
                confirm(request, name, columnInfoMap.get(name), handler
                        .getPropertyType(), annotationHandler
                        .getMarkedAnnotations(handler.getWriteMethod(),
                                TypeConversionHint.class), notes, annotation
                        .messageKey());
            }
        } else if (element instanceof Method) {
            String name = getPropertyName(element);
            confirm(request, name, columnInfoMap.get(name),
                    getPropertyType(element), annotationHandler
                            .getMarkedAnnotations(element,
                                    TypeConversionHint.class), notes,
                    annotation.messageKey());
        } else {
            throw new RuntimeException("May logic error");
        }

        if (notes.size() > 0) {
            throw new ValidationFailedException().setNotes(notes);
        }
    }

    void confirm(Request request, String name, ColumnInfo columnInfo,
            Class<?> type, Annotation[] hint, Notes notes, String messageKey) {
        String[] values = request.getParameterValues(name);
        if (values == null) {
            return;
        }

        // 必須条件をチェックする。
        if (columnInfo != null && columnInfo.isNotNull()) {
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

        for (String value : values) {
            if (value.length() == 0) {
                continue;
            }

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

            if (columnInfo != null) {
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
