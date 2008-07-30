package org.seasar.ymir.extension.creator.impl;

import java.util.HashMap;

import org.seasar.ymir.Request;
import org.seasar.ymir.extension.creator.AnnotationDesc;
import org.seasar.ymir.extension.creator.BodyDesc;
import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.FormDesc;
import org.seasar.ymir.extension.creator.MetaAnnotationDesc;
import org.seasar.ymir.extension.creator.MethodDesc;
import org.seasar.ymir.extension.creator.ParameterDesc;
import org.seasar.ymir.extension.zpt.AnalyzerUtils;

public class FormDescImpl implements FormDesc {
    private static final String INDEX_PREFIX = "[";

    private static final String INDEX_SUFFIX = "]";

    private ClassDesc classDesc_;

    private ClassDesc dtoClassDesc_;

    private String actionName_;

    private String name_;

    private boolean dispatchingByRequestParameter_;

    private String method_;

    public FormDescImpl(ClassDesc classDesc, ClassDesc dtoClassDesc,
            String actionName, String name,
            boolean dispatchingByRequestParameter, String method) {
        classDesc_ = classDesc;
        dtoClassDesc_ = dtoClassDesc;
        actionName_ = actionName;
        name_ = name;
        dispatchingByRequestParameter_ = dispatchingByRequestParameter;
        method_ = toUpperCase(method);
    }

    String toUpperCase(String string) {
        if (string == null) {
            return null;
        }
        return string.toUpperCase();
    }

    public void setActionMethodDesc(String parameterName) {
        if (parameterName == null) {
            // submit等にname属性が指定されていない場合。
            // そのsubmitを押されるとデフォルトのPOSTアクションが呼ばれるので、ボディを明示的に空にする。
            setEmptyToDefaultActionMethodBody();
            return;
        }
        int lparen = parameterName.indexOf(INDEX_PREFIX);
        if (lparen >= 0) {
            if (parameterName.endsWith(INDEX_SUFFIX)) {
                String body = parameterName.substring(0, lparen);
                if (AnalyzerUtils.isValidVariableName(body)) {
                    try {
                        Integer.parseInt(parameterName.substring(lparen
                                + INDEX_PREFIX.length(), parameterName.length()
                                - INDEX_SUFFIX.length()));

                        // パラメータ名[添え字] 形式。
                        MethodDesc md = new MethodDescImpl(actionName_ + "_"
                                + body);
                        md
                                .setParameterDescs(new ParameterDesc[] { new ParameterDescImpl(
                                        Integer.TYPE, "index") });
                        classDesc_.setMethodDesc(md);
                        adjustDefaultActionMethodBody();
                        return;
                    } catch (NumberFormatException ignore) {
                    }
                }
            }
        }

        if (AnalyzerUtils.isValidVariableName(parameterName)) {
            classDesc_.setMethodDesc(new MethodDescImpl(
                    (actionName_ + "_" + parameterName)));
            adjustDefaultActionMethodBody();
        } else {
            // このsubmitを押されるとデフォルトのPOSTアクションが呼ばれるので、ボディを明示的に空にする。
            setEmptyToDefaultActionMethodBody();
        }
    }

    void setEmptyToDefaultActionMethodBody() {
        if (!Request.METHOD_POST.equals(method_)) {
            // GETとかでもやると不便なことが多そうなので、POSTの時だけ。
            return;
        }

        MethodDesc md = getDefaultActionMethodDesc();
        md.setBodyDesc(new BodyDescImpl(""));
    }

    void adjustDefaultActionMethodBody() {
        if (!Request.METHOD_POST.equals(method_)) {
            // GETとかでもやると不便なことが多そうなので、POSTの時だけ。
            return;
        }

        MethodDesc md = getDefaultActionMethodDesc();
        BodyDesc bd = md.getBodyDesc();
        if (bd == null) {
            bd = new BodyDescImpl(BodyDesc.KEY_DEFAULTACTION_EXCEPTION,
                    new HashMap<String, Object>());
            md.setBodyDesc(bd);
        }
    }

    MethodDesc getDefaultActionMethodDesc() {
        return classDesc_.getMethodDesc(new MethodDescImpl(actionName_));
    }

    public String getActionPageClassName() {
        return classDesc_.getName();
    }

    public boolean isDispatchingByRequestParameter() {
        return dispatchingByRequestParameter_;
    }

    public ClassDesc getClassDesc() {
        return classDesc_;
    }

    public ClassDesc getDtoClassDesc() {
        return dtoClassDesc_;
    }

    public String getName() {
        return name_;
    }

    public void close() {
        MethodDesc md = getDefaultActionMethodDesc();
        BodyDesc bd = md.getBodyDesc();
        if (bd != null
                && BodyDesc.KEY_DEFAULTACTION_EXCEPTION.equals(bd.getKey())) {
            AnnotationDesc ad = new MetaAnnotationDescImpl(
                    MetaAnnotationDesc.NAME_DEFAULTACTION_EXCEPTION,
                    new String[0], new Class[0]);
            md.setAnnotationDesc(ad);
        }
    }
}
