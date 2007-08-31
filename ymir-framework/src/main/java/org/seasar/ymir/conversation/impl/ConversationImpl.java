package org.seasar.ymir.conversation.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.ymir.conversation.Conversation;

/**
 * このクラスはスレッドセーフです。
 */
public class ConversationImpl implements Conversation {
    private String name_;

    private Map<String, Object> attributeMap_ = new ConcurrentHashMap<String, Object>();

    private String phase_;

    private Object reenterResponse_;

    public ConversationImpl(String name) {
        name_ = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name_;
    }

    public Object getAttribute(String name) {
        return attributeMap_.get(name);
    }

    public void setAttribute(String name, Object value) {
        if (value != null) {
            attributeMap_.put(name, value);
        } else {
            attributeMap_.remove(name);
        }
    }

    public String getPhase() {
        return phase_;
    }

    public synchronized void setPhase(String phase) {
        phase_ = phase;
    }

    public Object getReenterResponse() {
        return reenterResponse_;
    }

    public void setReenterResponse(Object reenterResponse) {
        reenterResponse_ = reenterResponse;
    }
}
