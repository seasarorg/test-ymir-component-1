package org.seasar.ymir;

/**
 * HTTPリクエストに対応するPageクラスのアクションメソッドが見つからなかった場合にスローされる例外クラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class ActionNotFoundException extends PageNotFoundException {
    private static final long serialVersionUID = 5885040967668460745L;

    private String actionName_;

    /**
     * このクラスのオブジェクトを構築します。
     */
    public ActionNotFoundException(String path) {
        super(path);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param actionName アクション名。
     */
    public ActionNotFoundException(String path, String actionName) {
        super(path);
        setActionName(actionName);
    }

    /**
     * アクション名を返します。
     * 
     * @return アクション名。
     */
    public String getActionName() {
        return actionName_;
    }

    /**
     * アクション名を設定します。
     * 
     * @param actionName アクション名。
     * @return このオブジェクト自身。
     */
    public ActionNotFoundException setActionName(String actionName) {
        actionName_ = actionName;
        return this;
    }
}
