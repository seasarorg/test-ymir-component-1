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
     * スコープにある属性値がPageコンポーネントにインジェクトされようとしていることを表します。
     */
    SCOPEOBJECT_INJECTING,

    /**
     * Pageコンポーネントについてアクションが実行されようとしていることを表します。
     */
    ACTION_INVOKING,

    /**
     * Pageコンポーネントについてアクションが実行されたことを表します。
     */
    ACTION_INVOKED,

    /**
     * Pageコンポーネントのプロパティがスコープにアウトジェクトされようとしていることを表します。
     */
    SCOPEOBJECT_OUTJECTING,

    /**
     * Pageコンポーネントのプロパティがスコープにアウトジェクトされたことを表します。
     */
    SCOPEOBJECT_OUTJECTED
}
