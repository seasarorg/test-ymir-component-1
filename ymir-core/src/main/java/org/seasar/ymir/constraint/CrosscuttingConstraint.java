package org.seasar.ymir.constraint;

import java.lang.reflect.AnnotatedElement;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.annotation.ConstraintHolder;

/**
 * 複数のPageに対して一括して制約を指定するためのインタフェースです。
 * <p>このインタフェースの実装クラスをコンポーネントとして登録しておくと、
 * フレームワークはコンポーネントの
 * {@link #isConfirmed(Object, org.seasar.ymir.Request, ConstraintType, java.util.Set)}メソッド
 * がtrueを返すPageに関してこの{@link #confirm(Object, Request)}を呼び出します。
 * </p>
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.7
 */
public interface CrosscuttingConstraint extends ConfirmationDecider {
    /**
     * このオブジェクトが実現する制約チェックの種別を返します。
     * 
     * @return 制約チェックの種別。nullを返してはいけません。
     */
    ConstraintType getConstraintType();

    /**
     * 制約チェックを行ないます。
     * <p>指定されたPageオブジェクトに関して制約チェックを行ないます。
     * 制約を満たしていない場合は{@link PermissionDeniedException}
     * または{@link ValidationFailedException}をスローするようにして下さい。
     * </p>
     * 
     * @param component Pageオブジェクト。
     * @param request 現在のRequestオブジェクト。
     * @throws PermissionDeniedException 権限系のチェック制約を満たしていない場合。
     * @throws ValidationFailedException 値のまたは状態の整合性系のチェック制約を満たしていない場合。
     */
    void confirm(Object component, Request request)
            throws PermissionDeniedException, ValidationFailedException;
}
