package org.seasar.ymir.constraint;

import java.util.Set;

import org.seasar.ymir.Request;

/**
 * 制約チェックを実行するかどうかを決定するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface ConfirmationDecider {
    /**
     * 指定されたタイプの制約チェックを実行するかどうかを決定して返します。
     * <p>指定されたPageオブジェクトに対して、
     * 指定されたタイプの制約チェックを実行するかどうかを決定して返します。
     * </p>
     * 
     * @param page Pageオブジェクト。
     * @param request 現在のRequestオブジェクト。
     * @param type 制約タイプ。
     * @param suppressTypeSet 制約チェックを行なわない制約タイプの集合。
     * @return 制約チェックを実行するかどうか。
     */
    boolean isConfirmed(Object page, Request request, ConstraintType type,
            Set<ConstraintType> suppressTypeSet);
}
