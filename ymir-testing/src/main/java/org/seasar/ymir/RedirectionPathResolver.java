package org.seasar.ymir;

/**
 * {@link Response}が持つリダイレクションパスを加工するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @see Response
 * @author YOKOTA Takehiko
 */
public interface RedirectionPathResolver {
    /**
     * 指定されたリダイレクトパスを最終的な形式に加工して返します。
     * 
     * @param path パス。
     * @param request 現在のRequestオブジェクト。
     * @return 最終的なパス。
     */
    String resolve(String path, Request request);
}
