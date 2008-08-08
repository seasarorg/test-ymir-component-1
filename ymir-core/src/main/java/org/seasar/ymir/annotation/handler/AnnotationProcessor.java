package org.seasar.ymir.annotation.handler;

import org.seasar.ymir.Visitor;

public interface AnnotationProcessor<R> extends Visitor<R, AnnotationElement> {
}
