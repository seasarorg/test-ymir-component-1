package org.seasar.ymir.extension.creator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.seasar.ymir.annotation.Meta;
import org.seasar.ymir.annotation.Metas;
import org.seasar.ymir.extension.creator.impl.MetasAnnotationDescImpl;

public class AbstractAnnotatedDesc implements AnnotatedDesc {

    private Map<String, AnnotationDesc> annotationDescMap_ = new TreeMap<String, AnnotationDesc>();

    public Object clone() {
        AbstractAnnotatedDesc cloned;
        try {
            cloned = (AbstractAnnotatedDesc) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }

        cloned.annotationDescMap_ = new TreeMap<String, AnnotationDesc>();
        for (Iterator<Map.Entry<String, AnnotationDesc>> itr = annotationDescMap_
                .entrySet().iterator(); itr.hasNext();) {
            Map.Entry<String, AnnotationDesc> entry = itr.next();
            cloned.annotationDescMap_.put(entry.getKey(),
                    (AnnotationDesc) entry.getValue().clone());
        }
        return cloned;
    }

    public AnnotationDesc getAnnotationDesc(String name) {
        return annotationDescMap_.get(name);
    }

    public AnnotationDesc[] getAnnotationDescs() {
        return annotationDescMap_.values().toArray(new AnnotationDesc[0]);
    }

    public void setAnnotationDesc(AnnotationDesc annotationDesc) {
        if (Meta.class.getName().equals(annotationDesc.getName())) {
            // Metaの場合。
            MetaAnnotationDesc metaAd = (MetaAnnotationDesc) annotationDesc;

            MetasAnnotationDesc metas = (MetasAnnotationDesc) annotationDescMap_
                    .get(Metas.class.getName());
            if (metas != null) {
                // Metasがあればそこに追加する。
                MetaAnnotationDesc[] mads = metas.getMetaAnnotationDescs();
                List<MetaAnnotationDesc> madList = new ArrayList<MetaAnnotationDesc>(
                        mads.length + 1);
                for (MetaAnnotationDesc mad : mads) {
                    if (!mad.getName().equals(metaAd.getName())) {
                        madList.add(mad);
                    }
                }
                madList.add(metaAd);
                metas = new MetasAnnotationDescImpl(madList
                        .toArray(new MetaAnnotationDesc[0]));
                annotationDescMap_.put(Metas.class.getName(), metas);
                return;
            } else {
                MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap_
                        .get(Meta.class.getName());

                // MetasがなくてMetaがあればMetasに統合する。
                if (meta != null && !meta.getName().equals(metaAd.getName())) {
                    annotationDescMap_.put(Metas.class.getName(),
                            new MetasAnnotationDescImpl(
                                    new MetaAnnotationDesc[] { meta, metaAd }));
                    annotationDescMap_.remove(Meta.class.getName());
                    return;
                }
            }
        } else if (Metas.class.getName().equals(annotationDesc.getName())) {
            // Metasの場合。
            MetasAnnotationDesc metasAd = (MetasAnnotationDesc) annotationDesc;

            Map<String, MetaAnnotationDesc> madMap = new LinkedHashMap<String, MetaAnnotationDesc>();

            MetasAnnotationDesc metas = (MetasAnnotationDesc) annotationDescMap_
                    .get(Metas.class.getName());
            MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap_
                    .get(Meta.class.getName());
            if (metas != null) {
                // Metasがあればマージする。
                for (MetaAnnotationDesc mad : metas.getMetaAnnotationDescs()) {
                    madMap.put(mad.getName(), mad);
                }
            } else if (meta != null) {
                // MetasがなくてMetaがあればMetasに統合する。
                madMap.put(meta.getName(), meta);
            }
            for (MetaAnnotationDesc mad : metasAd.getMetaAnnotationDescs()) {
                madMap.put(mad.getName(), mad);
            }

            metas = new MetasAnnotationDescImpl(madMap.values().toArray(
                    new MetaAnnotationDesc[0]));
            annotationDescMap_.put(Metas.class.getName(), metas);
            return;
        }

        annotationDescMap_.put(annotationDesc.getName(), annotationDesc);
    }

    public void setAnnotationDescs(AnnotationDesc[] annotationDescs) {
        annotationDescMap_.clear();
        for (AnnotationDesc ad : annotationDescs) {
            setAnnotationDesc(ad);
        }
    }

    public String getMetaFirstValue(String name) {
        MetaAnnotationDesc metas = (MetaAnnotationDesc) annotationDescMap_
                .get(ANNOTATION_NAME_METAS);
        if (metas != null) {
            String value = metas.getValue(name);
            if (value != null) {
                return value;
            }
        }
        MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap_
                .get(ANNOTATION_NAME_META);
        if (meta != null) {
            return meta.getValue(name);
        }
        return null;
    }

    public boolean hasMeta(String name) {
        return (getMetaFirstValue(name) != null);
    }

    public String[] getMetaValue(String name) {
        MetaAnnotationDesc metas = (MetaAnnotationDesc) annotationDescMap_
                .get(ANNOTATION_NAME_METAS);
        if (metas != null) {
            String[] values = metas.getValues(name);
            if (values != null) {
                return values;
            }
        }
        MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap_
                .get(ANNOTATION_NAME_META);
        if (meta != null) {
            String[] values = meta.getValues(name);
            if (values != null) {
                return values;
            }
        }
        return null;
    }

    public Class<?>[] getMetaClassValue(String name) {
        MetaAnnotationDesc metas = (MetaAnnotationDesc) annotationDescMap_
                .get(ANNOTATION_NAME_METAS);
        if (metas != null) {
            Class<?>[] classValues = metas.getClassValues(name);
            if (classValues != null) {
                return classValues;
            }
        }
        MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap_
                .get(ANNOTATION_NAME_META);
        if (meta != null) {
            Class<?>[] classValues = meta.getClassValues(name);
            if (classValues != null) {
                return classValues;
            }
        }
        return null;
    }

    public void clear() {
        annotationDescMap_.clear();
    }
}
