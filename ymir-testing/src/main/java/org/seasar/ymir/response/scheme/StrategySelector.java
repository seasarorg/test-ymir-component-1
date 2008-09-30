package org.seasar.ymir.response.scheme;

import org.seasar.ymir.response.constructor.impl.StringResponseConstructor;

/**
 * 文字列からResponseオブジェクトを構築するためのStrategyクラスを管理するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 *
 * @see StringResponseConstructor
 * @see Strategy
 * @author YOKOTA Takehiko
 */
public interface StrategySelector {
    /**
     * 指定されたスキーム文字列に対応する{@link Strategy}を返します。
     * 
     * @param scheme スキーム文字列。
     * @return Strategyオブジェクト。
     * 見つからなかった場合はnullを返します。
     */
    Strategy getStrategy(String scheme);
}