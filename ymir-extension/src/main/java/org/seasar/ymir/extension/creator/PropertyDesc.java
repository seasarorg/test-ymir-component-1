package org.seasar.ymir.extension.creator;

import java.lang.reflect.Type;

public interface PropertyDesc extends AnnotatedDesc<PropertyDesc> {
    int NONE = 0;

    int READ = 1;

    int WRITE = 2;

    String getName();

    TypeDesc getTypeDesc();

    void setTypeDesc(TypeDesc typeDesc);

    TypeDesc setTypeDesc(Type type);

    int getMode();

    void setMode(int mode);

    void addMode(int mode);

    boolean isReadable();

    String getGetterName();

    void setGetterName(String getterName);

    boolean isWritable();

    /**
     * 型が設定済みであることを通知します。
     * <p>型は、{@link TypeDesc#isExplicit()}がtrueである場合は「確定」、
     * そうでない場合は{@link #isTypeAlreadySet()}がtrueである場合は「確定でないが推論結果として妥当な結果が設定済み」、
     * そうでない場合は「未設定または推論結果として弱い結果が設定済み」となります。
     * </p>
     */
    void notifyTypeUpdated();

    boolean isTypeAlreadySet();

    AnnotationDesc getAnnotationDescOnGetter(String name);

    void setAnnotationDescOnGetter(AnnotationDesc annotationDesc);

    void removeMetaAnnotationDescOnGetter(String metaName);

    AnnotationDesc[] getAnnotationDescsOnGetter();

    void setAnnotationDescsOnGetter(AnnotationDesc[] annotationDescs);

    void removeMetaAnnotationDescOnSetter(String metaName);

    AnnotationDesc getAnnotationDescOnSetter(String name);

    void setAnnotationDescOnSetter(AnnotationDesc annotationDesc);

    AnnotationDesc[] getAnnotationDescsOnSetter();

    void setAnnotationDescsOnSetter(AnnotationDesc[] annotationDescs);

    void setAnnotationDescs(AnnotationDesc[] annotationDescs);

    String getMetaFirstValueOnSetter(String name);

    String getMetaFirstValueOnGetter(String name);

    boolean hasMetaOnGetter(String name);

    boolean hasMetaOnSetter(String name);

    String[] getMetaValueOnGetter(String name);

    String[] getMetaValueOnSetter(String name);

    Class<?>[] getMetaClassValueOnGetter(String name);

    Class<?>[] getMetaClassValueOnSetter(String name);

    MetaAnnotationDesc[] getMetaAnnotationDescsOnGetter();

    MetaAnnotationDesc[] getMetaAnnotationDescsOnSetter();

    String getInitialValue();
}
