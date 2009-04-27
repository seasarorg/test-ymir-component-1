package org.seasar.ymir.extension.creator;

import java.lang.reflect.Type;

public interface MethodDesc extends AnnotatedDesc<MethodDesc> {
    String getName();

    TypeDesc getReturnTypeDesc();

    void setReturnTypeDesc(TypeDesc typeDesc);

    TypeDesc setReturnTypeDesc(Type type);

    TypeDesc setReturnTypeDesc(String typeName);

    ParameterDesc[] getParameterDescs();

    void setParameterDescs(ParameterDesc... parameterDescs);

    BodyDesc getBodyDesc();

    void setBodyDesc(BodyDesc bodyDesc);

    String getEvaluatedBody();

    void setEvaluatedBody(String evaluatedBody);

    ThrowsDesc getThrowsDesc();

    void setThrowsDesc(ThrowsDesc throwsDesc);

    boolean removeBornOf(String bornOf);
}
