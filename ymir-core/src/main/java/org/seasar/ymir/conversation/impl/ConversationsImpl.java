package org.seasar.ymir.conversation.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.seasar.ymir.conversation.Conversation;
import org.seasar.ymir.conversation.Conversations;
import org.seasar.ymir.conversation.IllegalTransitionRuntimeException;

/**
 * このクラスはスレッドセーフです。
 */
public class ConversationsImpl implements Conversations {
    private Map<String, Conversation> conversationMap_ = new HashMap<String, Conversation>();

    private LinkedList<Conversation> conversationStack_ = new LinkedList<Conversation>();

    private Set<String> stackedConversationNameSet_ = new HashSet<String>();

    private Conversation currentConversation_;

    private String subConversation_;

    public Conversation getConversation(String conversationName) {
        return getConversation(conversationName, true);
    }

    public synchronized Conversation getConversation(String conversationName,
            boolean create) {
        Conversation conversation = conversationMap_.get(conversationName);
        if (conversation == null && create) {
            conversation = newConversation(conversationName);
            conversationMap_.put(conversationName, conversation);
        }
        return conversation;
    }

    Conversation newConversation(String conversationName) {
        return new ConversationImpl(conversationName);
    }

    public Object getAttribute(String conversationName, String name) {
        Conversation conversation = getConversation(conversationName, false);
        if (conversation == null) {
            return null;
        } else {
            return conversation.getAttribute(name);
        }
    }

    public void setAttribute(String conversationName, String name, Object value) {
        getConversation(conversationName).setAttribute(name, value);
    }

    public String finish() {
        // TODO Auto-generated method stub 実装足りてない。
        return null;
    }

    public synchronized void join(String conversationName, String phase,
            boolean alwaysCreateConversation, String[] followAfter) {
        if (followAfter.length > 0) {
            if (currentConversation_ == null
                    || !currentConversation_.getName().equals(conversationName)) {
                // 現在のconversationが同一conversationではない。
                clear();
                throw new IllegalTransitionRuntimeException();
            }
            String currentPhase = currentConversation_.getPhase();
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
        } else {
            if (stackedConversationNameSet_.contains(conversationName)) {
                // 同一conversationが入れ子になっている場合は不正遷移。
                clear();
                throw new IllegalTransitionRuntimeException();
            }

            if (!conversationName.equals(subConversation_)) {
                // 開始されたsub-conversationと一致しない、もしくはsub-conversationが
                // 開始されていない場合は以前のconversationを破棄する。
                // ただしalwaysCreateConversationがfalseでかつ現在のconversationが
                // 指定されたconversationと同じ場合だけはconversationを破棄しない。
                if (alwaysCreateConversation
                        || currentConversation_ == null
                        || !currentConversation_.getName().equals(
                                conversationName)) {
                    clear();
                }
            }

            currentConversation_ = newConversation(conversationName);
            currentConversation_.setPhase(phase);
            conversationMap_.put(conversationName, currentConversation_);
            subConversation_ = null;
        }
    }

    public void removeCurrentConversation() {
        if (currentConversation_ != null) {
            currentConversation_ = null;
            conversationMap_.remove(currentConversation_);
        }
        subConversation_ = null;
    }

    public void clear() {
        currentConversation_ = null;
        subConversation_ = null;
        conversationMap_.clear();
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

    public void beginSubConversation(String conversationName, String reenterPath) {
        // TODO Auto-generated method stub 実装足りてない。
        conversationStack_.addLast(currentConversation_);
        stackedConversationNameSet_.add(conversationName);
    }
}
