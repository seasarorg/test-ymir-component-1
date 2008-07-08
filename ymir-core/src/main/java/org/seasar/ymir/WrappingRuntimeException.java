package org.seasar.ymir;

/**
 * 例外をラップする例外クラスです。
 * <p>このクラスはフレームワークによって利用されます。
 * 通常アプリケーションコードからこのクラスのオブジェクトを構築してスローすることはありません。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class WrappingRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -1904957525045873876L;

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message メッセージ文字列。
     * @param cause もともとの例外オブジェクト。
     */
    public WrappingRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param cause もともとの例外オブジェクト。
     */
    public WrappingRuntimeException(Throwable cause) {
        super(cause);
    }
}
