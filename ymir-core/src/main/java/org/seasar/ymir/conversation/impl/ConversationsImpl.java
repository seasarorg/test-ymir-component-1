package org.seasar.ymir.conversation.impl;

import java.util.LinkedList;

import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.conversation.IllegalTransitionRuntimeException;

/**
 * このクラスはスレッドセーフです。
 */
public class ConversationsImpl implements Conversations {
    private LinkedList<Conversation> conversationStack_ = new LinkedList<Conversation>();

    private Conversation currentConversation_;

    private String subConversationName_;

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
        if (currentConversation_ != null
                && currentConversation_.getName().equals(conversationName)
                && equals(currentConversation_.getPhase(), phase)) {
            // 既に同一名のConversationが始まっていてかつフェーズが同じなら何もしない。
            return;
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

    public synchronized void join(String conversationName, String phase,
            String[] followAfter) {
        if (currentConversation_ == null
                || !currentConversation_.getName().equals(conversationName)) {
            // 現在のconversationが同一conversationではない。
            clear();
            throw new IllegalTransitionRuntimeException();
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
    }

    public synchronized void beginSubConversation(String conversationName,
            Object reenterResponse) {
        currentConversation_.setReenterResponse(reenterResponse);
        conversationStack_.addLast(currentConversation_);
        currentConversation_ = null;
        subConversationName_ = conversationName;
    }

    LinkedList<Conversation> getConversationStack() {
        return conversationStack_;
    }
}
