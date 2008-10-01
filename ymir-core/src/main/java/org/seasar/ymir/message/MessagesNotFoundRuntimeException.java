package org.seasar.ymir.message;

/**
 * 指定されたMessagesコンポーネントが存在しなかった場合にスローされる例外クラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class MessagesNotFoundRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1741770699207141070L;

    private String messagesName_;

    private String messageKey_;

    /**
     * このクラスのオブジェクトを構築します。
     */
    public MessagesNotFoundRuntimeException() {
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message 説明文。
     * @param cause 元のThrowable。
     */
    public MessagesNotFoundRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message 説明文。
     */
    public MessagesNotFoundRuntimeException(String message) {
        super(message);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param cause 元のThrowable。
     */
    public MessagesNotFoundRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * 存在しなかったMessagesコンポーネントの名前を返します。
     * 
     * @return 存在しなかったMessagesコンポーネントの名前。
     */
    public String getMessagesName() {
        return messagesName_;
    }

    /**
     * Messagesコンポーネントの名前を設定します。
     * 
     * @param messagesName Messagesコンポーネントの名前。
     * @return このオブジェクト。
     */
    public MessagesNotFoundRuntimeException setMessagesName(String messagesName) {
        messagesName_ = messagesName;
        return this;
    }

    /**
     * 存在しなかったメッセージのキーを返します。
     * 
     * @return メッセージのキー。
     */
    public String getMessageKey() {
        return messageKey_;
    }

    /**
     * 存在しなかったメッセージのキーを設定します。
     * 
     * @param messageKey メッセージのキー。
     * @return このオブジェクト。
     */
    public MessagesNotFoundRuntimeException setMessageKey(String messageKey) {
        messageKey_ = messageKey;
        return this;
    }
}
