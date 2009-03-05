package org.seasar.ymir.extension.creator.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.seasar.ymir.extension.creator.AbstractAnnotatedDesc;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.ThrowsDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.util.DescUtils;

public class MethodDescImpl extends AbstractAnnotatedDesc implements MethodDesc {

    private String name_;

    private ParameterDesc[] parameterDescs_ = new ParameterDesc[0];

    private TypeDesc returnTypeDesc_ = new TypeDescImpl(TypeDesc.TYPE_VOID);

    private ThrowsDesc throwsDesc_ = new ThrowsDescImpl();

    private BodyDesc bodyDesc_;

    private String evaluatedBody_;

    public MethodDescImpl(String name) {
        name_ = name;
    }

    public MethodDescImpl(Method method) {
        name_ = method.getName();
        returnTypeDesc_ = new TypeDescImpl(DescUtils.toString(method
                .getGenericReturnType()));
        Type[] types = method.getGenericParameterTypes();
        parameterDescs_ = new ParameterDesc[types.length];
        for (int i = 0; i < types.length; i++) {
            parameterDescs_[i] = new ParameterDescImpl(new TypeDescImpl(
                    DescUtils.toString(types[i])));
        }
        types = method.getGenericExceptionTypes();
        for (int i = 0; i < types.length; i++) {
            throwsDesc_.addThrowable(DescUtils.toString(types[i]));
        }
        AnnotationDesc[] ads = DescUtils.newAnnotationDescs(method);
        for (int i = 0; i < ads.length; i++) {
            setAnnotationDesc(ads[i]);
        }
    }

    public Object clone() {
        MethodDescImpl cloned = (MethodDescImpl) super.clone();

        if (parameterDescs_ != null) {
            cloned.parameterDescs_ = new ParameterDesc[parameterDescs_.length];
            for (int i = 0; i < parameterDescs_.length; i++) {
                cloned.parameterDescs_[i] = (ParameterDesc) parameterDescs_[i]
                        .clone();
            }
        }
        if (throwsDesc_ != null) {
            cloned.throwsDesc_ = new ThrowsDescImpl();
            String[] classNames = throwsDesc_.getThrowableClassNames();
            for (int i = 0; i < classNames.length; i++) {
                cloned.throwsDesc_.addThrowable(classNames[i]);
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
        if (!throwsDesc_.isEmpty()) {
            String[] classNames = throwsDesc_.getThrowableClassNames();
            delim = " throws ";
            for (int i = 0; i < classNames.length; i++) {
                sb.append(delim).append(classNames[i]);
                delim = ", ";
            }
        }
        return sb.toString();
    }

    public String getName() {
        return name_;
    }

    public TypeDesc getReturnTypeDesc() {
        return returnTypeDesc_;
    }

    public void setReturnTypeDesc(TypeDesc returnTypeDesc) {
        returnTypeDesc_ = returnTypeDesc;
    }

    public void setReturnTypeDesc(String typeName) {
        setReturnTypeDesc(typeName, false);
    }

    public void setReturnTypeDesc(String typeName, boolean explicit) {
        setReturnTypeDesc(new TypeDescImpl(typeName, explicit));
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

    public ThrowsDesc getThrowsDesc() {
        return throwsDesc_;
    }

    public void setThrowsDesc(ThrowsDesc throwsDesc) {
        throwsDesc_ = throwsDesc;
    }
}
