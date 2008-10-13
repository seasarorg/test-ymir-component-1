package org.seasar.ymir.extension.creator.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;
import org.seasar.ymir.extension.creator.AbstractClassDesc;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.MethodDescKey;
import org.seasar.ymir.extension.creator.PropertyDesc;
import org.seasar.ymir.extension.creator.TypeDesc;

public class ClassDescImpl extends AbstractClassDesc {
    private String name_;

    private String superclassName_;

    private Map<String, PropertyDesc> propertyDescMap_ = new TreeMap<String, PropertyDesc>();

    private Map<MethodDescKey, MethodDesc> methodDescMap_ = new TreeMap<MethodDescKey, MethodDesc>();

    private boolean baseClassAbstract_;

    public ClassDescImpl(String name) {
        setName(name);
    }

    public Object clone() {
        ClassDescImpl cloned = (ClassDescImpl) super.clone();

        cloned.propertyDescMap_ = new LinkedHashMap<String, PropertyDesc>();
        for (Iterator<Map.Entry<String, PropertyDesc>> itr = propertyDescMap_
                .entrySet().iterator(); itr.hasNext();) {
            Map.Entry<String, PropertyDesc> entry = itr.next();
            cloned.propertyDescMap_.put(entry.getKey(), (PropertyDesc) entry
                    .getValue().clone());
        }
        cloned.methodDescMap_ = new LinkedHashMap<MethodDescKey, MethodDesc>();
        for (Iterator<Map.Entry<MethodDescKey, MethodDesc>> itr = methodDescMap_
                .entrySet().iterator(); itr.hasNext();) {
            Map.Entry<MethodDescKey, MethodDesc> entry = itr.next();
            cloned.methodDescMap_.put(entry.getKey(), (MethodDesc) entry
                    .getValue().clone());
        }
        return cloned;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        name_ = name;
    }

    public PropertyDesc addProperty(String name, int mode) {
        PropertyDesc propertyDesc = getPropertyDesc(name);
        if (propertyDesc == null) {
            propertyDesc = new PropertyDescImpl(name);
            propertyDescMap_.put(name, propertyDesc);
        }
        propertyDesc.addMode(mode);
        return propertyDesc;
    }

    public PropertyDesc getPropertyDesc(String name) {

        return propertyDescMap_.get(name);
    }

    public void setPropertyDesc(PropertyDesc propertyDesc) {
        propertyDescMap_.put(propertyDesc.getName(), propertyDesc);
    }

    public void removePropertyDesc(String name) {

        propertyDescMap_.remove(name);
    }

    public PropertyDesc[] getPropertyDescs() {

        return propertyDescMap_.values().toArray(new PropertyDesc[0]);
    }

    public MethodDesc getMethodDesc(MethodDesc methodDesc) {

        return methodDescMap_.get(new MethodDescKey(methodDesc));
    }

    public void setMethodDesc(MethodDesc methodDesc) {

        methodDescMap_.put(new MethodDescKey(methodDesc), methodDesc);
    }

    public MethodDesc[] getMethodDescs() {

        return methodDescMap_.values().toArray(new MethodDesc[0]);
    }

    public void removeMethodDesc(MethodDesc methodDesc) {

        methodDescMap_.remove(new MethodDescKey(methodDesc));
    }

    public String getSuperclassName() {
        return superclassName_;
    }

    public void setSuperclassName(String superclassName) {
        superclassName_ = superclassName;
    }

    public boolean isBaseClassAbstract() {

        return baseClassAbstract_;
    }

    public void setBaseClassAbstract(boolean baseClassAbstract) {
        baseClassAbstract_ = baseClassAbstract;
    }

