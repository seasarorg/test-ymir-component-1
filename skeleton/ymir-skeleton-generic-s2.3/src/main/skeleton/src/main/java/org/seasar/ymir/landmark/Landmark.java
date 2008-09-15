package org.seasar.ymir.landmark;

/**
 * このクラスはreleaseモードの時にPageクラス等をS2コンテナに自動登録する際の基準となるクラスです。
 * Seasar2.3との組み合わせの場合はフレームワークはPageクラスを自動登録しないため基準クラスは不要
 * ですが、YmirをSeasar2.4でも2.3でも動作させることができるようにしている仕組み上現状ではこのクラスを
 * 削除してしまうとYmirが動作しなくなりますので、絶対に削除しないで下さい！
 */
public class Landmark
{
}
