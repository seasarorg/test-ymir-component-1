package org.seasar.ymir.history;

import java.io.Serializable;

public class Conversation implements Serializable {
    private static final long serialVersionUID = -8955212478519992153L;

    private String name_;

    private String phase_;

    public Conversation() {
    }

    public Conversation(String name, String phase) {
        name_ = name;
        phase_ = phase;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        name_ = name;
    }

    public String getPhase() {
        return phase_;
    }

    public void setPhase(String phase) {
        phase_ = phase;
    }
}
