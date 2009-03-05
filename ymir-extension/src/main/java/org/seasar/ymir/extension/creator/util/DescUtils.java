package org.seasar.ymir.extension.creator.util;

import static org.seasar.ymir.extension.creator.AnnotatedDesc.ANNOTATION_NAME_META;
import static org.seasar.ymir.extension.creator.AnnotatedDesc.ANNOTATION_NAME_METAS;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;
import org.seasar.ymir.extension.creator.MetasAnnotationDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.impl.AnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.ClassDescImpl;
import org.seasar.ymir.extension.creator.impl.MetaAnnotationDescImpl;
import org.seasar.ymir.extension.creator.impl.MetasAnnotationDescImpl;
import org.seasar.ymir.extension.creator.util.type.Token;
import org.seasar.ymir.extension.creator.util.type.TokenVisitor;
import org.seasar.ymir.extension.creator.util.type.TypeToken;

public class DescUtils {
    private static final String SUFFIX_ARRAY = "[]";

    private static final String PACKAGE_JAVA_LANG = "java.lang.";

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
                String metaName = metaAd.getMetaName();
                MetaAnnotationDesc[] mads = metas.getMetaAnnotationDescs();
                List<MetaAnnotationDesc> madList = new ArrayList<MetaAnnotationDesc>(
                        mads.length + 1);
                for (MetaAnnotationDesc mad : mads) {
                    if (mad.getMetaName().equals(metaName)) {
                        metaAd = merge(mad, metaAd);
                    } else {
                        madList.add(mad);
                    }
                }
                madList.add(metaAd);
                metas = new MetasAnnotationDescImpl(madList
                        .toArray(new MetaAnnotationDesc[0]));
                annotationDescMap.put(Metas.class.getName(), metas);
                return;
            } else {
                MetaAnnotationDesc mad = (MetaAnnotationDesc) annotationDescMap
                        .get(Meta.class.getName());

                if (mad != null) {
                    if (mad.getMetaName().equals(metaAd.getMetaName())) {
                        annotationDescMap.put(Meta.class.getName(), merge(mad,
                                metaAd));
                    } else {
                        // MetasがなくてMetaがあればMetasに統合する。
                        annotationDescMap
                                .put(Metas.class.getName(),
                                        new MetasAnnotationDescImpl(
                                                new MetaAnnotationDesc[] { mad,
                                                    metaAd }));
                        annotationDescMap.remove(Meta.class.getName());
                    }
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
                String metaName = mad.getMetaName();
                madMap.put(metaName, merge(madMap.get(metaName), mad));
            }

            metas = new MetasAnnotationDescImpl(madMap.values().toArray(
                    new MetaAnnotationDesc[0]));
            annotationDescMap.put(Metas.class.getName(), metas);
            return;
        }

        annotationDescMap.put(annotationDesc.getName(), annotationDesc);
    }

    static MetaAnnotationDesc merge(MetaAnnotationDesc meta1,
            MetaAnnotationDesc meta2) {
        if (meta1 == null) {
            return meta2;
        } else if (meta2 == null) {
            return meta1;
        }

        String name = meta2.getMetaName();
        if (isMergeableMeta(name)) {
            return new MetaAnnotationDescImpl(name, mergeValue(meta1
                    .getValues(name), meta2.getValues(name)), mergeValue(meta1
                    .getClassValues(name), meta2.getClassValues(name)));
        } else {
            return meta2;
        }
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

    public static MetaAnnotationDesc[] getMetaAnnotationDescs(
            Map<String, AnnotationDesc> annotationDescMap) {
        MetasAnnotationDesc metas = (MetasAnnotationDesc) annotationDescMap
                .get(ANNOTATION_NAME_METAS);
        if (metas != null) {
            return metas.getMetaAnnotationDescs();
        }
        MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap
                .get(ANNOTATION_NAME_META);
        if (meta != null) {
            return new MetaAnnotationDesc[] { meta };
        }
        return new MetaAnnotationDesc[0];
    }

    public static boolean isPrimitive(String name) {
        return ("byte".equals(name) || "short".equals(name)
                || "int".equals(name) || "long".equals(name)
                || "float".equals(name) || "double".equals(name)
                || "char".equals(name) || "boolean".equals(name));
    }

    public static boolean isMetaAnnotation(AnnotationDesc ad) {
        return ad != null
                && (Meta.class.getName().equals(ad.getName()) || Metas.class
                        .getName().equals(ad.getName()));
    }

    public static void merge(PropertyDesc pd1, PropertyDesc pd2, boolean force) {
        DescUtils.merge(pd1.getTypeDesc(), pd2.getTypeDesc(), force);
        pd1.addMode(pd2.getMode());
        pd1.setAnnotationDescs(DescUtils.merge(pd1.getAnnotationDescs(), pd2
                .getAnnotationDescs(), force));
        pd1.setAnnotationDescsOnGetter(DescUtils.merge(pd1
                .getAnnotationDescsOnGetter(),
                pd2.getAnnotationDescsOnGetter(), force));
        pd1.setAnnotationDescsOnSetter(DescUtils.merge(pd1
                .getAnnotationDescsOnSetter(),
                pd2.getAnnotationDescsOnSetter(), force));
    }

    public static void merge(MethodDesc md1, MethodDesc md2, boolean force) {
        if (force) {
            // パラメータ名をコピーするためにこうしている。
            md1.setParameterDescs(md2.getParameterDescs());
        }

        TypeDesc returnTd1 = md1.getReturnTypeDesc();
        TypeDesc returnTd2 = md2.getReturnTypeDesc();
        if (DescUtils.merge(returnTd1, returnTd2, force)) {
            BodyDesc bodyDesc = md2.getBodyDesc();
            if (bodyDesc != null) {
                md1.setBodyDesc(md2.getBodyDesc());
            }
        }
        md1.setAnnotationDescs(DescUtils.merge(md1.getAnnotationDescs(), md2
                .getAnnotationDescs(), force));
    }

    public static boolean merge(TypeDesc td1, TypeDesc td2, boolean force) {
        if (td1.equals(td2)) {
            // force == trueの場合は強制的に上書き扱いなので、上書きしたということにするためにtrueを返すようにしている。
            return force;
        } else if (!force && !td1.isExplicit() && td2.isExplicit() || force
                && (!td1.isExplicit() || td2.isExplicit())) {
            td1.transcript(td2);
            return true;
        } else {
            // マージしない。その場合でも、コレクションの実装型情報だけは補完するようにする。
            if (td1.getCollectionClassName() != null
                    && td1.getCollectionClassName().equals(
                            td2.getCollectionClassName())
                    && td1.getCollectionImplementationClassName() == null) {
                td1.setCollectionImplementationClassName(td2
                        .getCollectionImplementationClassName());
            }

            return false;
        }
    }

    public static AnnotationDesc[] merge(AnnotationDesc[] ad1s,
            AnnotationDesc[] ad2s, boolean force) {
        ClassDesc dummyCd = new ClassDescImpl("");
        Map<String, AnnotationDesc> adMap = new LinkedHashMap<String, AnnotationDesc>();
        for (AnnotationDesc ad : ad1s) {
            if (isMetaAnnotation(ad)) {
                dummyCd.setAnnotationDesc(ad);
            } else {
                adMap.put(ad.getName(), ad);
            }
        }
        for (AnnotationDesc ad : ad2s) {
            if (isMetaAnnotation(ad)) {
                if (force) {
                    dummyCd.setAnnotationDesc(ad);
                } else {
                    ClassDesc dummyCd2 = new ClassDescImpl("");
                    dummyCd2.setAnnotationDesc(ad);
                    for (MetaAnnotationDesc mad : dummyCd2
                            .getMetaAnnotationDescs()) {
                        if (!dummyCd.hasMeta(mad.getMetaName())) {
                            dummyCd.setAnnotationDesc(mad);
                        }
                    }
                }
            } else {
                AnnotationDesc a = adMap.get(ad.getName());
                if (force || a == null) {
                    adMap.put(ad.getName(), (AnnotationDesc) ad.clone());
                }
            }
        }

        AnnotationDesc metasAd = dummyCd.getAnnotationDesc(Metas.class
                .getName());
        if (metasAd != null) {
            adMap.put(metasAd.getName(), metasAd);
        } else {
            AnnotationDesc metaAd = dummyCd.getAnnotationDesc(Meta.class
                    .getName());
            if (metaAd != null) {
                adMap.put(metaAd.getName(), metaAd);
            }
        }

        return adMap.values().toArray(new AnnotationDesc[0]);
    }

    public static String normalizePackage(String typeName) {
        if (typeName == null) {
            return null;
        }

        TypeToken typeToken = new TypeToken(typeName);
        typeToken.accept(new TokenVisitor<Object>() {
            public Object visit(Token acceptor) {
                String baseName = acceptor.getBaseName();
                StringBuilder sb = new StringBuilder();
                if (baseName.startsWith(PACKAGE_JAVA_LANG)) {
                    sb.append(baseName.substring(PACKAGE_JAVA_LANG.length()));
                } else {
                    sb.append(baseName);
                }
                acceptor.setBaseName(sb.toString());
                return null;
            }
        });
        return typeToken.getAsString();
    }

    public static Class<?> getClass(String className) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            if (cl != null) {
                return cl.loadClass(className);
            } else {
                return DescUtils.class.getClassLoader().loadClass(className);
            }
        } catch (ClassNotFoundException ignore) {
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }

        return null;
    }

    public static void removeMetaAnnotationDesc(
            Map<String, AnnotationDesc> annotationDescMap, String metaName) {
        MetasAnnotationDesc metas = (MetasAnnotationDesc) annotationDescMap
                .get(Metas.class.getName());
        if (metas != null) {
            // Metasがあればそこから削除する。
            MetaAnnotationDesc[] mads = metas.getMetaAnnotationDescs();
            List<MetaAnnotationDesc> madList = new ArrayList<MetaAnnotationDesc>(
                    mads.length);
            for (MetaAnnotationDesc mad : mads) {
                if (!mad.getMetaName().equals(metaName)) {
                    madList.add(mad);
                }
            }
            if (madList.size() > 0) {
                metas = new MetasAnnotationDescImpl(madList
                        .toArray(new MetaAnnotationDesc[0]));
                annotationDescMap.put(Metas.class.getName(), metas);
            } else {
                annotationDescMap.remove(Metas.class.getName());
            }
        } else {
            MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap
                    .get(Meta.class.getName());

            if (meta != null && meta.getMetaName().equals(metaName)) {
                annotationDescMap.remove(Meta.class.getName());
            }
        }
    }

    static boolean isMergeableMeta(String metaName) {
        return metaName.equals(Globals.META_NAME_BORNOF);
    }

    @SuppressWarnings("unchecked")
    static <T> T[] mergeValue(T[] values1, T[] values2) {
        if (values1 == null && values2 == null) {
            return null;
        }

        Set<T> valueSet = new LinkedHashSet<T>();
        if (values1 != null) {
            valueSet.addAll(Arrays.asList(values1));
        }
        if (values2 != null) {
            valueSet.addAll(Arrays.asList(values2));
        }
        return valueSet.toArray((T[]) Array.newInstance(values1.getClass()
                .getComponentType(), valueSet.size()));
    }
}
