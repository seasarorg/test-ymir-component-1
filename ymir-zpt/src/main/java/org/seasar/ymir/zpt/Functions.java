package org.seasar.ymir.zpt;

/**
 * Java式で記述可能なファンクションを持つクラスを表すインタフェースです。
 * <p>ファンクションを定義したい場合はこのインタフェースの実装クラスを作成して、
 * クラスをapp.diconにコンポーネント登録して下さい。
 * 登録されたクラスが持つ全てのstatic methodがファンクションとして認識され、
 * Java式の中で利用可能になります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public interface Functions {
}
