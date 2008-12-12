package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.Phase;

public class VisitorForInvoking extends PageComponentVisitor<Object> {
    private ActionManager actionManager_;

    private ComponentMetaDataFactory componentMetaDataFactory_;

    private Phase phase_;

    public VisitorForInvoking(Phase phase, ActionManager actionManager,
            ComponentMetaDataFactory componentMetaDataFactory) {
        phase_ = phase;
        actionManager_ = actionManager;
        componentMetaDataFactory_ = componentMetaDataFactory;
    }

    public Object process(PageComponent pageComponent) {
        ComponentMetaData metaData = componentMetaDataFactory_
                .getInstance(pageComponent.getPageClass());
        Method[] methods = metaData.getMethods(phase_);
        if (methods != null) {
            for (int i = 0; i < methods.length; i++) {
                actionManager_.newAction(
                        pageComponent.getPage(),
                        actionManager_.newMethodInvoker(pageComponent
                                .getPageClass(), methods[i])).invoke();
            }
        }
        return null;
    }
}
