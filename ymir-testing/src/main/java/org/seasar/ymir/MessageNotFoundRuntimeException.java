package org.seasar.ymir;

import java.util.Locale;

/**
 * Messagesコンポーネントに存在しないメッセージリソースを取得しようとした際にスローされる例外クラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class MessageNotFoundRuntimeException extends RuntimeException {

    private String messagesName_ = Globals.NAME_MESSAGES;

    private String messageKey_;

    private Locale locale_;

    private static final long serialVersionUID = 4840369946638949359L;

    /**
     * このクラスのオブジェクトを構築します。
     */
    public MessageNotFoundRuntimeException() {
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message 説明文。
     * @param cause 元のThrowable。
     */
    public MessageNotFoundRuntimeException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message 説明文。
     */
    public MessageNotFoundRuntimeException(String message) {
        super(message);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param cause 元のThrowable。
     */
    public MessageNotFoundRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * どのロケールに関するメッセージが存在しなかったのかを返します。
     * 
     * @return ロケール。
     */
    public Locale getLocale() {
        return locale_;
    }

    /**
     * どのロケールに関するメッセージが存在しなかったのかを設定します。
     * 
     * @param locale ロケール。
     * @return このオブジェクト。
     */
    public MessageNotFoundRuntimeException setLocale(Locale locale) {
        locale_ = locale;
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
    public MessageNotFoundRuntimeException setMessageKey(String messageKey) {
        messageKey_ = messageKey;
        return this;
    }

    /**
     * メッセージが存在しなかったMessagesコンポーネントの名前を返します。
     * 
     * @return Messagesコンポーネントの名前。
     */
    public String getMessagesName() {
        return messagesName_;
    }

    /**
     * メッセージが存在しなかったMessagesコンポーネントの名前を設定します。
     * 
     * @param messagesName Messagesコンポーネントの名前。
     * @return このオブジェクト。
     */
    public MessageNotFoundRuntimeException setMessagesName(String messagesName) {
        messagesName_ = messagesName;
        return this;
    }
}
