package org.seasar.ymir.conversation;

/**
 * conversationに関して不正な遷移が検出された場合にスローされる例外クラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class IllegalTransitionRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 3084715814456978818L;

    /**
     * このクラスのオブジェクトを構築します。
     */
    public IllegalTransitionRuntimeException() {
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message エラーメッセージ文字列。
     * @param cause 元もとの例外オブジェクト。
     */
    public IllegalTransitionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message エラーメッセージ文字列。
     */
    public IllegalTransitionRuntimeException(String message) {
        super(message);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param cause 元もとの例外オブジェクト。
     */
    public IllegalTransitionRuntimeException(Throwable cause) {
        super(cause);
    }
}
