package org.seasar.ymir.history;

import java.io.Serializable;

import org.seasar.ymir.Path;

/**
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public interface HistoryElement extends Serializable {
    Path getPath();

    Class<?> getPageClass();

    Conversation getConversation();
}
