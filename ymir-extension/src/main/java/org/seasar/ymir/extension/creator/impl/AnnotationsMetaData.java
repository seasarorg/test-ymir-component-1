package org.seasar.ymir.extension.creator.impl;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.seasar.ymir.Phase;
import org.seasar.ymir.annotation.ForTesting;
import org.seasar.ymir.annotation.Include;
import org.seasar.ymir.annotation.Invoke;
import org.seasar.ymir.annotation.Published;
import org.seasar.ymir.aop.annotation.HTTP;
import org.seasar.ymir.aop.annotation.HTTPS;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.annotation.Confirmed;
import org.seasar.ymir.constraint.annotation.Confirmeds;
import org.seasar.ymir.constraint.annotation.ConstraintAnnotation;
import org.seasar.ymir.constraint.annotation.Datetime;
import org.seasar.ymir.constraint.annotation.Datetimes;
import org.seasar.ymir.constraint.annotation.Length;
import org.seasar.ymir.constraint.annotation.Lengths;
import org.seasar.ymir.constraint.annotation.Matched;
import org.seasar.ymir.constraint.annotation.Matcheds;
import org.seasar.ymir.constraint.annotation.Numeric;
import org.seasar.ymir.constraint.annotation.Numerics;
import org.seasar.ymir.constraint.annotation.PermissionDenied;
import org.seasar.ymir.constraint.annotation.Required;
import org.seasar.ymir.constraint.annotation.Requireds;
import org.seasar.ymir.constraint.annotation.SuppressConstraints;
import org.seasar.ymir.constraint.annotation.ValidationFailed;
import org.seasar.ymir.constraint.annotation.Validator;
import org.seasar.ymir.constraint.impl.RequiredConstraint;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.annotation.End;
import org.seasar.ymir.converter.annotation.TypeConversionHint;
import org.seasar.ymir.handler.annotation.ExceptionHandler;
import org.seasar.ymir.scope.annotation.In;
import org.seasar.ymir.scope.annotation.Inject;
import org.seasar.ymir.scope.annotation.Injects;
import org.seasar.ymir.scope.annotation.Ins;
import org.seasar.ymir.scope.annotation.Out;
import org.seasar.ymir.scope.annotation.Outs;
import org.seasar.ymir.scope.annotation.Populate;
import org.seasar.ymir.scope.annotation.RequestParameter;
import org.seasar.ymir.scope.annotation.Resolve;
import org.seasar.ymir.token.constraint.annotation.TokenRequired;
import org.seasar.ymir.util.ClassUtils;
import org.seasar.ymir.util.MethodUtils;

public class AnnotationsMetaData {
    @SuppressWarnings("unused")
    @DefaultAnnotations
    private static final Object DEFAULT_ANNOTATIONS_HOLDER = null;

    public static final AnnotationsMetaData INSTANCE = new AnnotationsMetaData();

    private Map<Class<?>, AnnotationMetaData> metaDataMap_ = new HashMap<Class<?>, AnnotationMetaData>();

    private AnnotationsMetaData() {
        loadDefaultAnnotations();
    }

