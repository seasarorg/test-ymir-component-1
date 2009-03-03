package org.seasar.ymir.impl;

import org.seasar.ymir.ActionManager;
import org.seasar.ymir.MatchedPathMapping;
import org.seasar.ymir.PageComponent;
import org.seasar.ymir.PageComponentVisitor;
import org.seasar.ymir.Request;

public class VisitorForPrerendering extends PageComponentVisitor<Object> {
    private Request request_;

    private ActionManager actionManager_;

    private MatchedPathMapping matched_;

    public VisitorForPrerendering(Request request, ActionManager actionManager) {
        request_ = request;
        actionManager_ = actionManager;
        matched_ = request_.getCurrentDispatch().getMatchedPathMapping();
    }

    public Object process(PageComponent pageComponent) {
        actionManager_.invokeAction(matched_.getPrerenderAction(pageComponent,
                request_));
        return null;
    }
}
