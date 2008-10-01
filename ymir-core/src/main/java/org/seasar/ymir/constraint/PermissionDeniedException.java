package org.seasar.ymir.constraint;

import org.seasar.ymir.message.Notes;

/**
 * 適切な権限を持っていない場合にスローされる例外クラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class PermissionDeniedException extends ConstraintViolatedException {
    private static final long serialVersionUID = -7495239316902981080L;

    /**
     * このクラスのオブジェクトを構築します。
     */
    public PermissionDeniedException() {
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message エラーメッセージ文字列。
     */
    public PermissionDeniedException(String message) {
        super(message);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param cause 元になった例外。
     */
    public PermissionDeniedException(Throwable cause) {
        super(cause);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message エラーメッセージ文字列。
     * @param cause 元になった例外。
     */
    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * このクラスのオブジェクトを構築します。
     *
     * @param notes エラーメッセージを表すNotesオブジェクト。
     */
    public PermissionDeniedException(Notes notes) {
        super(notes);
    }
}
