package org.seasar.ymir;

/**
 * フレームワークがDispatchに対して行なうことのできる操作を表すインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.0
 */
public interface FrameworkDispatch extends Dispatch {
    /**
     * パスに対応するPageコンポーネントを設定します。
     * <p>このメソッドはパスに対応するPageコンポーネントを生成した段階でPageコンポーネントを
     * Requestオブジェクトにセットするために用いられます。
     * </p>
     * 
     * @param componentClass Pageコンポーネント。
     */
    void setPageComponent(PageComponent pageComponent);

    /**
     * リクエストされたパスに対応するアクションを設定します。
     * <p>このメソッドは実際にアクションが作成された時点でアクションを
     * Dispatchオブジェクトにセットするために用いられます。
     * </p>
     * 
     * @param originalAction リクエストされたパスに対応するアクション。
     * @see Dispatch#getOriginalAction()
     * @since 1.0.3
     */
    void setOriginalAction(Action originalAction);

    /**
     * アクションを設定します。
     * <p>このメソッドは実際に呼び出されるアクションが決定した段階でアクションを
     * Dispatchオブジェクトにセットするために用いられます。
     * </p>
     * 
     * @param action アクション。
     * @see Dispatch#getAction()
     */
    void setAction(Action action);
}
