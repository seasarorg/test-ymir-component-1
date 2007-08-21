package org.seasar.ymir.conversation.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.seasar.cms.pluggable.Configuration;
import org.seasar.framework.container.S2Container;
import org.seasar.kvasir.util.PropertyUtils;
import org.seasar.ymir.Action;
import org.seasar.ymir.LifecycleListener;
import org.seasar.ymir.Request;
import org.seasar.ymir.SessionManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.constraint.PermissionDeniedException;
import org.seasar.ymir.conversation.ConversationUtils;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.conversation.Globals;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.BeginSubConversation;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.annotation.End;
import org.seasar.ymir.impl.ActionImpl;
import org.seasar.ymir.interceptor.impl.AbstractYmirProcessInterceptor;

/**
 * Conversationスコープを実現するためのクラスです。
 */
public class ConversationInterceptor extends AbstractYmirProcessInterceptor
        implements LifecycleListener {
    private Configuration configuration_;

    private SessionManager sessionManager_;

    public void setConfiguration(Configuration configuration) {
        configuration_ = configuration;
    }

    public void setSessionManager(SessionManager sessionManager) {
        sessionManager_ = sessionManager;
    }

    public void init() {
        sessionManager_
                .addStraddlingAttributeName(ConversationUtils.ATTR_CONVERSATIONS);
    }

    public void destroy() {
    }

    @Override
    public Action actionInvoking(Request request, Action originalAction,
            Action action) throws PermissionDeniedException {
        Conversation annotation = request.getCurrentDispatch()
                .getPageComponent().getPageClass().getAnnotation(
                        Conversation.class);
        if (annotation != null) {
            Conversations conversations = ConversationUtils.getConversations();

            Method actionMethod = action != null ? action.getMethodInvoker()
                    .getMethod() : null;
            if (isAnnotationPresent(actionMethod, Begin.class)
                    || isDisableBeginCheck()
                    && !annotation.name().equals(
                            conversations.getCurrentConversationName())) {
                // Beginチェックが無効であっても、conversationが同一であればbeginしない。
                // （こうしないと不便なことがありそうだから。不便なことがなければ、Beginチェックが
                // 無効の場合は何も考えずにbeginしちゃって良いと思う。）
                conversations.begin(annotation.name(), annotation.phase());
            }

            conversations.join(annotation.name(), annotation.phase(),
                    annotation.followAfter());

            BeginSubConversation beginSubConversation = getAnnotation(
                    actionMethod, BeginSubConversation.class);
            if (isAnnotationPresent(actionMethod, End.class)) {
                if (beginSubConversation != null) {
                    throw new RuntimeException(
                            "Can't specify both @End and @BeginSubConversation: "
                                    + actionMethod.getName());
                }
                action = new ActionImpl(action.getTarget(),
                        new EndConversationMethodInvoker(action
                                .getMethodInvoker()));
            } else {
                if (beginSubConversation != null) {
                    action = new ActionImpl(action.getTarget(),
                            new BeginSubConversationMethodInvoker(action
                                    .getMethodInvoker(), beginSubConversation));
                }
            }
        }

        return action;
    }

    <T extends Annotation> T getAnnotation(AnnotatedElement element,
            Class<T> annotationClass) {
        if (element != null) {
            return element.getAnnotation(annotationClass);
        } else {
            return null;
        }
    }

    boolean isAnnotationPresent(AnnotatedElement element,
            Class<? extends Annotation> annotationType) {
        if (element != null) {
            return element.isAnnotationPresent(annotationType);
        } else {
            return false;
        }
    }

    S2Container getS2Container() {
        return YmirContext.getYmir().getApplication().getS2Container();
    }

    boolean isDisableBeginCheck() {
        return PropertyUtils.valueOf(configuration_
                .getProperty(Globals.APPKEY_DISABLEBEGINCHECK), false);
    }
}
