package org.seasar.ymir.impl;

import org.seasar.ymir.AppDiconComponent;
import org.seasar.ymir.Request;
import org.seasar.ymir.message.Messages;

public class AppDiconComponentImpl implements AppDiconComponent {
    private Messages messages_;

    private Request ymirRequest_;

    public Messages getMessages() {
        return messages_;
    }

    public void setMessages(Messages messages) {
        messages_ = messages;
    }

    public Request getYmirRequest() {
        return ymirRequest_;
    }

    public void setYmirRequest(Request ymirRequest) {
        ymirRequest_ = ymirRequest;
    }
}
