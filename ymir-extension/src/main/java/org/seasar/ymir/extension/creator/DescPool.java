package org.seasar.ymir.extension.creator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.ymir.extension.creator.impl.MethodDescImpl;
import org.seasar.ymir.extension.creator.impl.ParameterDescImpl;
import org.seasar.ymir.extension.creator.impl.PropertyDescImpl;
import org.seasar.ymir.extension.creator.impl.TypeDescImpl;

public class DescPool implements Iterable<ClassDesc> {
    private static ThreadLocal<DescPool> pools_ = new ThreadLocal<DescPool>();

    private Map<ClassDescKey, ClassDesc> classDescMap_ = new HashMap<ClassDescKey, ClassDesc>();

    private SourceCreator sourceCreator_;

    private ClassCreationHintBag hintBag_;

    private DescPool(SourceCreator sourceCreator, ClassCreationHintBag hintBag) {
        sourceCreator_ = sourceCreator;
        hintBag_ = hintBag;
    }

    public static DescPool getDefault() {
        return pools_.get();
    }

    public static void setDefault(DescPool pool) {
        pools_.set(pool);
    }

    public static DescPool newInstance(SourceCreator sourceCreator,
            ClassCreationHintBag hintBag) {
        return new DescPool(sourceCreator, hintBag);
    }

    public ClassDesc getClassDesc(Class<?> clazz) {
        return getClassDesc(clazz, null);
    }

    public ClassDesc getClassDesc(Class<?> clazz, String qualifier) {
        if (clazz == null) {
            return null;
        } else {
            return getClassDesc(new ClassDescKey(clazz, qualifier));
        }
    }

    public ClassDesc getClassDesc(String className) {
        return getClassDesc(className, null);
    }

    public ClassDesc getClassDesc(String className, String qualifier) {
        if (className == null) {
            return null;
        } else {
            return getClassDesc(new ClassDescKey(className, qualifier));
        }
    }

    public ClassDesc getClassDesc(ClassDescKey classDescKey) {
        if (classDescKey == null) {
            return null;
        }

        ClassDesc classDesc = classDescMap_.get(classDescKey);
        if (classDesc == null) {
            classDesc = sourceCreator_.newClassDesc(this, classDescKey
                    .getClassName(), classDescKey.getQualifier(), hintBag_);
            classDescMap_.put(classDescKey, classDesc);
        }
        return classDesc;
    }

    public TypeDesc newTypeDesc(String typeName) {
        return newTypeDesc(typeName, null);
    }

    public TypeDesc newTypeDesc(String typeName, String componentClassQualifier) {
        return new TypeDescImpl(this, typeName, componentClassQualifier);
    }

    public TypeDesc newTypeDesc(Type type) {
        return newTypeDesc(type, null);
    }

    public TypeDesc newTypeDesc(Type type, String qualifier) {
        return new TypeDescImpl(this, type, qualifier);
    }

    public TypeDesc newTypeDesc(ClassDesc componentClassDesc) {
        return newTypeDesc(componentClassDesc, false, null);
    }

    public TypeDesc newTypeDesc(TypeDesc typeDesc) {
        return new TypeDescImpl(this, typeDesc.getName(), typeDesc
                .getComponentClassDesc().getQualifier());
    }

    public TypeDesc newTypeDesc(ClassDesc componentClassDesc,
            boolean collection, String collectionClassName) {
        return new TypeDescImpl(this, componentClassDesc, collection,
                collectionClassName);
    }

    public void clear() {
        classDescMap_.clear();
    }

    public Iterator<ClassDesc> iterator() {
        return classDescMap_.values().iterator();
    }

    public List<ClassDesc> getGeneratedClassDescs() {
        List<ClassDesc> list = new ArrayList<ClassDesc>();
        for (ClassDesc classDesc : classDescMap_.values()) {
            if (getSourceCreator().isGeneratedClass(classDesc.getName())) {
                list.add(classDesc);
            }
        }
        return list;
    }

