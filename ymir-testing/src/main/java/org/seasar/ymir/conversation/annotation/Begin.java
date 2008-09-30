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
     * 既に同一名のconversationが開始されていても再度開始するかどうかです。
     * <p>直前のconversationの名前が新たなconversationの名前と一致する場合は、
     * alwaysBeginがfalseであればconversationを開始しません。
     * alwaysBeginがtrueであれば常にconversationを開始します。
     * （ただし直前のフェーズ名と新しいフェーズ名が一致した場合はconversationを開始しません。）
     * </p>
     * 
     * @return 既に同一名のconversationが開始されていても
     * 再度開始するかどうか。
     * @see Conversations#begin(String, String, boolean)
     */
    boolean alwaysBegin() default true;
}
