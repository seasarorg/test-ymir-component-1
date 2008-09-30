package org.seasar.ymir.constraint;

/**
 * 制約のタイプを表すenumです。
 * 
 * @author YOKOTA Takehiko
 */
public enum ConstraintType {
    /**
     * 「バリデーション」タイプです。
     * <p>なんらかの値または状態が適切であるかどうかをチェックすることを表します。
     * </p>
     */
    VALIDATION,

    /**
     * 「権限」タイプです。
     * <p>アクションを実行するための権限を持っているかどうかをチェックすることを表します。
     * </p>
     */
    PERMISSION;
}
