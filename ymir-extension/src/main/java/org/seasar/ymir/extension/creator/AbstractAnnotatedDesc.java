package org.seasar.ymir.extension.creator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AbstractAnnotatedDesc implements AnnotatedDesc {

    private Map<String, AnnotationDesc> annotationDescMap_ = new TreeMap<String, AnnotationDesc>();

    public AnnotationDesc getAnnotationDesc(String name) {
        return annotationDescMap_.get(name);
    }

    public AnnotationDesc[] getAnnotationDescs() {
        return annotationDescMap_.values().toArray(new AnnotationDesc[0]);
    }

    public void setAnnotationDesc(AnnotationDesc annotationDesc) {
        annotationDescMap_.put(annotationDesc.getName(), annotationDesc);
    }

    public String getMetaValue(String name) {
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
        return (getMetaValue(name) != null);
    }

    public String[] getMetaValues(String name) {
        List<String> valueList = new ArrayList<String>();
        MetaAnnotationDesc metas = (MetaAnnotationDesc) annotationDescMap_
                .get(ANNOTATION_NAME_METAS);
        if (metas != null) {
            valueList.addAll(Arrays.asList(metas.getValues(name)));
        }
        MetaAnnotationDesc meta = (MetaAnnotationDesc) annotationDescMap_
                .get(ANNOTATION_NAME_META);
        if (meta != null) {
            valueList.addAll(Arrays.asList(meta.getValues(name)));
        }
        return valueList.toArray(new String[0]);
    }
}
