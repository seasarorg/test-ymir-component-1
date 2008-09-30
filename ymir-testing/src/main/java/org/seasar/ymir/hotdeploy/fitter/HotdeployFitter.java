package org.seasar.ymir.hotdeploy.fitter;

/**
 * S2ContainerのHOT Deployの利用時に起きるオブジェクトのクラスミスマッチを解消するための処理を定義するためのインタフェースです。
 * <p>リクエストスコープよりも範囲の広いスコープに属する属性の値をスコープから取り出す場合、
 * フレームワークは取り出した値オブジェクトの型とマッチするHotdeployFitterを使ってオブジェクトを差し替えます。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface HotdeployFitter<T> {
    /**
     * このHotdeployFitterが処理する対象となるクラスを返します。
     * 
     * @return このHotdeployFitterが処理する対象となるクラス。
     */
    Class<T> getTargetClass();

    /**
     * HOT Deploy後の新しいクラスのオブジェクトを作成し、
     * 指定されたオブジェクトの内容をコピーして返します。
     * <p>コピーの必要がない場合は指定されたオブジェクトがそのまま返されることがありますが、
     * オブジェクトの中身は指し変わっていることがあります。
     * </p>
     * 
     * @param value コピー元のオブジェクト。
     * @return コピーされたオブジェクト。
     */
    T copy(T value);
}
