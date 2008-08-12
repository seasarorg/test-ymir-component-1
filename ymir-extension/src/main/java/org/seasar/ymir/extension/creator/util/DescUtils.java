package org.seasar.ymir.extension.creator.util;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.impl.AnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.MetaAnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.MetasAnnotationDescImpl;
import org.seasar.ymir.extension.creator.util.type.TypeToken;

public class DescUtils {
    private static final char BEGIN_TYPESPEC = '<';

    private static final char END_TYPESPEC = '>';

    private static final String SUFFIX_ARRAY = "[]";

    private DescUtils() {
    }

    public static String getNonGenericClassName(String className) {
        if (className == null) {
            return null;
        }

        return new TypeToken(className).getBaseName();
    }

    public static String getGenericPropertyTypeName(PropertyDescriptor pd) {
        Method method = pd.getReadMethod();
        if (method != null) {
            return DescUtils.toString(method.getGenericReturnType());
        }
        method = pd.getWriteMethod();
        if (method != null) {
            return DescUtils.toString(method.getGenericParameterTypes()[0]);
        }
        return pd.getPropertyType().getName();
    }

    public static String getComponentName(String className) {
        if (className == null) {
            return null;
        }
        if (className.endsWith(SUFFIX_ARRAY)) {
            return className.substring(0, className.length()
                    - SUFFIX_ARRAY.length());
        } else {
            return className;
        }
    }

    public static boolean isArray(String className) {
        if (className == null) {
            return false;
        }
        return className.endsWith(SUFFIX_ARRAY);
    }

    public static String getClassName(String componentName, boolean array) {
        if (componentName == null) {
            return null;
        }
        if (array) {
            return componentName + SUFFIX_ARRAY;
        } else {
            return componentName;
        }
    }

    public static AnnotationDesc[] newAnnotationDescs(AnnotatedElement element) {
        if (element == null) {
            return new AnnotationDesc[0];
        }
        Annotation[] annotations = element.getAnnotations();
        AnnotationDesc[] ads = new AnnotationDesc[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            ads[i] = newAnnotationDesc(annotations[i]);
        }
        return ads;
    }

    public static AnnotationDesc newAnnotationDesc(Annotation annotation) {
        if (annotation instanceof Metas) {
            return new MetasAnnotationDescImpl((Metas) annotation);
        } else if (annotation instanceof Meta) {
            return new MetaAnnotationDescImpl((Meta) annotation);
        } else {
            return new AnnotationDescImpl(annotation);
        }
    }

    public static String toString(Type type) {
        if (type == null) {
            return null;
        }
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            StringBuffer sb = new StringBuffer();
            while (clazz.isArray()) {
                clazz = clazz.getComponentType();
                sb.append(SUFFIX_ARRAY);
            }
            return clazz.getName() + sb.toString();
        } else {
            return type.toString();
        }
    }

    public static String getShortName(String className) {
        if (className == null) {
            return null;
        }
        int dot = className.lastIndexOf('.');
        if (dot < 0) {
            return className;
        } else {
            return className.substring(dot + 1);
        }
    }

    public static String getPackageName(String className) {
        if (className == null) {
            return null;
        }
        int dot = className.lastIndexOf('.');
        if (dot < 0) {
            return "";
        } else {
            return className.substring(0, dot);
        }
    }
}
