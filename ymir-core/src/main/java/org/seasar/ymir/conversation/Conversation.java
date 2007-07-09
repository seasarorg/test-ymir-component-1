package org.seasar.ymir.conversation;

/**
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 */
public interface Conversation {
    String getName();

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    String getPhase();

    void setPhase(String phase);

    Object getReenterResponse();

    void setReenterResponse(Object reenterResponse);
}
