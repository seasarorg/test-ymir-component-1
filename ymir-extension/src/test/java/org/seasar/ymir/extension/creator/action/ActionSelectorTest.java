package org.seasar.ymir.extension.creator.action;

import junit.framework.TestCase;

import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.extension.creator.PathMetaData;

public class ActionSelectorTest extends TestCase {

    private ActionSelector<UpdateAction> target_ = new ActionSelector<UpdateAction>();

    public void testGetAction() throws Exception {
        UpdateAction updateAction = new UpdateAction() {
            public Response act(Request request, PathMetaData pathMetaData) {
                return null;
            }
        };
        target_.register(new Condition(State.ANY, State.ANY, State.ANY, null),
                updateAction);

        assertSame(updateAction, target_.getAction(new Condition(State.FALSE,
                State.FALSE, State.FALSE, null)));
        assertSame(updateAction, target_.getAction(new Condition(State.FALSE,
                State.FALSE, State.TRUE, null)));
        assertSame(updateAction, target_.getAction(new Condition(State.FALSE,
                State.TRUE, State.FALSE, null)));
        assertSame(updateAction, target_.getAction(new Condition(State.FALSE,
                State.TRUE, State.TRUE, null)));
        assertSame(updateAction, target_.getAction(new Condition(State.TRUE,
                State.FALSE, State.FALSE, null)));
        assertSame(updateAction, target_.getAction(new Condition(State.TRUE,
                State.FALSE, State.TRUE, null)));
        assertSame(updateAction, target_.getAction(new Condition(State.TRUE,
                State.TRUE, State.FALSE, null)));
        assertSame(updateAction, target_.getAction(new Condition(State.TRUE,
                State.TRUE, State.TRUE, null)));
        assertNull(target_.getAction(new Condition(State.ANY, State.ANY,
                State.ANY, null)));
    }
}
