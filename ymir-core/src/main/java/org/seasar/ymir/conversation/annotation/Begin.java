package org.seasar.ymir.conversation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.conversation.Conversations;

/**
 * conversationを開始することを表すアノテーションです。
 * <p>このアノテーションが付与されたアクションの実行に先立って、
 * conversationが開始されます。
 * 
 * @see Conversations#begin(String, String, boolean)
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Begin {
    /**
     * 既に同一名のconversationが開始されていてかつフェーズが異なる場合に再度開始するかどうかです。
     * 
     * @return 既に同一名のconversationが開始されていて
     * かつフェーズが異なる場合に再度開始するかどうか。
     * @see Conversations#begin(String, String, boolean)
     */
    boolean alwaysBegin() default true;
}
