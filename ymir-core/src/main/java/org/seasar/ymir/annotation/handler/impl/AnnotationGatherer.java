package org.seasar.ymir.annotation.handler.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.seasar.ymir.annotation.handler.AnnotationElement;
import org.seasar.ymir.annotation.handler.AnnotationProcessor;

/**
 * 指定された要素に付与されているアノテーションのうち、ある型に合致するアノテーションだけを収集するためのクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class AnnotationGatherer implements AnnotationProcessor<Void> {
    private Class<? extends Annotation> annotationType_;

    private List<Annotation> list_ = new ArrayList<Annotation>();

    public AnnotationGatherer(Class<? extends Annotation> annotationType) {
        annotationType_ = annotationType;
    }

    public Void visit(AnnotationElement acceptor) {
        Annotation annotation = acceptor.getAnnotation();
        if (annotation.annotationType().equals(annotationType_)) {
            list_.add(annotation);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T[] getAnnotations() {
        return list_.toArray((T[]) Array.newInstance(annotationType_, list_
                .size()));
    }
}
