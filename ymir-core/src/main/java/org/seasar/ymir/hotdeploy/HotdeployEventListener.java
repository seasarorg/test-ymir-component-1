package org.seasar.ymir.hotdeploy;

/**
 * HOT Deployに関するイベント発生時に処理を追加するためのリスナインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @since 0.9.6
 * @author YOKOTA Takehiko
 */
public interface HotdeployEventListener {
    /**
     * HOT Deploy処理の開始時に呼び出されます。
     * <p>Seasar2の場合はリクエストを受けた時点でHOT Deploy処理が開始されます。</p>
     */
    void start();

    /**
     * HOT Deploy処理の終了時に呼び出されます。
     * <p>Seasar2の場合はレスポンスを返した時点でHOT Deploy処理が終了します。</p>
     */
    void stop();
}
