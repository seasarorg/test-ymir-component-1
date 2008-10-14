package org.seasar.ymir.extension.creator.util;

import static org.seasar.ymir.extension.creator.AnnotatedDesc.ANNOTATION_NAME_META;
import static org.seasar.ymir.extension.creator.AnnotatedDesc.ANNOTATION_NAME_METAS;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;
import org.seasar.ymir.extension.creator.MetasAnnotationDesc;
import org.seasar.ymir.extension.creator.impl.AnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.MetaAnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.MetasAnnotationDescImpl;
import org.seasar.ymir.extension.creator.util.type.TypeToken;

public class DescUtils {
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

    public static void setAnnotationDesc(
            Map<String, AnnotationDesc> annotationDescMap,
            AnnotationDesc annotationDesc) {
        if (Meta.class.getName().equals(annotationDesc.getName())) {
            // Metaの場合。
            MetaAnnotationDesc metaAd = (MetaAnnotationDesc) annotationDesc;

            MetasAnnotationDesc metas = (MetasAnnotationDesc) annotationDescMap
                    .get(Metas.class.getName());
            if (metas != null) {
                // Metasがあればそこに追加する。
                MetaAnnotationDesc[] mads = metas.getMetaAnnotationDescs();
                List<MetaAnnotationDesc> madList = new ArrayList<MetaAnnotationDesc>(
                        mads.length + 1);
                for (MetaAnnotationDesc mad : mads) {
                    if (!mad.getMetaName().equals(metaAd.getMetaName())) {
                        madList.add(mad);
                    }
                }
                madList.add(metaAd);
                metas = new MetasAnnotationDescImpl(madList
                        .toArray(new MetaAnnotationDesc[0]));
                annotationDescMap.put(Metas.class.getName(), metas);
                return;
            } else {
                MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap
                        .get(Meta.class.getName());

                // MetasがなくてMetaがあればMetasに統合する。
                if (meta != null
                        && !meta.getMetaName().equals(metaAd.getMetaName())) {
                    annotationDescMap.put(Metas.class.getName(),
                            new MetasAnnotationDescImpl(
                                    new MetaAnnotationDesc[] { meta, metaAd }));
                    annotationDescMap.remove(Meta.class.getName());
                    return;
                }
            }
        } else if (Metas.class.getName().equals(annotationDesc.getName())) {
            // Metasの場合。
            MetasAnnotationDesc metasAd = (MetasAnnotationDesc) annotationDesc;

            Map<String, MetaAnnotationDesc> madMap = new LinkedHashMap<String, MetaAnnotationDesc>();

            MetasAnnotationDesc metas = (MetasAnnotationDesc) annotationDescMap
                    .get(Metas.class.getName());
            MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap
                    .get(Meta.class.getName());
            if (metas != null) {
                // Metasがあればマージする。
                for (MetaAnnotationDesc mad : metas.getMetaAnnotationDescs()) {
                    madMap.put(mad.getMetaName(), mad);
                }
            } else if (meta != null) {
                // MetasがなくてMetaがあればMetasに統合する。
                madMap.put(meta.getMetaName(), meta);
            }
            for (MetaAnnotationDesc mad : metasAd.getMetaAnnotationDescs()) {
                madMap.put(mad.getMetaName(), mad);
            }

            metas = new MetasAnnotationDescImpl(madMap.values().toArray(
                    new MetaAnnotationDesc[0]));
            annotationDescMap.put(Metas.class.getName(), metas);
            return;
        }

        annotationDescMap.put(annotationDesc.getName(), annotationDesc);
    }

    public static String getMetaFirstValue(
            Map<String, AnnotationDesc> annotationDescMap, String name) {
        MetaAnnotationDesc metas = (MetaAnnotationDesc) annotationDescMap
                .get(ANNOTATION_NAME_METAS);
        if (metas != null) {
            String value = metas.getValue(name);
            if (value != null) {
                return value;
            }
        }
        MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap
                .get(ANNOTATION_NAME_META);
        if (meta != null) {
            return meta.getValue(name);
        }
        return null;
    }

    public static boolean hasMeta(
            Map<String, AnnotationDesc> annotationDescMap, String name) {
        MetaAnnotationDesc metas = (MetaAnnotationDesc) annotationDescMap
                .get(ANNOTATION_NAME_METAS);
        if (metas != null) {
            return metas.hasValue(name);
        }
        MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap
                .get(ANNOTATION_NAME_META);
        if (meta != null) {
            return meta.hasValue(name);
        }
        return false;
    }

    public static String[] getMetaValue(
            Map<String, AnnotationDesc> annotationDescMap, String name) {
        MetaAnnotationDesc metas = (MetaAnnotationDesc) annotationDescMap
                .get(ANNOTATION_NAME_METAS);
        if (metas != null) {
            String[] values = metas.getValues(name);
            if (values != null) {
                return values;
            }
        }
        MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap
                .get(ANNOTATION_NAME_META);
        if (meta != null) {
            String[] values = meta.getValues(name);
            if (values != null) {
                return values;
            }
        }
        return null;
    }

    public static Class<?>[] getMetaClassValue(
            Map<String, AnnotationDesc> annotationDescMap, String name) {
        MetaAnnotationDesc metas = (MetaAnnotationDesc) annotationDescMap
                .get(ANNOTATION_NAME_METAS);
        if (metas != null) {
            Class<?>[] classValues = metas.getClassValues(name);
            if (classValues != null) {
                return classValues;
            }
        }
        MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap
                .get(ANNOTATION_NAME_META);
        if (meta != null) {
            Class<?>[] classValues = meta.getClassValues(name);
            if (classValues != null) {
                return classValues;
            }
        }
        return null;
    }
}
