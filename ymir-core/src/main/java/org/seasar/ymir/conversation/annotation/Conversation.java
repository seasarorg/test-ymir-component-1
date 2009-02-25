package org.seasar.ymir.conversation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.ymir.annotation.Bool;

/**
 * 複数のリクエストからなる処理の一単位を定義するためのアノテーションです。
 * <p>カンバセーションはHTTPリクエストよりも大きく、HTTPセッションよりも小さい処理の単位です。
 * 例えば新規登録処理を「入力画面」「確認画面」「完了画面」で構成する場合等に利用することができます。
 * </p>
 * <p>1つのカンバセーションには複数のPageクラスを関連付けることができます。
 * カンバセーションは名前を持っており、同じ名前が指定されたConversationアノテーションが付与された
 * Pageが同一カンバセーションに属することになります。
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
     * カンバセーションの名前です。
     * <p>カンバセーション名は任意の文字列です。
     * </p>
     * 
     * @return カンバセーションの名前。
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
     * <p>直前のリクエストに対応するPageが属するカンバセーションが同一カンバセーションで、
     * かつ直前のリクエストに対応するPageに割り当てられたフェーズの名前が、
     * このプロパティで指定されたフェーズ名のどれかに一致する必要があることを指定します。
     * これによって画面遷移を制御することができます。
     * </p>
     * <p>このプロパティが指定されていない場合は、
     * 直前のリクエストに対応するPageが属するカンバセーションが同一カンバセーションで、
     * かつ直前のリクエストに対応するPageに割り当てられたフェーズの名前が、
     * このPageに割り当てられたフェーズの名前と一致する必要があります。
     * ただし{@link Begin}アノテーションが付与されている場合はその限りではありません。
     * </p>
     * 
     * @return 直前のリクエストに対応するPageに割り当てられているべきフェーズ。
     */
    String[] followAfter() default {};

    /**
     * ブラウザの「戻る」ボタンを押された場合の遷移を許可するかどうかです。
     * <p>ブラウザの「戻る」ボタンが押された場合、アプリケーションが想定していないフェーズからの遷移が行なわれることがあります。
     * このプロパティをTRUEにすることで、このようなブラウザの「戻る」ボタンが押されることで発生しうる想定外の遷移を許容するようになります。
     * </p>
     * <p>具体的には、例えばカンバセーションC1のフェーズP2からブラウザの「戻る」ボタンでC1のフェーズP1の画面に戻ってポストバックのボタンを押した場合、
     * C1のP1のPageクラスのConversationアノテーションのfollowAfterにP2が指定されていない場合でも不正な遷移とみなさないようになります。
     * また、C1から始まったサブカンバセーションCSからブラウザの「戻る」ボタンでC1の画面に戻ってポストバックのボタンを押した場合、
     * 不正な遷移とみなさずCSを破棄してC1に状態を戻します。
     * </p>
     * <p>このプロパティがNULLである場合は、アプリケーションの設定に従います。
     * 具体的には、app.propertiesの<code>core.conversation.acceptBrowsersBackButton</code>プロパティに従います。
     * これが指定されていない場合はFALSEとみなされます。
     * </p>
     * 
     * @return ブラウザの「戻る」ボタンを押された場合の遷移を許可するかどうか。
     * @since 1.0.2
     */
    Bool acceptBrowsersBackButton() default Bool.NULL;
}
