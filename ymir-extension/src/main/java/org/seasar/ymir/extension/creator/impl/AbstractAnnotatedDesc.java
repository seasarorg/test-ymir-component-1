package org.seasar.ymir.extension.creator.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.seasar.ymir.extension.creator.AnnotatedDesc;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;
import org.seasar.ymir.extension.creator.util.DescUtils;

abstract public class AbstractAnnotatedDesc {
    private Map<String, AnnotationDesc> annotationDescMap_ = new TreeMap<String, AnnotationDesc>();

    private Map<String, Object> attributeMap_ = new HashMap<String, Object>();

    private Set<String> touchedClassNameSet_ = new HashSet<String>();

    abstract public DescPool getDescPool();

    public AnnotatedDesc<?> transcriptTo(AnnotatedDesc<?> desc) {
        for (AnnotationDesc annotationDesc : getAnnotationDescs()) {
            desc.setAnnotationDesc((AnnotationDesc) annotationDesc.clone());
        }

        desc.setAttributeMap(attributeMap_);

        return desc;
    }

    public AnnotationDesc getAnnotationDesc(String name) {
        return annotationDescMap_.get(name);
    }

    public AnnotationDesc[] getAnnotationDescs() {
        return annotationDescMap_.values().toArray(new AnnotationDesc[0]);
    }

    public void setAnnotationDesc(AnnotationDesc annotationDesc) {
        DescUtils.setAnnotationDesc(annotationDescMap_, annotationDesc);
    }

    public void setAnnotationDescs(AnnotationDesc... annotationDescs) {
        annotationDescMap_.clear();
        for (AnnotationDesc ad : annotationDescs) {
            setAnnotationDesc(ad);
        }
    }

    public void removeMetaAnnotationDesc(String metaName) {
        DescUtils.removeMetaAnnotationDesc(annotationDescMap_, metaName);
    }

    public void clear() {
        annotationDescMap_.clear();
        attributeMap_ = new HashMap<String, Object>();
    }

    public MetaAnnotationDesc[] getMetaAnnotationDescs() {
        return DescUtils.getMetaAnnotationDescs(annotationDescMap_);
    }

    public Class<?>[] getMetaClassValue(String name) {
        return DescUtils.getMetaClassValue(annotationDescMap_, name);
    }

    public String getMetaFirstValue(String name) {
        return DescUtils.getMetaFirstValue(annotationDescMap_, name);
    }

    public String[] getMetaValue(String name) {
        return DescUtils.getMetaValue(annotationDescMap_, name);
    }

    public boolean hasMeta(String name) {
        return DescUtils.hasMeta(annotationDescMap_, name);
    }

    public Object getAttribute(String name) {
        return attributeMap_.get(name);
    }

    public void setAttribute(String name, Object value) {
        if (value != null) {
            attributeMap_.put(name, value);
        } else {
            removeAttribute(name);
        }
    }

    public void removeAttribute(String name) {
        attributeMap_.remove(name);
    }

    public Map<String, Object> getAttributeMap() {
        return attributeMap_;
    }

    public void setAttributeMap(Map<String, Object> attributeMap) {
        attributeMap_ = new HashMap<String, Object>(attributeMap);
    }

    abstract public void addDependingClassNamesTo(Set<String> set);

    protected void addDependingClassNamesTo0(Set<String> set) {
        for (AnnotationDesc annotationDesc : getAnnotationDescs()) {
            annotationDesc.addDependingClassNamesTo(set);
        }
    }

    public String[] getImportClassNames() {
        Set<String> set = new TreeSet<String>();
        addDependingClassNamesTo(set);

        DescUtils.removeStandardClassNames(set);
        return set.toArray(new String[0]);
    }

    abstract public void setTouchedClassNameSet(Set<String> set);

    protected boolean setTouchedClassNameSet0(Set<String> set) {
        if (set == touchedClassNameSet_) {
            // ループを避けるため。
            return true;
        }

        touchedClassNameSet_ = set;
        for (AnnotationDesc annotationDesc : getAnnotationDescs()) {
            annotationDesc.setTouchedClassNameSet(touchedClassNameSet_);
        }

        return false;
    }

    protected Set<String> getTouchedClassNameSet() {
        return touchedClassNameSet_;
    }
}
