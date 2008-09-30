package org.seasar.ymir;

import java.util.Map;

/**
 * フレームワークがDispatchに対して行なうことのできる操作を表すインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.0
 * @see Request
 */
public interface FrameworkRequest extends Request {
    /**
     * 拡張リクエストパラメータを格納しているMapを設定します。
     *
     * @param extendedParameterMap Mapオブジェクト。
     */
    void setExtendedParameterMap(Map<String, Object> extendedParameterMap);
}
