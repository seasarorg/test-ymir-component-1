package org.seasar.ymir.constraint;

/**
 * 複数のPageに対して一括して制約を指定するためのインタフェースです。
 * <p>このインタフェースの実装クラスをコンポーネントとして登録しておくと、
 * フレームワークはConstraintBundleコンポーネントの
 * {@link ConfirmationDecider#isConfirmed(Object, org.seasar.ymir.Request, ConstraintType, java.util.Set)}メソッド
 * がtrueを返すPageに関してこのConstraintBundleクラスに付与されている制約アノテーションに従った制約チェックを行ないます。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface ConstraintBundle extends ConfirmationDecider {
}
