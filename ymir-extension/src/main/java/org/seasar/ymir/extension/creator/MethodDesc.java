package org.seasar.ymir.extension.creator;

import java.lang.reflect.Type;

public interface MethodDesc extends AnnotatedDesc, Cloneable {
    Object clone();

    DescPool getDescPool();

    String getName();

    TypeDesc getReturnTypeDesc();

    void setReturnTypeDesc(TypeDesc typeDesc);

    void setReturnTypeDesc(Type type);

    void setReturnTypeDesc(String typeName);

    ParameterDesc[] getParameterDescs();

    void setParameterDescs(ParameterDesc[] parameterDescs);

    BodyDesc getBodyDesc();

    void setBodyDesc(BodyDesc bodyDesc);

    String getEvaluatedBody();

    void setEvaluatedBody(String evaluatedBody);

    ThrowsDesc getThrowsDesc();

    void setThrowsDesc(ThrowsDesc throwsDesc);

    void setAnnotationDescs(AnnotationDesc[] annotationDescs);
}
