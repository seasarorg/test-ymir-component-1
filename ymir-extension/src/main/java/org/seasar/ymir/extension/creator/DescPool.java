package org.seasar.ymir.extension.creator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.ymir.extension.creator.impl.TypeDescImpl;

public class DescPool implements Iterable<ClassDesc> {
    private static ThreadLocal<DescPool> descPools_ = new ThreadLocal<DescPool>();

    private Map<String, ClassDesc> classDescMap_ = new HashMap<String, ClassDesc>();

    private SourceCreator sourceCreator_;

    private ClassCreationHintBag hintBag_;

    private DescPool() {
    }

    public static DescPool getDefault() {
        DescPool descPool = descPools_.get();
        if (descPool == null) {
            descPool = new DescPool();
            descPools_.set(descPool);
        }
        return descPool;
    }

    public static DescPool newInstance() {
        return new DescPool();
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

    public List<ClassDesc> getClassDescs() {
        // 返したListが加工されても問題ないようにコピーを返している。
        return new ArrayList<ClassDesc>(classDescMap_.values());
    }

    public boolean contains(String className) {
        return classDescMap_.containsKey(className);
    }

    public ClassCreationHintBag getHintBag() {
        return hintBag_;
    }

    public void setHintBag(ClassCreationHintBag hintBag) {
        hintBag_ = hintBag;
    }

    public SourceCreator getSourceCreator() {
        return sourceCreator_;
    }

    public void setSourceCreator(SourceCreator sourceCreator) {
        sourceCreator_ = sourceCreator;
    }
}
