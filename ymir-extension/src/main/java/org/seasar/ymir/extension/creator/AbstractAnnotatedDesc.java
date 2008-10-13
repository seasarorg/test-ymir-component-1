package org.seasar.ymir.extension.creator;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.seasar.ymir.extension.creator.util.DescUtils;

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
        DescUtils.setAnnotationDesc(annotationDescMap_, annotationDesc);
    }

    public void setAnnotationDescs(AnnotationDesc[] annotationDescs) {
        annotationDescMap_.clear();
        for (AnnotationDesc ad : annotationDescs) {
            setAnnotationDesc(ad);
        }
    }

    public void clear() {
        annotationDescMap_.clear();
    }

    public MetaAnnotationDesc[] getMetaAnnotationDescs() {
        MetasAnnotationDesc metas = (MetasAnnotationDesc) annotationDescMap_
                .get(ANNOTATION_NAME_METAS);
        if (metas != null) {
            return metas.getMetaAnnotationDescs();
        }
        MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap_
                .get(ANNOTATION_NAME_META);
        if (meta != null) {
            return new MetaAnnotationDesc[] { meta };
        }
        return new MetaAnnotationDesc[0];
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
}
