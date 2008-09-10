package org.seasar.ymir.cache;

import java.util.Map;

import org.seasar.ymir.hotdeploy.HotdeployManager;

/**
 * キャッシュ用のコレクションクラスを管理するためのインタフェースです。
 * 
 * @author yokota
 * @since 0.9.6
 */
public interface CacheManager {
    /**
     * キャッシュ用のMapを生成して返します。
     * <p>このメソッドが返すMapはキャッシュ用であり、一度登録されたエントリがいつまでも
     * 登録されて続けている保証はありません。
     * </p>
     * <p>生成したMapは自動的に{@link HotdeployManager}に登録され、
     * HOT Deploy有効時には毎リクエスト終了毎に内容が自動的にクリアされるようになります。
     * </p>
     * <p>返されるMapは同期化されています。</p>
     * <p>返されるMapはキーや値としてnullを指定してはいけないMapである可能性があります。
     * </p>
     * 
     * @param <K> 生成するMapのキーの型です。
     * @param <V> 生成するMapの値の型です。
     * @return 生成したMapオブジェクトです。
     */
    <K, V> Map<K, V> newMap();
}
