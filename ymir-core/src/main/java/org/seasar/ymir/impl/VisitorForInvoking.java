package org.seasar.ymir.impl;

import java.lang.reflect.Method;

import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ComponentMetaData;
import org.seasar.ymir.ComponentMetaDataFactory;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.Phase;
import org.seasar.ymir.Response;
import org.seasar.ymir.ResponseType;
import org.seasar.ymir.response.PassthroughResponse;
import org.seasar.ymir.util.ResponseUtils;

public class VisitorForInvoking extends PageComponentVisitor<Response> {
    private ActionManager actionManager_;

    private ComponentMetaDataFactory componentMetaDataFactory_;

    public VisitorForInvoking(ActionManager actionManager,
            ComponentMetaDataFactory componentMetaDataFactory) {
        actionManager_ = actionManager;
        componentMetaDataFactory_ = componentMetaDataFactory;
    }

    public Response process(PageComponent pageComponent, Object... parameters) {
        Phase phase = (Phase) parameters[0];
        String actionName = parameters.length >= 2 ? (String) parameters[1]
                : null;
        Response response = new PassthroughResponse();

        ComponentMetaData metaData = componentMetaDataFactory_
                .getInstance(pageComponent.getPageClass());

        for (Method method : metaData.getMethods(phase, actionName)) {
            response = actionManager_.invokeAction(actionManager_.newAction(
                    pageComponent.getPage(), pageComponent.getPageClass(),
                    method));
            if (response.getType() != ResponseType.PASSTHROUGH) {
                break;
            }
        }
        return response;
    }

    @Override
    public boolean isFinalResult(Response result) {
        return !ResponseUtils.isPassthroughResponse(result);
    }

    @Override
    public Response getFinalResult() {
        return new PassthroughResponse();
    }
}
