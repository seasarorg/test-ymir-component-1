package org.seasar.ymir.extension.creator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.ymir.extension.creator.impl.TypeDescImpl;

public class DescPool implements Iterable<ClassDesc> {
    private static ThreadLocal<DescPool> pools_ = new ThreadLocal<DescPool>();

    private Map<String, ClassDesc> classDescMap_ = new HashMap<String, ClassDesc>();

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
        if (clazz == null) {
            return null;
        } else {
            return getClassDesc(clazz.getName());
        }
    }

    public ClassDesc getClassDesc(String className) {
        if (className == null) {
            return null;
        }

        ClassDesc classDesc = classDescMap_.get(className);
        if (classDesc == null) {
            classDesc = sourceCreator_.newClassDesc(this, className, hintBag_);
            classDescMap_.put(className, classDesc);
        }
        return classDesc;
    }

    public TypeDesc newTypeDesc(String typeName) {
        return new TypeDescImpl(this, typeName);
    }

    public TypeDesc newTypeDesc(Type type) {
        return new TypeDescImpl(this, type);
    }

    public TypeDesc newTypeDesc(ClassDesc classDesc) {
        return new TypeDescImpl(this, classDesc);
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
        return classDescMap_.containsKey(className);
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
        return classDescMap_.put(classDesc.getName(), classDesc);
    }

    public ClassDesc unregisterClassDesc(String className) {
        if (className == null) {
            return null;
        }
        return classDescMap_.remove(className);
    }

    public ClassDesc unregisterClassDesc(ClassDesc classDesc) {
        if (classDesc == null) {
            return null;
        }
        if (classDesc.getDescPool() != this) {
            throw new IllegalArgumentException(
                    "Can't unregister ClassDesc born from another DescPool");
        }
        return classDescMap_.remove(classDesc.getName());
    }
}
