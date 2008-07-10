package org.seasar.ymir.constraint;

import org.seasar.ymir.Notes;

/**
 * 値または状態が適切でない場合にスローされる例外クラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class ValidationFailedException extends ConstraintViolatedException {
    private static final long serialVersionUID = -835167364152293726L;

    /**
     * このクラスのオブジェクトを構築します。
     */
    public ValidationFailedException() {
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message エラーメッセージ文字列。
     */
    public ValidationFailedException(String message) {
        super(message);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param cause 元になった例外。
     */
    public ValidationFailedException(Throwable cause) {
        super(cause);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message エラーメッセージ文字列。
     * @param cause 元になった例外。
     */
    public ValidationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * このクラスのオブジェクトを構築します。
     *
     * @param notes エラーメッセージを表すNotesオブジェクト。
     */
    public ValidationFailedException(Notes notes) {
        super(notes);
    }
}
