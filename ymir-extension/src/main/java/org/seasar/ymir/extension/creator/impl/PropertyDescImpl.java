package org.seasar.ymir.extension.creator.impl;

import static org.seasar.ymir.extension.creator.util.GenericsUtils.getNormalizedTypeName;
import static org.seasar.ymir.extension.creator.util.GenericsUtils.getShortTypeName;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.Desc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.extension.creator.util.type.Token;
import org.seasar.ymir.extension.creator.util.type.TokenVisitor;
import org.seasar.ymir.extension.creator.util.type.TypeToken;
import org.seasar.ymir.util.ClassUtils;

public class PropertyDescImpl extends AbstractAnnotatedDesc implements
        PropertyDesc {
    private static final Log log_ = LogFactory.getLog(PropertyDescImpl.class);

    private DescPool pool_;

    private Desc<?> parent_;

    private String name_;

    private TypeDesc typeDesc_;

    private int mode_;

    private int probability_ = PROBABILITY_MINIMUM;

    private Map<String, AnnotationDesc> annotationDescForGetterMap_ = new TreeMap<String, AnnotationDesc>();

    private Map<String, AnnotationDesc> annotationDescForSetterMap_ = new TreeMap<String, AnnotationDesc>();

    private String getterName_;

    private boolean mayBoolean_;

    private int referCount_;

    public PropertyDescImpl(DescPool pool, String name) {
        pool_ = pool;
        name_ = name;
        setTypeDesc(String.class);
        applyBornOf(pool_.getBornOf());
    }

    public PropertyDescImpl(DescPool pool, PropertyDescriptor descriptor) {
        pool_ = pool;
        name_ = descriptor.getName();
        setTypeDesc(newTypeDesc(descriptor));
        if (descriptor.getReadMethod() != null) {
            addMode(READ);
            setGetterName(descriptor.getReadMethod().getName());
            for (AnnotationDesc annotationDesc : DescUtils
                    .newAnnotationDescs(descriptor.getReadMethod())) {
                setAnnotationDescOnGetter(annotationDesc);
            }
        }
        if (descriptor.getWriteMethod() != null) {
            addMode(WRITE);
            for (AnnotationDesc annotationDesc : DescUtils
                    .newAnnotationDescs(descriptor.getWriteMethod())) {
                setAnnotationDescOnSetter(annotationDesc);
            }
        }
        Field field = findField(descriptor);
        if (field != null) {
            for (AnnotationDesc annotationDesc : DescUtils
                    .newAnnotationDescs(field)) {
                setAnnotationDesc(annotationDesc);
            }
            if (List.class.isAssignableFrom(descriptor.getPropertyType())
                    && field != null) {
                // Listのプロパティについては初期値の実装型情報をコピーする。

                Class<?> clazz;
                if (descriptor.getReadMethod() != null) {
                    clazz = descriptor.getReadMethod().getDeclaringClass();
                } else {
                    clazz = descriptor.getWriteMethod().getDeclaringClass();
                }

                boolean accessible = field.isAccessible();
                try {
                    field.setAccessible(true);

                    Object instance = ClassUtils.newInstance(field
                            .getDeclaringClass());
                    Object value = field.get(instance);
                    if (value != null) {
                        getTypeDesc().setCollectionImplementationClass(
                                value.getClass());
                    }
                } catch (Throwable ignore) {
                    log_.debug("Can't get initial value of field ("
                            + field.getName() + "): class=" + clazz.getName(),
                            ignore);
                } finally {
                    field.setAccessible(accessible);
                }
            }
        }
        applyBornOf(pool_.getBornOf());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name_ == null) ? 0 : name_.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final PropertyDescImpl other = (PropertyDescImpl) obj;
        if (name_ == null) {
            if (other.name_ != null)
                return false;
        } else if (!name_.equals(other.name_))
            return false;
        return true;
    }

    private TypeDesc newTypeDesc(PropertyDescriptor descriptor) {
        Type type;
        if (descriptor.getReadMethod() != null) {
            type = descriptor.getReadMethod().getGenericReturnType();
        } else {
            type = descriptor.getWriteMethod().getGenericParameterTypes()[0];
        }
        return pool_.newTypeDesc(type);
    }

    private Field findField(PropertyDescriptor descriptor) {
        String name = descriptor.getName();
        Field field = pool_.getSourceCreator().findField(
                descriptor.getReadMethod(), name);
        if (field == null) {
            field = pool_.getSourceCreator().findField(
                    descriptor.getWriteMethod(), name);
        }
        return field;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(typeDesc_).append(" ").append(name_).append("(");
        if ((mode_ & READ) != 0) {
            sb.append("r");
        }
        if ((mode_ & WRITE) != 0) {
            sb.append("w");
        }
        sb.append(")");
        String initialValue = getInitialValue();
        if (initialValue != null) {
            sb.append(" = ").append(initialValue);
        }
        return sb.toString();
    }

    public DescPool getDescPool() {
        return pool_;
    }

    public String getName() {
        return name_;
    }

    public TypeDesc getTypeDesc() {
        return typeDesc_;
    }

    public void setTypeDesc(TypeDesc typeDesc) {
        typeDesc_ = typeDesc;
        typeDesc_.setParent(this);
    }

    public TypeDesc setTypeDesc(Type type) {
        TypeDesc typeDesc = pool_.newTypeDesc(type);
        setTypeDesc(typeDesc);
        return typeDesc;
    }

    public int getMode() {
        return mode_;
    }

    public void setMode(int mode) {
        mode_ = mode;
    }

    public void addMode(int mode) {
        mode_ |= mode;
    }

    public boolean isReadable() {
        return ((mode_ & READ) != 0);
    }

    public boolean isWritable() {
        return ((mode_ & WRITE) != 0);
    }

    public String getGetterName() {
        return getterName_ != null ? getterName_ : constructGetterName();
    }

    public void setGetterName(String getterName) {
        getterName_ = getterName;
    }

    String constructGetterName() {
        if ("boolean".equals(typeDesc_.getName())) {
            return "is" + DescUtils.capFirst(name_);
        } else {
            return "get" + DescUtils.capFirst(name_);
        }
    }

    public boolean isTypeAlreadySet(int probability) {
        return probability_ >= probability;
    }

    public void notifyTypeUpdated(int probability) {
        if (isTypeAlreadySet(probability)) {
            throw new IllegalStateException(
                    "Can't make probability down: current=" + probability_
                            + ", new=" + probability);
        }
        probability_ = probability;
    }

    public AnnotationDesc getAnnotationDescOnGetter(String name) {
        return annotationDescForGetterMap_.get(name);
    }

    public void setAnnotationDescOnGetter(AnnotationDesc annotationDesc) {
        DescUtils
                .setAnnotationDesc(annotationDescForGetterMap_, annotationDesc);
    }

    public void removeMetaAnnotationDescOnGetter(String metaName) {
        DescUtils.removeMetaAnnotationDesc(annotationDescForGetterMap_,
                metaName);
    }

    public void removeMetaAnnotationDescOnSetter(String metaName) {
        DescUtils.removeMetaAnnotationDesc(annotationDescForSetterMap_,
                metaName);
    }

    public AnnotationDesc[] getAnnotationDescsOnGetter() {
        return annotationDescForGetterMap_.values().toArray(
                new AnnotationDesc[0]);
    }

    public void setAnnotationDescsOnGetter(AnnotationDesc[] annotationDescs) {
        annotationDescForGetterMap_.clear();
        for (AnnotationDesc annotationDesc : annotationDescs) {
            setAnnotationDescOnGetter(annotationDesc);
        }
    }

    public AnnotationDesc getAnnotationDescOnSetter(String name) {
        return annotationDescForSetterMap_.get(name);
    }

    public void setAnnotationDescOnSetter(AnnotationDesc annotationDesc) {
        DescUtils
                .setAnnotationDesc(annotationDescForSetterMap_, annotationDesc);
    }

    public AnnotationDesc[] getAnnotationDescsOnSetter() {
        return annotationDescForSetterMap_.values().toArray(
                new AnnotationDesc[0]);
    }

    public void setAnnotationDescsOnSetter(AnnotationDesc[] annotationDescs) {
        annotationDescForSetterMap_.clear();
        for (AnnotationDesc annotationDesc : annotationDescs) {
            setAnnotationDescOnSetter(annotationDesc);
        }
    }

    public String getMetaFirstValueOnGetter(String name) {
        return DescUtils.getMetaFirstValue(annotationDescForGetterMap_, name);
    }

    public String getMetaFirstValueOnSetter(String name) {
        return DescUtils.getMetaFirstValue(annotationDescForSetterMap_, name);
    }

    public String getMetaFirstValueOnGetterOrSetter(String name) {
        String value = getMetaFirstValueOnGetter(name);
        if (value == null) {
            value = getMetaFirstValueOnSetter(name);
        }
        return value;
    }

    public String[] getMetaValueOnGetter(String name) {
        return DescUtils.getMetaValue(annotationDescForGetterMap_, name);
    }

    public String[] getMetaValueOnSetter(String name) {
        return DescUtils.getMetaValue(annotationDescForSetterMap_, name);
    }

    public String[] getMetaValueOnGetterOrSetter(String name) {
        String[] value = getMetaValueOnGetter(name);
        if (value == null) {
            value = getMetaValueOnSetter(name);
        }
        return value;
    }

    public Class<?>[] getMetaClassValueOnGetter(String name) {
        return DescUtils.getMetaClassValue(annotationDescForGetterMap_, name);
    }

    public Class<?>[] getMetaClassValueOnSetter(String name) {
        return DescUtils.getMetaClassValue(annotationDescForSetterMap_, name);
    }

    public Class<?>[] getMetaClassValueOnGetterOrSetter(String name) {
        Class<?>[] classValue = getMetaClassValueOnGetter(name);
        if (classValue == null) {
            classValue = getMetaClassValueOnSetter(name);
        }
        return classValue;
    }

    public boolean hasMetaOnGetter(String name) {
        return DescUtils.hasMeta(annotationDescForGetterMap_, name);
    }

    public boolean hasMetaOnSetter(String name) {
        return DescUtils.hasMeta(annotationDescForSetterMap_, name);
    }

    public boolean hasMetaOnGetterOrSetter(String name) {
        return hasMetaOnGetter(name) || hasMetaOnSetter(name);
    }

    public MetaAnnotationDesc[] getMetaAnnotationDescsOnGetter() {
        return DescUtils.getMetaAnnotationDescs(annotationDescForGetterMap_);
    }

    public MetaAnnotationDesc[] getMetaAnnotationDescsOnSetter() {
        return DescUtils.getMetaAnnotationDescs(annotationDescForSetterMap_);
    }

    public String getInitialValue() {
        if (typeDesc_ == null) {
            return null;
        }

        String initialValue = null;

        String componentTypeName = typeDesc_.getComponentTypeName();
        if (typeDesc_.isCollection()) {
            String collectionClassName = typeDesc_.getCollectionClassName();
            if (collectionClassName != null) {
                String collectionImplementationClassName = typeDesc_
                        .getCollectionImplementationClassName();
                if (collectionImplementationClassName != null) {
                    initialValue = "new "
                            + getNormalizedTypeName(collectionImplementationClassName
                                    + "<" + componentTypeName + ">") + "()";
                } else {
                    // 実装クラスが指定されていない場合は、可能であればインタフェースごとに空の実装クラスをぶら下げておく。
                    if (List.class.getName().equals(collectionClassName)) {
                        initialValue = "new "
                                + getNormalizedTypeName(ArrayList.class
                                        .getName()
                                        + "<" + componentTypeName + ">") + "()";
                    }
                }
            } else {
                // 配列の場合は（何を生成すればいいかも分かるし）空の配列をぶら下げておく。
                initialValue = "new "
                        + getNormalizedTypeName(componentTypeName) + "[0]";
            }
        } else {
            boolean generateInitialValue = false;
            if (pool_.getSourceCreator().isDtoClass(componentTypeName)) {
                // DTOクラスだけに限定しているのは、そうしないと例えばStringやDateなどについても初期値が生成されてしまうため。
                // cf. [#YMIR-336]
                Class<?> clazz = DescUtils.getClass(componentTypeName);
                if (clazz != null) {
                    try {
                        clazz.newInstance();
                        generateInitialValue = true;
                    } catch (InstantiationException ignore) {
                    } catch (IllegalAccessException ignore) {
                    }
                } else {
                    // まだ生成されていないDTO。自動生成対象のDTOはデフォルトコンストラクタを持つので非nullを返すようにする。
                    generateInitialValue = true;
                }
            }
            if (generateInitialValue) {
                initialValue = "new " + componentTypeName + "()";
            }
        }

        return initialValue;
    }

    public String getInitialShortValue() {
        if (typeDesc_ == null) {
            return null;
        }

        String initialValue = null;

        String componentTypeName = typeDesc_.getComponentTypeName();
        Set<String> touchedClassNameSet = getTouchedClassNameSet();
        if (typeDesc_.isCollection()) {
            String collectionClassName = typeDesc_.getCollectionClassName();
            if (collectionClassName != null) {
                String collectionImplementationClassName = typeDesc_
                        .getCollectionImplementationClassName();
                if (collectionImplementationClassName != null) {
                    initialValue = "new "
                            + getShortTypeName(collectionImplementationClassName
                                    + "<" + componentTypeName + ">") + "()";
                    addTypeName(touchedClassNameSet, componentTypeName);
                    touchedClassNameSet.add(collectionImplementationClassName);
                } else {
                    // 実装クラスが指定されていない場合は、可能であればインタフェースごとに空の実装クラスをぶら下げておく。
                    if (List.class.getName().equals(collectionClassName)) {
                        initialValue = "new "
                                + getShortTypeName(ArrayList.class.getName()
                                        + "<" + componentTypeName + ">") + "()";
                        addTypeName(touchedClassNameSet, componentTypeName);
                        touchedClassNameSet.add(ArrayList.class.getName());
                    }
                }
            } else {
                // 配列の場合は（何を生成すればいいかも分かるし）空の配列をぶら下げておく。
                initialValue = "new " + getShortTypeName(componentTypeName)
                        + "[0]";
                addTypeName(touchedClassNameSet, componentTypeName);
            }
        } else {
            boolean generateInitialValue = false;
            if (pool_.getSourceCreator().isDtoClass(componentTypeName)) {
                // DTOクラスだけに限定しているのは、そうしないと例えばStringやDateなどについても初期値が生成されてしまうため。
                // cf. [#YMIR-336]
                Class<?> clazz = DescUtils.getClass(componentTypeName);
                if (clazz != null) {
                    try {
                        clazz.newInstance();
                        generateInitialValue = true;
                    } catch (InstantiationException ignore) {
                    } catch (IllegalAccessException ignore) {
                    }
                } else {
                    // まだ生成されていないDTO。自動生成対象のDTOはデフォルトコンストラクタを持つので非nullを返すようにする。
                    generateInitialValue = true;
                }
            }
            if (generateInitialValue) {
                initialValue = "new " + getShortTypeName(componentTypeName)
                        + "()";
                addTypeName(touchedClassNameSet, componentTypeName);
                touchedClassNameSet.add(componentTypeName);
            }
        }

        return initialValue;
    }

    private void addTypeName(final Set<String> set, String typeName) {
        TypeToken token = new TypeToken(typeName);
        token.accept(new TokenVisitor<Object>() {
            public Object visit(Token acceptor) {
                set.add(acceptor.getComponentName());
                return null;
            }
        });
    }

    public PropertyDesc transcriptTo(PropertyDesc desc) {
        DescPool pool = desc.getDescPool();
        super.transcriptTo(desc);

        if (typeDesc_ != null) {
            desc.setTypeDesc(typeDesc_
                    .transcriptTo(pool.newTypeDesc(typeDesc_)));
        }

        desc.setMode(mode_);
        if (probability_ > PROBABILITY_MINIMUM) {
            desc.notifyTypeUpdated(probability_);
        }

        for (AnnotationDesc annotationDesc : getAnnotationDescsOnGetter()) {
            desc.setAnnotationDescOnGetter((AnnotationDesc) annotationDesc
                    .clone());
        }

        for (AnnotationDesc annotationDesc : getAnnotationDescsOnSetter()) {
            desc.setAnnotationDescOnSetter((AnnotationDesc) annotationDesc
                    .clone());
        }

        desc.setGetterName(getterName_);

        return desc;
    }

    @SuppressWarnings("unchecked")
    public <D extends Desc<?>> D getParent() {
        return (D) parent_;
    }

    public void setParent(Desc<?> parent) {
        parent_ = parent;
    }

    public boolean isMayBoolean() {
        return mayBoolean_;
    }

    public void setMayBoolean(boolean mayBoolean) {
        mayBoolean_ = mayBoolean;
    }

    public int getReferCount() {
        return referCount_;
    }

    public void setReferCount(int referCount) {
        referCount_ = referCount;
    }

    public void incrementReferCount() {
        referCount_++;
    }

    public void decrementReferCount() {
        referCount_--;
    }

    public int getProbability() {
        return probability_;
    }

    @Override
    public void addDependingClassNamesTo(Set<String> set) {
        addDependingClassNamesTo0(set);

        typeDesc_.addDependingClassNamesTo(set);

        for (AnnotationDesc annotationDesc : getAnnotationDescsOnGetter()) {
            annotationDesc.addDependingClassNamesTo(set);
        }
        for (AnnotationDesc annotationDesc : getAnnotationDescsOnSetter()) {
            annotationDesc.addDependingClassNamesTo(set);
        }
    }

    @Override
    public void setTouchedClassNameSet(Set<String> set) {
        if (setTouchedClassNameSet0(set)) {
            return;
        }

        typeDesc_.setTouchedClassNameSet(set);

        for (AnnotationDesc annotationDesc : getAnnotationDescsOnGetter()) {
            annotationDesc.setTouchedClassNameSet(set);
        }
        for (AnnotationDesc annotationDesc : getAnnotationDescsOnSetter()) {
            annotationDesc.setTouchedClassNameSet(set);
        }
    }

    void applyBornOf(String bornOf) {
        if (bornOf == null) {
            return;
        }

        // Getter。
        AnnotationDesc annotationDescOnGetter = DescUtils
                .newBornOfMetaAnnotationDesc(
                        getMetaValueOnGetter(Globals.META_NAME_BORNOF), bornOf);
        removeMetaAnnotationDescOnGetter(Globals.META_NAME_BORNOF);
        setAnnotationDescOnGetter(annotationDescOnGetter);

        // Setter。
        AnnotationDesc annotationDescOnSetter = DescUtils
                .newBornOfMetaAnnotationDesc(
                        getMetaValueOnSetter(Globals.META_NAME_BORNOF), bornOf);
        removeMetaAnnotationDescOnSetter(Globals.META_NAME_BORNOF);
        setAnnotationDescOnSetter(annotationDescOnSetter);

        // フィールド。
        AnnotationDesc annotationDesc = DescUtils.newBornOfMetaAnnotationDesc(
                getMetaValue(Globals.META_NAME_BORNOF), bornOf);
        removeMetaAnnotationDesc(Globals.META_NAME_BORNOF);
        setAnnotationDesc(annotationDesc);
    }

    public boolean removeBornOf(String bornOf) {
        if (bornOf == null || bornOf.startsWith(Globals.BORNOFPREFIX_REQUEST)) {
            return false;
        }

        boolean mayFieldBeRemoved = false;

        // Getter。

        String[] values = getMetaValueOnGetter(Globals.META_NAME_BORNOF);
        if (values != null) {
            removeMetaAnnotationDescOnGetter(Globals.META_NAME_BORNOF);

            List<String> valueList = new ArrayList<String>();
            for (String value : values) {
                if (!value.equals(bornOf)) {
                    valueList.add(value);
                }
            }
            values = valueList.toArray(new String[0]);
            if (values.length == 0) {
                mode_ &= ~PropertyDesc.READ;
            } else {
                setAnnotationDescOnGetter(new MetaAnnotationDescImpl(
                        Globals.META_NAME_BORNOF, values));
            }
        }

        // Setter。

        values = getMetaValueOnSetter(Globals.META_NAME_BORNOF);
        if (values != null) {
            removeMetaAnnotationDescOnSetter(Globals.META_NAME_BORNOF);

            List<String> valueList = new ArrayList<String>();
            for (String value : values) {
                if (!value.equals(bornOf)) {
                    valueList.add(value);
                }
            }
            values = valueList.toArray(new String[0]);
            if (values.length == 0) {
                mode_ &= ~PropertyDesc.WRITE;
            } else {
                setAnnotationDescOnSetter(new MetaAnnotationDescImpl(
                        Globals.META_NAME_BORNOF, values));
            }
        }

        // フィールド。

        values = getMetaValue(Globals.META_NAME_BORNOF);
        if (values != null) {
            removeMetaAnnotationDesc(Globals.META_NAME_BORNOF);

            List<String> valueList = new ArrayList<String>();
            for (String value : values) {
                if (!value.equals(bornOf)) {
                    valueList.add(value);
                }
            }
            values = valueList.toArray(new String[0]);
            if (values.length == 0) {
                mayFieldBeRemoved = true;
            } else {
                setAnnotationDesc(new MetaAnnotationDescImpl(
                        Globals.META_NAME_BORNOF, values));
            }
        }

        if (mode_ == PropertyDesc.NONE && mayFieldBeRemoved) {
            return true;
        } else {
            return false;
        }
    }
}
