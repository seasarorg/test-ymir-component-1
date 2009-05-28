package org.seasar.ymir.extension.creator;

import java.lang.reflect.Type;

public interface PropertyDesc extends AnnotatedDesc<PropertyDesc> {
    int NONE = 0;

    int READ = 1;

    int WRITE = 2;

    int PROBABILITY_MINIMUM = 0;

    int PROBABILITY_DEFAULT = 1000;

    int PROBABILITY_MAXIMUM = Integer.MAX_VALUE;

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
     * 
     * @param probability 確からしさ。数字が大きいほど妥当であることを表します。
     */
    void notifyTypeUpdated(int probability);

    /**
     * 指定された確からしさ以上に高い確からしさで型が設定済みかどうかを返します。
     * 
     * @param probability 確からしさ。
     * @return 指定された確からしさ以上に高い確からしさで型が設定済みかどうか。
     */
    boolean isTypeAlreadySet(int probability);

    int getProbability();

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

    String getMetaFirstValueOnSetter(String name);

    String getMetaFirstValueOnGetter(String name);

    /**
     * 指定された名前のメタデータの最初の文字列値をGetterまたはSetterから取得して返します。
     * <p>GetterにもSetterにも同名のメタデータが存在する場合はGetterのメタデータが返されます。
     * </p>
     * 
     * @param name メタデータの名前。nullを指定してはいけません。
     * @return メタデータの最初の文字列値。メタデータが存在しない場合やメタデータが文字列値を持たない場合はnullを返します。
     */
    String getMetaFirstValueOnGetterOrSetter(String name);

    boolean hasMetaOnGetter(String name);

    boolean hasMetaOnSetter(String name);

    boolean hasMetaOnGetterOrSetter(String name);

    String[] getMetaValueOnGetter(String name);

    String[] getMetaValueOnSetter(String name);

    /**
     * 指定された名前のメタデータの文字列値をGetterまたはSetterから取得して返します。
     * <p>GetterにもSetterにも同名のメタデータが存在する場合はGetterのメタデータが返されます。
     * </p>
     * 
     * @param name メタデータの名前。nullを指定してはいけません。
     * @return メタデータの文字列値。メタデータが存在しない場合はnullを返します。
     */
    String[] getMetaValueOnGetterOrSetter(String name);

    Class<?>[] getMetaClassValueOnGetter(String name);

    Class<?>[] getMetaClassValueOnSetter(String name);

    /**
     * 指定された名前のメタデータのクラス値をGetterまたはSetterから取得して返します。
     * <p>GetterにもSetterにも同名のメタデータが存在する場合はGetterのメタデータが返されます。
     * </p>
     * 
     * @param name メタデータの名前。nullを指定してはいけません。
     * @return メタデータのクラス値。メタデータが存在しない場合はnullを返します。
     */
    Class<?>[] getMetaClassValueOnGetterOrSetter(String name);

    MetaAnnotationDesc[] getMetaAnnotationDescsOnGetter();

    MetaAnnotationDesc[] getMetaAnnotationDescsOnSetter();

    String getInitialValue();

    String getInitialShortValue();

    boolean isMayBoolean();

    void setMayBoolean(boolean mayBoolean);

    int getReferCount();

    void setReferCount(int referCount);

    void incrementReferCount();

    void decrementReferCount();

    boolean removeBornOf(String bornOf);
}
