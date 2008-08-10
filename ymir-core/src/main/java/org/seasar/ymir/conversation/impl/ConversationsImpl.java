package org.seasar.ymir.conversation.impl;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.seasar.ymir.ApplicationManager;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.conversation.IllegalTransitionRuntimeException;
import org.seasar.ymir.hotdeploy.HotdeployManager;

/**
 * このクラスはスレッドセーフです。
 */
public class ConversationsImpl implements Conversations, Serializable {
    private static final long serialVersionUID = 378981595198068349L;

    private transient HotdeployManager hotdeployManager_;

    private transient ApplicationManager applicationManager_;

    private LinkedList<Conversation> conversationStack_ = new LinkedList<Conversation>();

    private Conversation currentConversation_;

    private boolean enteredInSubConversation_;

    public void setHotdeployManager(HotdeployManager hotdeployManager) {
        hotdeployManager_ = hotdeployManager;
    }

    public void setApplicationManager(ApplicationManager applicationManager) {
        applicationManager_ = applicationManager;
    }

    private Object readResolve() throws ObjectStreamException {
        // transientなオブジェクトを復元する。
        hotdeployManager_ = (HotdeployManager) YmirContext.getYmir()
                .getApplication().getS2Container().getComponent(
                        HotdeployManager.class);
        applicationManager_ = (ApplicationManager) YmirContext.getYmir()
                .getApplication().getS2Container().getComponent(
                        ApplicationManager.class);
        return this;
    }

    public synchronized Conversation getCurrentConversation() {
        return currentConversation_;
    }

    public synchronized String getCurrentConversationName() {
        if (currentConversation_ == null) {
            return null;
        } else {
            return currentConversation_.getName();
        }
    }

    public synchronized Conversation getSuperConversation() {
        if (conversationStack_.size() > 0) {
            return conversationStack_.getLast();
        } else {
            return null;
        }
    }

    public synchronized String getSuperConversationName() {
        Conversation superConversation = getSuperConversation();
        if (superConversation == null) {
            return null;
        } else {
            return superConversation.getName();
        }
    }

    protected Conversation newConversation(String conversationName) {
        ConversationImpl impl = new ConversationImpl(conversationName);
        impl.setHotdeployManager(hotdeployManager_);
        impl.setApplicationManager(applicationManager_);
        return impl;
    }

    public synchronized Object getAttribute(String name) {
        if (currentConversation_ != null) {
            return currentConversation_.getAttribute(name);
        } else {
            return null;
        }
    }

    public synchronized void setAttribute(String name, Object value) {
        if (currentConversation_ != null) {
            currentConversation_.setAttribute(name, value);
        }
    }

    public synchronized void begin(String conversationName, String phase) {
        begin(conversationName, phase, true);
    }

    public synchronized void begin(String conversationName, String phase,
            boolean alwaysBegin) {
        if (currentConversation_ != null
                && currentConversation_.getName().equals(conversationName)) {
            // 既に同一名のConversationが始まっている場合、
            if (!alwaysBegin || equals(currentConversation_.getPhase(), phase)) {
                // alwaysBegin == falseなら再beginしないようにする。
                // また、alwaysBegin == trueである場合、フェーズが同じなら何もしない。
                return;
            }
        }

        if (!enteredInSubConversation_) {
            // sub-conversationが開始されている場合*以外*は以前のconversationを破棄する。
            clear();
        }

        currentConversation_ = newConversation(conversationName);
        currentConversation_.setPhase(phase);
        enteredInSubConversation_ = false;
    }

    boolean equals(String s1, String s2) {
        if (s1 == null) {
            return s2 == null;
        } else {
            return s1.equals(s2);
        }
    }

    public synchronized Object end() {
        removeCurrentConversation();
        if (conversationStack_.size() > 0) {
            currentConversation_ = conversationStack_.removeLast();
            return currentConversation_.getReenterResponse();
        } else {
            return null;
        }
    }

    public synchronized boolean isInSubConversation() {
        return (conversationStack_.size() > 0 && conversationStack_.peek()
                .getReenterResponse() != null);
    }

    public synchronized void join(String conversationName, String phase,
            String[] followAfter) {
        if (currentConversation_ == null
                || !currentConversation_.getName().equals(conversationName)) {
            // 現在のconversationが同一conversationではない。
            illegalTransitionDetected();
            return;
        }

        String currentPhase = currentConversation_.getPhase();
        if (equals(currentPhase, phase)) {
            // フェーズが現在のフェーズと同じ場合は何もしない。
            return;
        }

        boolean matched = false;
        for (int i = 0; i < followAfter.length; i++) {
            if (followAfter[i].equals(currentPhase)) {
                matched = true;
                break;
            }
        }
        if (!matched) {
            // 現在のconversationが指定されたフェーズでない。
            illegalTransitionDetected();
            return;
        }

        currentConversation_.setPhase(phase);
    }

    /**
     * 不正な遷移が検出された際に呼び出されるメソッドです。
     * <p>Ymir-0.9.5以前と互換性を保つには、このメソッドをオーバライドして
     * <code>clear()</code>メソッドを呼び出すようにしたConversationsImplのサブクラスを作成し、
     * ConversationManagerImpl#newConversations()メソッドをオーバライドして
     * 上記クラスのインスタンスを生成して返すようにしたConversationManagerImplのサブクラスを作成し、
     * 上記クラスをコンポーネント定義した
     * ymir-component+conversationManager.diconをクラスパスに追加して下さい。
     * </p>
     */
    protected void illegalTransitionDetected() {
        throw new IllegalTransitionRuntimeException();
    }

    void removeCurrentConversation() {
        if (currentConversation_ != null) {
            currentConversation_ = null;
        }
        enteredInSubConversation_ = false;
    }

    void clear() {
        currentConversation_ = null;
        enteredInSubConversation_ = false;
        conversationStack_.clear();
    }

    @Deprecated
    public synchronized void beginSubConversation(String conversationName,
            Object reenterResponse) {
        beginSubConversation(reenterResponse);
    }

    public synchronized void beginSubConversation(Object reenterResponse) {
        if (currentConversation_ == null) {
            throw new RuntimeException("Conversation is not begun");
        }

        currentConversation_.setReenterResponse(reenterResponse);
        conversationStack_.addLast(currentConversation_);
        currentConversation_ = null;
        enteredInSubConversation_ = true;
    }

    LinkedList<Conversation> getConversationStack() {
        return conversationStack_;
    }

    public Iterator<String> getAttributeNames() {
        if (currentConversation_ != null) {
            return currentConversation_.getAttributeNames();
        } else {
            return new ArrayList<String>().iterator();
        }
    }
}