    void loadDefaultAnnotations() {
        Field holder;
        try {
            holder = AnnotationsMetaData.class
                    .getDeclaredField("DEFAULT_ANNOTATIONS_HOLDER");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        DefaultAnnotations defaultAnnotations = holder
                .getAnnotation(DefaultAnnotations.class);
        for (Method method : defaultAnnotations.getClass().getMethods()) {
            if (!Annotation.class.isAssignableFrom(method.getReturnType())) {
                continue;
            }
            metaDataMap_.put(method.getReturnType(), new AnnotationMetaData(
                    defaultAnnotations, method));
        }
    }

    public boolean isDefaultValue(Annotation annotation, String attribute,
            Object value) {
        AnnotationMetaData metaData = metaDataMap_.get(annotation
                .annotationType());
        if (metaData == null) {
            return false;
        }
        return metaData.isDefaultValue(attribute, value);
    }

    boolean equals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 == null) {
            return o2 == null;
        } else if (o2 == null) {
            return false;
        } else if (o1.getClass() != o2.getClass()) {
            return false;
        } else if (!o1.getClass().isArray()) {
            return o1.equals(o2);
        } else {
            int l1 = Array.getLength(o1);
            int l2 = Array.getLength(o2);
            if (l1 != l2) {
                return false;
            }
            for (int i = 0; i < l1; i++) {
                if (!equals(Array.get(o1, i), Array.get(o2, i))) {
                    return false;
                }
            }
            return true;
        }
    }

    class AnnotationMetaData {
        private Annotation defaultAnnotation_;

        private Set<String> requilredAttributeSet_ = new HashSet<String>();

        public AnnotationMetaData(DefaultAnnotations defaultAnnotations,
                Method method) {
            defaultAnnotation_ = (Annotation) MethodUtils.invoke(method,
                    defaultAnnotations);
            RequiredAttributes requiredAttributes = method
                    .getAnnotation(RequiredAttributes.class);
            if (requiredAttributes != null) {
                requilredAttributeSet_.addAll(Arrays.asList(requiredAttributes
                        .value()));
            }
        }

        public boolean isDefaultValue(String attribute, Object value) {
            if (requilredAttributeSet_.contains(attribute)) {
                return false;
            } else {
                return AnnotationsMetaData.this.equals(MethodUtils.invoke(
                        ClassUtils.getMethod(defaultAnnotation_
                                .annotationType(), attribute),
                        defaultAnnotation_), value);
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    static @interface RequiredAttributes {
        String[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    static @interface DefaultAnnotations {
        Begin Begin() default @Begin;

        @RequiredAttributes("reenter")
        BeginSubConversation BeginSubConversation() default @BeginSubConversation(reenter = "");

        @RequiredAttributes("value")
        Confirmed Confirmed() default @Confirmed( {});

        @RequiredAttributes("value")
        Confirmeds Confirmeds() default @Confirmeds( {});

        @RequiredAttributes( { "component", "type" })
        ConstraintAnnotation ConstraintAnnotation() default @ConstraintAnnotation(component = RequiredConstraint.class, type = ConstraintType.PERMISSION);

        @RequiredAttributes("name")
        Conversation Conversation() default @Conversation(name = "");

        Datetime Datetime() default @Datetime;

        @RequiredAttributes("value")
        Datetimes Datetimes() default @Datetimes( {});

        ExceptionHandler ExceptionHandler() default @ExceptionHandler;

        End End() default @End;

        ForTesting ForTesting() default @ForTesting;

        HTTP HTTP() default @HTTP;

        HTTPS HTTPS() default @HTTPS;

        In In() default @In;

        @RequiredAttributes("value")
        Include Include() default @Include( {});

        Inject Inject() default @Inject;

        @RequiredAttributes("value")
        Injects Injects() default @Injects( {});

        @RequiredAttributes("value")
        Ins Ins() default @Ins( {});

        @RequiredAttributes("value")
        Invoke Invoke() default @Invoke(Phase.ACTION_INVOKED);

        Length Length() default @Length;

        @RequiredAttributes("value")
        Lengths Lengths() default @Lengths( {});

        Matched Matched() default @Matched;

        @RequiredAttributes("value")
        Matcheds Matcheds() default @Matcheds( {});

        Numeric Numeric() default @Numeric;

        @RequiredAttributes("value")
        Numerics Numerics() default @Numerics( {});

        Out Out() default @Out;

        @RequiredAttributes("value")
        Outs Outs() default @Outs( {});

        PermissionDenied PermissionDenied() default @PermissionDenied;

        Populate Populate() default @Populate;

        Published Published() default @Published;

        RequestParameter RequestParameter() default @RequestParameter;

        Required Required() default @Required;

        @RequiredAttributes("value")
        Requireds Requireds() default @Requireds( {});

        Resolve Resolve() default @Resolve;

        SuppressConstraints SuppressConstraints() default @SuppressConstraints;

        TokenRequired TokenRequired() default @TokenRequired;

        TypeConversionHint TypeConversionHint() default @TypeConversionHint;

        ValidationFailed ValidationFailed() default @ValidationFailed;

        Validator Validator() default @Validator;
    }
}
