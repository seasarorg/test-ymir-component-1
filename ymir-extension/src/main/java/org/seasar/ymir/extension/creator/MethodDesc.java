package org.seasar.ymir.extension.creator;

public interface MethodDesc extends AnnotatedDesc, Cloneable {

    Object clone();

    String getName();

    TypeDesc getReturnTypeDesc();

    void setReturnTypeDesc(TypeDesc typeDesc);

    void setReturnTypeDesc(String typeName);

    void setReturnTypeDesc(String typeName, boolean explicit);

    ParameterDesc[] getParameterDescs();

    void setParameterDescs(ParameterDesc[] parameterDescs);

    BodyDesc getBodyDesc();

    void setBodyDesc(BodyDesc bodyDesc);

    String getEvaluatedBody();

    void setEvaluatedBody(String evaluatedBody);

    ThrowsDesc getThrowsDesc();

    void setThrowsDesc(ThrowsDesc throwsDesc);
}
