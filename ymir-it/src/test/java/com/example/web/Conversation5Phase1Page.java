package com.example.web;

import org.seasar.ymir.Response;
import org.seasar.ymir.conversation.annotation.Begin;
import org.seasar.ymir.conversation.annotation.Conversation;
import org.seasar.ymir.conversation.annotation.End;
import org.seasar.ymir.response.PassthroughResponse;

@Conversation(name = "subconversation", phase = "phase1")
public class Conversation5Phase1Page {
    @Begin
    public void _get() {
    }

    @End
    public void _get_endVoid() {
    }

    @End
    public Object _get_endObject() {
        return "passthrough:";
    }

    @End
    public String _get_endString() {
        return "passthrough:";
    }

    @End
    public Response _get_endResponse() {
        return new PassthroughResponse();
    }
}
