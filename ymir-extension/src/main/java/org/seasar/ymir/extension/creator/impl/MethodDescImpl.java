package org.seasar.ymir.extension.creator.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.seasar.ymir.extension.Globals;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.Desc;
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

    private Desc<?> parent_;

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
            parameterDescs_[i] = pool_.newParameterDesc();
            parameterDescs_[i].setTypeDesc(pool_.newTypeDesc(types[i]));
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
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
        returnTypeDesc_.setParent(this);
    }

    public TypeDesc setReturnTypeDesc(Type type) {
        TypeDesc typeDesc = pool_.newTypeDesc(type);
        setReturnTypeDesc(typeDesc);
        return typeDesc;
    }

    public TypeDesc setReturnTypeDesc(String typeName) {
        TypeDesc typeDesc = pool_.newTypeDesc(typeName);
        setReturnTypeDesc(typeDesc);
        return typeDesc;
    }

    public ParameterDesc[] getParameterDescs() {
        return parameterDescs_;
    }

    public void setParameterDescs(ParameterDesc... parameterDescs) {
        parameterDescs_ = parameterDescs;
        for (ParameterDesc parameterDesc : parameterDescs_) {
            parameterDesc.setParent(this);
        }
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

    public MethodDesc transcriptTo(MethodDesc desc) {
        DescPool pool = desc.getDescPool();
        super.transcriptTo(desc);

        List<ParameterDesc> list = new ArrayList<ParameterDesc>();
        for (ParameterDesc parameterDesc : parameterDescs_) {
            list.add(parameterDesc.transcriptTo(pool.newParameterDesc()));
        }
        desc.setParameterDescs(list.toArray(new ParameterDesc[0]));

        desc.setReturnTypeDesc(returnTypeDesc_.transcriptTo(pool
                .newTypeDesc(returnTypeDesc_)));

        for (String className : throwsDesc_.getThrowableClassNames()) {
            desc.getThrowsDesc().addThrowable(className);
        }

        if (bodyDesc_ != null) {
            desc.setBodyDesc(new BodyDescImpl(bodyDesc_.getKey(), bodyDesc_
                    .getRoot()));
        }

        desc.setEvaluatedBody(evaluatedBody_);

        return desc;
    }

    @SuppressWarnings("unchecked")
    public <D extends Desc<?>> D getParent() {
        return (D) parent_;
    }

    public void setParent(Desc<?> parent) {
        parent_ = parent;
    }

    @Override
    public void addDependingClassNamesTo(Set<String> set) {
        addDependingClassNamesTo0(set);

        for (ParameterDesc parameterDesc : parameterDescs_) {
            parameterDesc.addDependingClassNamesTo(set);
        }

        returnTypeDesc_.addDependingClassNamesTo(set);

        throwsDesc_.addDependingClassNamesTo(set);
    }

    @Override
    public void setTouchedClassNameSet(Set<String> set) {
        setTouchedClassNameSet0(set);

        for (ParameterDesc parameterDesc : parameterDescs_) {
            parameterDesc.setTouchedClassNameSet(set);
        }

        returnTypeDesc_.setTouchedClassNameSet(set);

        throwsDesc_.setTouchedClassNameSet(set);
    }

    public void applyBornOf(String bornOf) {
        if (bornOf == null) {
            return;
        }

        setAnnotationDesc(new MetaAnnotationDescImpl(Globals.META_NAME_BORNOF,
                new String[] { bornOf }));
    }

    public boolean removeBornOf(String bornOf) {
        if (bornOf == null) {
            return false;
        }

        boolean removeThis = false;
        String[] values = getMetaValue(Globals.META_NAME_BORNOF);
        if (values != null) {
            removeMetaAnnotationDesc(Globals.META_NAME_BORNOF);

            List<String> valueList = new ArrayList<String>();
            for (String value : values) {
                if (!value.equals(bornOf)) {
                    valueList.add(value);
                }
            }
            values = valueList.toArray(new String[0]);
            if (values.length == 0) {
                removeThis = true;
            } else {
                setAnnotationDesc(new MetaAnnotationDescImpl(
                        Globals.META_NAME_BORNOF, values));
            }
        }

        return removeThis;
    }
}
