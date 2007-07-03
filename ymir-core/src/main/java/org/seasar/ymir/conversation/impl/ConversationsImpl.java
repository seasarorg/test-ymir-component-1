package org.seasar.ymir.conversation.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.conversation.IllegalTransitionRuntimeException;

/**
 * このクラスはスレッドセーフです。
 */
public class ConversationsImpl implements Conversations {
    private LinkedList<Conversation> conversationStack_ = new LinkedList<Conversation>();

    private Set<String> stackedConversationNameSet_ = new HashSet<String>();

    private Conversation currentConversation_;

    private String subConversationName_;

    public Conversation getCurrentConversation() {
        return currentConversation_;
    }

    Conversation newConversation(String conversationName) {
        return new ConversationImpl(conversationName);
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
        if (stackedConversationNameSet_.contains(conversationName)) {
            // 同一conversationが入れ子になっている場合は不正遷移。
            clear();
            throw new IllegalTransitionRuntimeException();
        }

        if (!conversationName.equals(subConversationName_)) {
            // 開始されたsub-conversationと一致しない、もしくはsub-conversationが
            // 開始されていない場合は以前のconversationを破棄する。
            clear();
        }

        currentConversation_ = newConversation(conversationName);
        currentConversation_.setPhase(phase);
        subConversationName_ = null;
    }

    public synchronized String end() {
        removeCurrentConversation();
        if (conversationStack_.size() > 0) {
            currentConversation_ = conversationStack_.removeLast();
            updateStackedConversationNameSet();
            return currentConversation_.getReenterPath();
        } else {
            return null;
        }
    }

    public synchronized void join(String conversationName, String phase,
            String[] followAfter) {
        if (currentConversation_ == null
                || !currentConversation_.getName().equals(conversationName)) {
            // 現在のconversationが同一conversationではない。
            clear();
            throw new IllegalTransitionRuntimeException();
        }

        String currentPhase = currentConversation_.getPhase();
        if (phase == null && currentPhase == null || phase != null
                && phase.equals(currentPhase)) {
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
            clear();
            throw new IllegalTransitionRuntimeException();
        }

        currentConversation_.setPhase(phase);
    }

    void removeCurrentConversation() {
        if (currentConversation_ != null) {
            currentConversation_ = null;
        }
        subConversationName_ = null;
    }

    void clear() {
        currentConversation_ = null;
        subConversationName_ = null;
        conversationStack_.clear();
        stackedConversationNameSet_.clear();
    }

    void updateStackedConversationNameSet() {
        stackedConversationNameSet_.clear();
        for (Iterator<Conversation> itr = conversationStack_.iterator(); itr
                .hasNext();) {
            stackedConversationNameSet_.add(itr.next().getName());
        }
    }

    public synchronized void beginSubConversation(String conversationName,
            String reenterPath) {
        currentConversation_.setReenterPath(reenterPath);
        conversationStack_.addLast(currentConversation_);
        stackedConversationNameSet_.add(currentConversation_.getName());
        currentConversation_ = null;
        subConversationName_ = conversationName;
    }
}
