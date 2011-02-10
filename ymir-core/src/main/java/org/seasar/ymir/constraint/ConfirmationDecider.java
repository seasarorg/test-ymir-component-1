package org.seasar.ymir.constraint;

import java.util.Set;

import org.seasar.ymir.Request;
import org.seasar.ymir.constraint.annotation.SuppressConstraints;

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
     * <p><code>type</code>で指定された制約タイプは、
     * このConfirmationDecideに紐付けられている制約のタイプです。
     * この値は{@link SuppressConstraints}に従って制約チェックを行なうかどうかを決定する際に
     * 参照されます。
     * </p>
     * <p><code>suppressTypeSet</code>で指定された制約タイプは、
     * アクションの実行に際して{@link SuppressConstraints}によってチェックを抑制することを指定された制約タイプです。
     * 例えばこのConfirmationDeciderが権限チェックに関する制約に紐付けられている場合は、
     * <code>suppressTypeSet</code>に{@link ConstraintType#PERMISSION}が
     * 含まれているのであればこのメソッドはfalseを返すべきです。
     * ただし、抑制されているかどうかに関係なくチェックすべき制約にこのConfirmationDeciderが紐づけられている場合は、
     * <code>suppressTypeSet</code>を無視して構いません。
     * </p>
     * 
     * @param component Pageオブジェクト。
     * @param request 現在のRequestオブジェクト。
     * @param type 制約タイプ。
     * @param suppressTypeSet 制約チェックを行なわない制約タイプの集合。
     * @return 制約チェックを実行するかどうか。
     */
    boolean isConfirmed(Object component, Request request, ConstraintType type,
            Set<ConstraintType> suppressTypeSet);
}
