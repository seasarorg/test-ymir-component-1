package org.seasar.cms.framework.creator.impl;

import org.seasar.cms.framework.creator.BodyDesc;
import org.seasar.cms.framework.creator.MethodDesc;
import org.seasar.cms.framework.creator.ParameterDesc;
import org.seasar.cms.framework.creator.TypeDesc;

public class MethodDescImpl implements MethodDesc {

    private String name_;

    private ParameterDesc[] parameterDescs_ = new ParameterDesc[0];

    private TypeDesc returnTypeDesc_ = new TypeDescImpl(new SimpleClassDesc(
        TypeDesc.TYPE_VOID));

    private BodyDesc bodyDesc_;

    private String evaluatedBody_;

    public MethodDescImpl(String name) {

        name_ = name;
    }

    public Object clone() {

        MethodDescImpl cloned;
        try {
            cloned = (MethodDescImpl) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
        if (parameterDescs_ != null) {
            cloned.parameterDescs_ = new ParameterDesc[parameterDescs_.length];
            for (int i = 0; i < parameterDescs_.length; i++) {
                cloned.parameterDescs_[i] = (ParameterDesc) parameterDescs_[i]
                    .clone();
            }
        }
        if (returnTypeDesc_ != null) {
            cloned.returnTypeDesc_ = (TypeDesc) returnTypeDesc_.clone();
        }
        if (bodyDesc_ != null) {
            cloned.bodyDesc_ = (BodyDesc) bodyDesc_.clone();
        }

        return cloned;
    }

    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append(returnTypeDesc_).append(" ").append(name_).append("(");
        String delim = "";
        for (int i = 0; i < parameterDescs_.length; i++) {
            sb.append(delim).append(parameterDescs_[i]);
            delim = ", ";
        }
        sb.append(")");
        return sb.toString();
    }

    public String getName() {

        return name_;
    }

    public TypeDesc getReturnTypeDesc() {

        return returnTypeDesc_;
    }

    public ParameterDesc[] getParameterDescs() {

        return parameterDescs_;
    }

    public void setParameterDescs(ParameterDesc[] parameterDescs) {

        parameterDescs_ = parameterDescs;
    }

    public BodyDesc getBodyDesc() {

        return bodyDesc_;
    }

    public void setBodyDesc(BodyDesc bodyDesc) {

        bodyDesc_ = bodyDesc;
    }

    public String getEvaluatedBody() {

        return evaluatedBody_;
    }

    public void setEvaluatedBody(String evaluatedBody) {

        evaluatedBody_ = evaluatedBody;
    }
}
