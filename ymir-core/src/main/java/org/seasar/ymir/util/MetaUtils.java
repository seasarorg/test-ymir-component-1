package org.seasar.ymir.util;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;

public class MetaUtils {
    protected MetaUtils() {
    }

    public static boolean hasMeta(AnnotatedElement element, String name) {
        return getValue(element, name) != null;
    }

    public static String[] getValue(AnnotatedElement element, String name) {
        if (element == null) {
            return null;
        }
        Meta meta = element.getAnnotation(Meta.class);
        if (meta != null) {
            if (name.equals(meta.name())) {
                return meta.value();
            }
        }
        Metas metas = element.getAnnotation(Metas.class);
        if (metas != null) {
            for (Meta m : metas.value()) {
                if (name.equals(m.name())) {
                    return m.value();
                }
            }
        }

        return null;
    }

    public static String getFirstValue(AnnotatedElement element, String name) {
        String[] value = getValue(element, name);
        if (value == null || value.length == 0) {
            return null;
        } else {
            return value[0];
        }
    }

    public static Class<?>[] getClassValue(AnnotatedElement element, String name) {
        if (element == null) {
            return null;
        }
        Meta meta = element.getAnnotation(Meta.class);
        if (meta != null) {
            if (name.equals(meta.name())) {
                return meta.classValue();
            }
        }
        Metas metas = element.getAnnotation(Metas.class);
        if (metas != null) {
            for (Meta m : metas.value()) {
                if (name.equals(m.name())) {
                    return m.classValue();
                }
            }
        }

        return null;
    }

    public static Class<?> getFirstClassValue(AnnotatedElement element,
            String name) {
        Class<?>[] value = getClassValue(element, name);
        if (value == null || value.length == 0) {
            return null;
        } else {
            return value[0];
        }
    }
}
