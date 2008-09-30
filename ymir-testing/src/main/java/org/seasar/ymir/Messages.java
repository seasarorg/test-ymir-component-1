package org.seasar.ymir;

import org.seasar.kvasir.util.collection.I18NPropertyReader;

/**
 * 一まとまりのメッセージを保持するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface Messages extends I18NPropertyReader {
    /**
     * 指定された名前に対応するメッセージを返します。
     * <p>現在のPageに対応するPageコンポーネント名が「hoePage」、メッセージ名が「message」の場合、メッセージは次の順序で探索されます：
     * <ol>
     * <li>現在のロケールに関して、<code>hoePage.message</code>（メッセージ名が「a.b.c」のような場合は<code>a.hoePage.b.c</code>）。
     * 見つからない場合はロケールに関してデフォルトロケールまで再帰的に探索</li>
     * <li>現在のロケールに関して、<code>hoe.message</code>（メッセージ名が「a.b.c」のような場合は<code>a.hoe.b.c</code>）。
     * 見つからない場合はロケールに関してデフォルトロケールまで再帰的に探索</li>
     * <li>現在のロケールに関して、<code>message</code>。
     * 見つからない場合はロケールに関してデフォルトロケールまで再帰的に探索</li>
     * </ol>
     * </p>
     * 
     * @param name 名前。
     * @return メッセージ。存在しない場合はnullを返します。
     */
    String getMessage(String name);
}
