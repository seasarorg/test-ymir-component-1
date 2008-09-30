package org.seasar.ymir.conversation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.conversation.Conversations;

/**
 * conversationまたはsub-conversationを開始することを表すアノテーションです。
 * <p>このアノテーションが付与されたアクションの実行の終了後にconversationが終了されます。
 * 現在のconversationがsub-conversationである場合は、
 * sub-conversationの開始時に設定された戻り値を表すオブジェクトがアクションメソッドの返り値として返されます。
 * 元もとのアクションメソッドの返り値は破棄されます。
 * </p>
 * 
 * @see Conversations#end()
 * @author YOKOTA Takehiko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface End {
}
