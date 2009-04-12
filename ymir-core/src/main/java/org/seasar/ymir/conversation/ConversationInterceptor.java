package org.seasar.ymir.conversation;

import java.lang.reflect.Method;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Action;
import org.seasar.ymir.ActionManager;
import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.IllegalClientCodeRuntimeException;
import org.seasar.ymir.Request;
import org.seasar.ymir.Response;
import org.seasar.ymir.annotation.Bool;
import org.seasar.ymir.annotation.handler.AnnotationHandler;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.annotation.End;
import org.seasar.ymir.conversation.impl.BeginSubConversationMethodInvoker;
import org.seasar.ymir.conversation.impl.EndConversationMethodInvoker;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;

/**
 * Conversationスコープを実現するためのクラスです。
 */
public class ConversationInterceptor extends AbstractYmirProcessInterceptor {
    private ActionManager actionManager_;

    private ApplicationManager applicationManager_;

    private AnnotationHandler annotationHandler_;

    @Binding(bindingType = BindingType.MUST)
    public void setActionManager(ActionManager actionManager) {
        actionManager_ = actionManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    @Binding(bindingType = BindingType.MUST)
    public void setAnnotationHandler(AnnotationHandler annotationHandler) {
        annotationHandler_ = annotationHandler;
    }

    @Override
    public Action actionInvoking(Request request, Action action) {
        Conversation annotation = annotationHandler_.getAnnotation(request
                .getCurrentDispatch().getPageComponent().getPageClass(),
                Conversation.class);
        if (annotation != null) {
            Conversations conversations = ConversationUtils.getConversations();

            Method actionMethod = action != null ? action.getMethodInvoker()
                    .getMethod() : null;
            Begin begin = annotationHandler_.getAnnotation(actionMethod,
                    Begin.class);
            if (begin != null) {
                conversations.begin(annotation.name(), annotation.phase(),
                        begin.where());
            } else if (isDisableBeginCheck()) {
                // Beginチェックを無効にする旨の指定がされている場合はbeginする。
                // ただし、conversationが同一であればbeginしない。
                // （こうしないと不便なことがありそうだから。不便なことがなければ、
                // 何も考えずにbeginしちゃって良いと思う。）
                conversations.begin(annotation.name(), annotation.phase(),
                        BeginCondition.EXCEPT_FOR_SAME_CONVERSATION);
            }

            conversations.join(annotation.name(), annotation.phase(),
                    annotation.followAfter(),
                    isAcceptBrowsersBackButton(annotation
                            .acceptBrowsersBackButton().booleanValue()));

            BeginSubConversation beginSubConversation = annotationHandler_
                    .getAnnotation(actionMethod, BeginSubConversation.class);
            if (annotationHandler_.isAnnotationPresent(actionMethod, End.class)) {
                if (beginSubConversation != null) {
                    throw new IllegalClientCodeRuntimeException(
                            "Can't specify both @End and @BeginSubConversation: "
                                    + actionMethod.getName());
                }
                if (conversations.isInSubConversation()
                        && !isValidReturnTypeAsSubConversationEndAction(action
                                .getReturnType())) {
                    throw new IllegalClientCodeRuntimeException(
                            "@End must annote a method whose return type is Object, String or Response: "
                                    + action.getTarget().getClass().getName()
                                    + "#" + action.getName() + "()");
                }
                action = actionManager_.newAction(action.getTarget(),
                        new EndConversationMethodInvoker(action
                                .getMethodInvoker(), actionManager_));
            } else {
                if (beginSubConversation != null) {
                    action = actionManager_.newAction(action.getTarget(),
                            new BeginSubConversationMethodInvoker(action
                                    .getMethodInvoker(), beginSubConversation));
                }
            }
        }

        return action;
    }

    boolean isAcceptBrowsersBackButton(Boolean acceptBrowsersBackButton) {
        if (acceptBrowsersBackButton != null) {
            return acceptBrowsersBackButton.booleanValue();
        } else {
            return PropertyUtils
                    .valueOf(
                            applicationManager_
                                    .findContextApplication()
                                    .getProperty(
                                            Globals.APPKEY_CORE_CONVERSATION_ACCEPTBROWSERSBACKBUTTON),
                            false);
        }
    }

    boolean isValidReturnTypeAsSubConversationEndAction(
            Class<? extends Object> returnType) {
        return returnType.isAssignableFrom(String.class)
                || returnType == Response.class;
    }

    S2Container getS2Container() {
        return applicationManager_.findContextApplication().getS2Container();
    }

    boolean isDisableBeginCheck() {
        return PropertyUtils.valueOf(applicationManager_
                .findContextApplication().getProperty(
                        Globals.APPKEY_CORE_CONVERSATION_DISABLEBEGINCHECK),
                false);
    }
}
