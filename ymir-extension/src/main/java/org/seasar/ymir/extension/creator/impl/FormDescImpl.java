package org.seasar.ymir.extension.creator.impl;

import org.seasar.ymir.extension.creator.ClassDesc;
import org.seasar.ymir.extension.creator.FormDesc;
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

    public FormDescImpl(ClassDesc classDesc, ClassDesc dtoClassDesc,
            String actionName, String name,
            boolean dispatchingByRequestParameter) {

        classDesc_ = classDesc;
        dtoClassDesc_ = dtoClassDesc;
        actionName_ = actionName;
        name_ = name;
        dispatchingByRequestParameter_ = dispatchingByRequestParameter;
    }

    public void setActionMethodDesc(String parameterName) {

        if (parameterName == null) {
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
                        return;
                    } catch (NumberFormatException ignore) {
                    }
                }
            }
        }

        if (AnalyzerUtils.isValidVariableName(parameterName)) {
            classDesc_.setMethodDesc(new MethodDescImpl(
                    (actionName_ + "_" + parameterName)));
        }
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
}
