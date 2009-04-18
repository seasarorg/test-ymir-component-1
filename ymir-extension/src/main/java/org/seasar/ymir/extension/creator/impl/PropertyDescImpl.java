package org.seasar.ymir.extension.creator.impl;

import static org.seasar.ymir.extension.creator.util.DescUtils.normalizePackage;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.ymir.extension.creator.AbstractAnnotatedDesc;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.util.DescUtils;
import org.seasar.ymir.util.ClassUtils;

public class PropertyDescImpl extends AbstractAnnotatedDesc implements
        PropertyDesc {
    private DescPool pool_;

    private String name_;

    private TypeDesc typeDesc_;

    private int mode_;

    private boolean typeAlreadySet_;

    private Map<String, AnnotationDesc> annotationDescForGetterMap_ = new TreeMap<String, AnnotationDesc>();

    private Map<String, AnnotationDesc> annotationDescForSetterMap_ = new TreeMap<String, AnnotationDesc>();

    private String getterName_;

    private static final Log log_ = LogFactory.getLog(PropertyDescImpl.class);

    public PropertyDescImpl(DescPool pool, String name) {
        pool_ = pool;
        name_ = name;
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

                    Object instance = ClassUtils.newInstance(clazz);
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
    }

    private TypeDesc newTypeDesc(PropertyDescriptor descriptor) {
        Type type;
        if (descriptor.getReadMethod() != null) {
            type = descriptor.getReadMethod().getGenericReturnType();
        } else {
            type = descriptor.getWriteMethod().getGenericParameterTypes()[0];
        }
        if (pool_ != null) {
            return pool_.newTypeDesc(type);
        } else {
            return new TypeDescImpl(null, type);
        }
    }

    private Field findField(PropertyDescriptor descriptor) {
        Field field = null;
        if (descriptor.getReadMethod() != null) {
            field = DescUtils.findField(toFieldName(descriptor.getName()),
                    descriptor.getReadMethod().getDeclaringClass());
        }
        if (field == null && descriptor.getWriteMethod() != null) {
            field = DescUtils.findField(toFieldName(descriptor.getName()),
                    descriptor.getWriteMethod().getDeclaringClass());
        }
        return field;
    }

    private String toFieldName(String propertyName) {
        if (pool_ != null) {
            return pool_.getSourceCreator().getSourceCreatorSetting()
                    .getFieldName(propertyName);
        } else {
            return propertyName;
        }
    }

    public Object clone() {
        PropertyDescImpl cloned = (PropertyDescImpl) super.clone();

        if (typeDesc_ != null) {
            cloned.typeDesc_ = (TypeDesc) typeDesc_.clone();
        }
        cloned.annotationDescForGetterMap_ = new TreeMap<String, AnnotationDesc>(
                annotationDescForGetterMap_);
        cloned.annotationDescForSetterMap_ = new TreeMap<String, AnnotationDesc>(
                annotationDescForSetterMap_);

        return cloned;
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
    }

    public void setTypeDesc(Type type) {
        if (pool_ != null) {
            setTypeDesc(pool_.newTypeDesc(type));
        } else {
            setTypeDesc(new TypeDescImpl(null, type));
        }
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

    public boolean isTypeAlreadySet() {
        return typeAlreadySet_;
    }

    public void notifyTypeUpdated() {
        typeAlreadySet_ = true;
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

    public void setAnnotationDescs(AnnotationDesc[] annotationDescs) {
        super.clear();
        for (AnnotationDesc annotationDesc : annotationDescs) {
            setAnnotationDesc(annotationDesc);
        }
    }

    public String getMetaFirstValueOnGetter(String name) {
        return DescUtils.getMetaFirstValue(annotationDescForGetterMap_, name);
    }

    public String getMetaFirstValueOnSetter(String name) {
        return DescUtils.getMetaFirstValue(annotationDescForSetterMap_, name);
    }

    public String[] getMetaValueOnGetter(String name) {
        return DescUtils.getMetaValue(annotationDescForGetterMap_, name);
    }

    public String[] getMetaValueOnSetter(String name) {
        return DescUtils.getMetaValue(annotationDescForSetterMap_, name);
    }

    public Class<?>[] getMetaClassValueOnGetter(String name) {
        return DescUtils.getMetaClassValue(annotationDescForGetterMap_, name);
    }

    public Class<?>[] getMetaClassValueOnSetter(String name) {
        return DescUtils.getMetaClassValue(annotationDescForSetterMap_, name);
    }

    public boolean hasMetaOnGetter(String name) {
        return DescUtils.hasMeta(annotationDescForGetterMap_, name);
    }

    public boolean hasMetaOnSetter(String name) {
        return DescUtils.hasMeta(annotationDescForSetterMap_, name);
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

        ClassDesc componentClassDesc = typeDesc_.getComponentClassDesc();
        if (typeDesc_.isCollection()) {
            String collectionClassName = typeDesc_.getCollectionClassName();
            if (collectionClassName != null) {
                String collectionImplementationClassName = typeDesc_
                        .getCollectionImplementationClassName();
                if (collectionImplementationClassName != null) {
                    initialValue = "new "
                            + normalizePackage(collectionImplementationClassName
                                    + "<" + componentClassDesc.getName() + ">")
                            + "()";
                } else {
                    // 実装クラスが指定されていない場合は、可能であればインタフェースごとに空の実装クラスをぶら下げておく。
                    if (List.class.getName().equals(collectionClassName)) {
                        initialValue = "new "
                                + normalizePackage(ArrayList.class.getName()
                                        + "<" + componentClassDesc.getName()
                                        + ">") + "()";
                    }
                }
            } else {
                // 配列の場合は（何を生成すればいいかも分かるし）空の配列をぶら下げておく。
                initialValue = "new "
                        + normalizePackage(componentClassDesc.getName())
                        + "[0]";
            }
        } else {
            boolean generateInitialValue = false;
            Class<?> clazz = DescUtils.getClass(componentClassDesc.getName());
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
            if (generateInitialValue) {
                initialValue = "new " + typeDesc_.getName() + "()";
            }
        }

        return initialValue;
    }
}