    public boolean contains(String className) {
        return contains(className, null);
    }

    public boolean contains(String className, String qualifier) {
        if (className == null) {
            return false;
        }
        return classDescMap_
                .containsKey(new ClassDescKey(className, qualifier));
    }

    public boolean contains(ClassDesc classDesc) {
        if (classDesc == null) {
            return false;
        }

        if (classDesc.getDescPool() != this) {
            throw new IllegalArgumentException(
                    "Can't process ClassDesc born from another DescPool");
        }

        for (ClassDesc cd : classDescMap_.values()) {
            // 指定されたものと全く同じClassDescを探す。
            if (cd == classDesc) {
                return true;
            }
        }
        return false;
    }

    public ClassCreationHintBag getHintBag() {
        return hintBag_;
    }

    public SourceCreator getSourceCreator() {
        return sourceCreator_;
    }

    public ClassDesc registerClassDesc(ClassDesc classDesc) {
        if (classDesc.getDescPool() != this) {
            throw new IllegalArgumentException(
                    "Can't register ClassDesc born from another DescPool");
        }
        return classDescMap_.put(new ClassDescKey(classDesc), classDesc);
    }

    public boolean unregisterClassDesc(String className) {
        return unregisterClassDesc(className, null);
    }

    public boolean unregisterClassDesc(String className, String qualifier) {
        if (className == null) {
            return false;
        }

        return unregisterClassDesc(new ClassDescKey(className, qualifier));
    }

    public boolean unregisterClassDesc(ClassDesc classDesc) {
        if (classDesc == null) {
            return false;
        }

        if (classDesc.getDescPool() != this) {
            throw new IllegalArgumentException(
                    "Can't unregister ClassDesc born from another DescPool");
        }
        for (Iterator<Map.Entry<ClassDescKey, ClassDesc>> itr = classDescMap_
                .entrySet().iterator(); itr.hasNext();) {
            Map.Entry<ClassDescKey, ClassDesc> entry = itr.next();
            // 指定されたものと全く同じClassDescを削除する。
            if (entry.getValue() == classDesc) {
                itr.remove();
                return true;
            }
        }
        return false;
    }

    public boolean unregisterClassDesc(ClassDescKey classDescKey) {
        if (classDescKey == null) {
            return false;
        }

        return classDescMap_.remove(classDescKey) != null;
    }

    public PropertyDesc newPropertyDesc(String name) {
        return new PropertyDescImpl(this, name);
    }

    public MethodDesc newMethodDesc(String name) {
        return new MethodDescImpl(this, name);
    }

    public ParameterDesc newParameterDesc() {
        return new ParameterDescImpl(this);
    }

    public static class ClassDescKey {
        private String className_;

        private String qualifier_;

        public ClassDescKey(String className) {
            this(className, null);
        }

        public ClassDescKey(String className, String qualifier) {
            className_ = className;
            qualifier_ = qualifier;
        }

        public ClassDescKey(Class<?> clazz) {
            this(clazz, null);
        }

        public ClassDescKey(Class<?> clazz, String qualifier) {
            className_ = clazz.getName();
            qualifier_ = qualifier;
        }

        public ClassDescKey(ClassDesc classDesc) {
            className_ = classDesc.getName();
            qualifier_ = classDesc.getQualifier();
        }

        public String getClassName() {
            return className_;
        }

        public String getQualifier() {
            return qualifier_;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((className_ == null) ? 0 : className_.hashCode());
            result = prime * result
                    + ((qualifier_ == null) ? 0 : qualifier_.hashCode());
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
            final ClassDescKey other = (ClassDescKey) obj;
            if (className_ == null) {
                if (other.className_ != null)
                    return false;
            } else if (!className_.equals(other.className_))
                return false;
            if (qualifier_ == null) {
                if (other.qualifier_ != null)
                    return false;
            } else if (!qualifier_.equals(other.qualifier_))
                return false;
            return true;
        }
    }
}
