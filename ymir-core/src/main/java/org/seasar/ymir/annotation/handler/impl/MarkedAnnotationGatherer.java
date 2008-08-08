package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.seasar.ymir.annotation.handler.AnnotationElement;
import org.seasar.ymir.annotation.handler.AnnotationProcessor;

/**
 * 指定された要素に付与されているアノテーションのうち、
 * 指定されたメタアノテーションが付与されているアノテーションだけを収集するためのクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class MarkedAnnotationGatherer implements AnnotationProcessor<Void> {
    private Class<? extends Annotation> metaAnnotationType_;

    private List<Annotation> list_ = new ArrayList<Annotation>();

    public MarkedAnnotationGatherer(
            Class<? extends Annotation> metaAnnotationType) {
        metaAnnotationType_ = metaAnnotationType;
    }

    public Void visit(AnnotationElement acceptor) {
        Annotation annotation = acceptor.getAnnotation();
        if (annotation.annotationType()
                .isAnnotationPresent(metaAnnotationType_)) {
            list_.add(annotation);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Annotation[] getAnnotations() {
        return list_.toArray(new Annotation[0]);
    }
}
