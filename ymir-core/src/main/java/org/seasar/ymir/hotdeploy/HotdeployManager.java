package org.seasar.ymir.hotdeploy;

import org.seasar.ymir.hotdeploy.fitter.HotdeployFitter;

public interface HotdeployManager {
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
}
