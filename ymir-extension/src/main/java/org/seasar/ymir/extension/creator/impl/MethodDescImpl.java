package org.seasar.ymir.extension.creator.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.seasar.ymir.extension.creator.AbstractAnnotatedDesc;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.DescPool;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.creator.ThrowsDesc;
import org.seasar.ymir.extension.creator.TypeDesc;
import org.seasar.ymir.extension.creator.util.DescUtils;

public class MethodDescImpl extends AbstractAnnotatedDesc implements MethodDesc {
    private DescPool pool_;

    private String name_;

    private ParameterDesc[] parameterDescs_ = new ParameterDesc[0];

    private TypeDesc returnTypeDesc_;

    private ThrowsDesc throwsDesc_ = new ThrowsDescImpl();

    private BodyDesc bodyDesc_;

    private String evaluatedBody_;

    public MethodDescImpl(DescPool pool, String name) {
        pool_ = pool;
        name_ = name;
        setReturnTypeDesc(Void.TYPE);
    }

    public MethodDescImpl(DescPool pool, Method method) {
        pool_ = pool;
        name_ = method.getName();
        setReturnTypeDesc(method.getGenericReturnType());
        Type[] types = method.getGenericParameterTypes();
        parameterDescs_ = new ParameterDesc[types.length];
        for (int i = 0; i < types.length; i++) {
            parameterDescs_[i] = newParameterDesc(types[i]);
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

    private ParameterDesc newParameterDesc(Type type) {
        if (pool_ != null) {
            return new ParameterDescImpl(pool_, pool_.newTypeDesc(type));
        } else {
            return new ParameterDescImpl(null, new TypeDescImpl(null, type));
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

    public DescPool getDescPool() {
        return pool_;
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

    public TypeDesc setReturnTypeDesc(Type type) {
        TypeDesc typeDesc;
        if (pool_ != null) {
            typeDesc = pool_.newTypeDesc(type);
            setReturnTypeDesc(typeDesc);
        } else {
            typeDesc = new TypeDescImpl(null, type);
            setReturnTypeDesc(typeDesc);
        }
        return typeDesc;
    }

    public TypeDesc setReturnTypeDesc(String typeName) {
        TypeDesc typeDesc;
        if (pool_ != null) {
            typeDesc = pool_.newTypeDesc(typeName);
            setReturnTypeDesc(typeDesc);
        } else {
            typeDesc = new TypeDescImpl(null, typeName);
            setReturnTypeDesc(typeDesc);
        }
        return typeDesc;
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