    public void merge(ClassDesc classDesc, boolean force) {
        if (classDesc == null) {
            return;
        }

        if (classDesc.getSuperclassName() != null
                && !classDesc.getSuperclassName()
                        .equals(Object.class.getName())
                && (force || superclassName_ == null)) {
            setSuperclassName(classDesc.getSuperclassName());
        }

        PropertyDesc[] propertyDescs = classDesc.getPropertyDescs();
        for (int i = 0; i < propertyDescs.length; i++) {
            PropertyDesc pd = getPropertyDesc(propertyDescs[i].getName());
            if (pd == null) {
                setPropertyDesc((PropertyDesc) propertyDescs[i].clone());
            } else {
                mergetPropertyDesc(pd, propertyDescs[i], force);
            }
        }

        MethodDesc[] methodDescs = classDesc.getMethodDescs();
        for (int i = 0; i < methodDescs.length; i++) {
            MethodDesc md = getMethodDesc(methodDescs[i]);
            if (md == null) {
                setMethodDesc((MethodDesc) methodDescs[i].clone());
            } else {
                if (force) {
                    // パラメータ名をコピーするためにこうしている。
                    md.setParameterDescs(methodDescs[i].getParameterDescs());
                }

                TypeDesc returnTd = md.getReturnTypeDesc();
                TypeDesc returnTypeDesc = methodDescs[i].getReturnTypeDesc();
                if (merge(returnTd, returnTypeDesc, force)) {
                    BodyDesc bodyDesc = methodDescs[i].getBodyDesc();
                    if (bodyDesc != null) {
                        md.setBodyDesc(methodDescs[i].getBodyDesc());
                    }
                }
                md.setAnnotationDescs(merge(md.getAnnotationDescs(),
                        methodDescs[i].getAnnotationDescs(), force));
            }
        }

        setAnnotationDescs(merge(getAnnotationDescs(), classDesc
                .getAnnotationDescs(), force));
    }

    void mergetPropertyDesc(PropertyDesc pd1, PropertyDesc pd2, boolean force) {
        merge(pd1.getTypeDesc(), pd2.getTypeDesc(), force);
        pd1.addMode(pd2.getMode());
        pd1.setAnnotationDescs(merge(pd1.getAnnotationDescs(), pd2
                .getAnnotationDescs(), force));
        pd1.setAnnotationDescsForGetter(merge(
                pd1.getAnnotationDescsForGetter(), pd2
                        .getAnnotationDescsForGetter(), force));
        pd1.setAnnotationDescsForSetter(merge(
                pd1.getAnnotationDescsForSetter(), pd2
                        .getAnnotationDescsForSetter(), force));
    }

    AnnotationDesc[] merge(AnnotationDesc[] ads,
            AnnotationDesc[] annotationDescs, boolean force) {
        ClassDesc dummyCd = new ClassDescImpl("");
        Map<String, AnnotationDesc> adMap = new LinkedHashMap<String, AnnotationDesc>();
        for (AnnotationDesc ad : ads) {
            if (isMetaAnnotation(ad)) {
                dummyCd.setAnnotationDesc(ad);
            } else {
                adMap.put(ad.getName(), ad);
            }
        }
        for (AnnotationDesc annotationDesc : annotationDescs) {
            if (isMetaAnnotation(annotationDesc)) {
                if (force) {
                    dummyCd.setAnnotationDesc(annotationDesc);
                } else {
                    ClassDesc dummyCd2 = new ClassDescImpl("");
                    dummyCd2.setAnnotationDesc(annotationDesc);
                    for (MetaAnnotationDesc mad : dummyCd2
                            .getMetaAnnotationDescs()) {
                        if (!dummyCd.hasMeta(mad.getMetaName())) {
                            dummyCd.setAnnotationDesc(mad);
                        }
                    }
                }
            } else {
                AnnotationDesc ad = adMap.get(annotationDesc.getName());
                if (force || ad == null) {
                    adMap.put(annotationDesc.getName(),
                            (AnnotationDesc) annotationDesc.clone());
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

    boolean isMetaAnnotation(AnnotationDesc ad) {
        return ad != null
                && (Meta.class.getName().equals(ad.getName()) || Metas.class
                        .getName().equals(ad.getName()));
    }

    boolean merge(TypeDesc td, TypeDesc typeDesc, boolean force) {
        if (td.equals(typeDesc)) {
            // force == trueの場合は強制的に上書き扱いなので、上書きしたということにするためにtrueを返すようになっている。
            return force;
        } else if (!force && !td.isExplicit() && typeDesc.isExplicit() || force
                && (!td.isExplicit() || typeDesc.isExplicit())) {
            td.transcript(typeDesc);
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {

        return (propertyDescMap_.isEmpty() && methodDescMap_.isEmpty());
    }

    public void clear() {
        super.clear();
        propertyDescMap_.clear();
        methodDescMap_.clear();
    }
}
