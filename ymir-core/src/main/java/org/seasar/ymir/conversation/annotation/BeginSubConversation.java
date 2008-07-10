package org.seasar.ymir.conversation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.conversation.Conversations;

/**
 * sub-conversationを開始することを表すアノテーションです。
 * <p>このアノテーションが付与されたアクションの実行に先立って、
 * sub-conversationが開始されます。
 * 
 * @see Conversations#beginSubConversation(Object)
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeginSubConversation {
    // TODO 互換性のため残しているが、いつかなくす。
    String name() default "";

    /**
     * 終了時の処理の戻り先を表す文字列です。
     * 
     * @return 終了時の処理の戻り先を表す文字列。
     */
    String reenter();
}
