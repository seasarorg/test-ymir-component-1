package org.seasar.ymir;

import org.seasar.ymir.message.Messages;

public interface AppDiconComponent {
    Messages getMessages();

    Request getYmirRequest();
}
