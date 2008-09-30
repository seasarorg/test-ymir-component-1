package org.seasar.ymir.hotdeploy;

import org.seasar.ymir.hotdeploy.fitter.HotdeployFitter;

/**
 * S2ContainerのHOT Deployに関する操作を提供するためのインタフェースです。
 * <p>通常このインタフェースはフレームワーク中から利用されます。
 * このインタフェースをアプリケーションコードから利用する必要はほとんどありません。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface HotdeployManager {
    /**
     * HotdeployFittersを設定します。
     * 
     * @param hotdeployFitters HotdeployFitters。
     */
    void setHotdeployFitters(HotdeployFitter<?>[] hotdeployFitters);

    /**
     * 指定されたオブジェクトを現在のコンテキストクラスローダからロードされるクラスと整合性が取れるように変換します。
     * <p>オブジェクトは以下のルールで変換されます。</p>
     * <ul>
     * <li>nullは変換されません。</li>
     * <li>HotdeployClassLoader由来のクラスは現在のコンテキストクラスローダからロードした
     * 同名のクラスのオブジェクトに変換されます。
     * ただしHotdeployManagerにセットされているいずれかのHotdeployFitterのtargetClassを実装している場合は、Hotdeploy由来かどうかに関わらず変換されます。</li>
     * <li>配列の場合は中身の要素も上記ルールに従って変換されます。</li>
     * <p>変換はオブジェクトの持つ各フィールドについて再帰的に行なわれますが、
     * 変換されなかったフィールドについてはそれ以上再帰的な変換を行いません。</p>
     * 
     * @param value 変換したいオブジェクト。nullでも構いません。nullでない場合、オブジェクトのクラスは
     * デフォルトコンストラクタを持つ必要があります。
     * @return 変換結果のオブジェクト。
     */
    Object fit(Object value);

    /**
     * イベントリスナを登録します。
     * 
     * @param listener イベントリスナ。nullを指定してはいけません。
     * @since 0.9.6
     */
    void addEventListener(HotdeployEventListener listener);

    /**
     * 登録されているイベントリスナを返します。
     * 
     * @return 登録されているイベントリスナの配列。nullを返すことはありません。
     * @since 0.9.6
     */
    HotdeployEventListener[] getEventListeners();
}
