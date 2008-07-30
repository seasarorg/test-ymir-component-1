package org.seasar.ymir.extension.creator.util;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;

public class MetaUtils {
    private MetaUtils() {
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
}
