package org.seasar.ymir.conversation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 複数のリクエストからなる処理の一単位を定義するためのアノテーションです。
 * <p>ConversationはHTTPリクエストよりも大きく、HTTPセッションよりも小さい処理の単位です。
 * 例えば新規登録処理を「入力画面」「確認画面」「完了画面」で構成する場合等に利用することができます。
 * </p>
 * <p>1つのConversationには複数のPageクラスを関連付けることができます。
 * Conversationは名前を持っており、同じ名前が指定されたConversationアノテーションが付与された
 * Pageが同一Conversationに属することになります。
 * </p>
 * <p>Pageにフェーズと呼ばれる文字列を割り当てておき、
 * フェーズ間の遷移に関する制約を設定することで、
 * Page間の遷移順序などを指定することもできます。
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Conversation {
    /**
     * Conversationの名前です。
     * <p>Conversation名は任意の文字列です。
     * </p>
     * 
     * @return Conversationの名前。
     */
    String name();

    /**
     * このアノテーションが付与されたPageに割り当てるフェーズ名です。
     * <p>フェーズ名は任意の文字列です。
     * </p>
     * 
     * @return フェーズ名。
     */
    String phase() default "";

    /**
     * 直前のリクエストに対応するPageに割り当てられているべきフェーズです。
     * <p>直前のリクエストに対応するPageが属するConversationが同一Conversationで、
     * かつ直前のリクエストに対応するPageに割り当てられたフェーズの名前が、
     * このプロパティで指定されたフェーズ名のどれかに一致する必要があることを指定します。
     * これによって画面遷移を制御することができます。
     * </p>
     * <p>このプロパティが指定されていない場合は、
     * 直前のリクエストに対応するPageが属するConversationが同一Conversationで、
     * かつ直前のリクエストに対応するPageに割り当てられたフェーズの名前が、
     * このPageに割り当てられたフェーズの名前と一致する必要があります。
     * ただし{@link Begin}アノテーションが付与されている場合はその限りではありません。
     * </p>
     * 
     * @return 直前のリクエストに対応するPageに割り当てられているべきフェーズ。
     */
    String[] followAfter() default {};
}
