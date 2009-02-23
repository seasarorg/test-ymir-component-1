package org.seasar.ymir.conversation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.conversation.BeginCondition;
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
     * conversationを開始する条件です。
     * 
     * @return conversationを開始する条件。
     * @see Conversations#begin(String, String, BeginCondition)
     * @since 1.0.2
     */
    BeginCondition where() default BeginCondition.ALWAYS;
}
