package org.seasar.ymir;

/**
 * Pageコンポーネントの処理フェーズを表すenumです。
 * 
 * @author YOKOTA Takehiko
 */
public enum Phase {
    /**
     * Pageコンポーネントが生成されたことを表します。
     */
    PAGECOMPONENT_CREATED,

    /**
     * スコープからオブジェクトがインジェクトされたことを表します。
     * ポピュレートはまだ行なわれていません。
     */
    OBJECT_INJECTED,

    /**
     * Pageコンポーネントについてアクションが実行されようとしていることを表します。
     */
    ACTION_INVOKING,

    /**
     * Pageコンポーネントについてアクションが実行されたことを表します。
     */
    ACTION_INVOKED,
}
