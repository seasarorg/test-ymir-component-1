package org.seasar.ymir.conversation;

import java.io.Serializable;
import java.util.Iterator;

/**
 * 現在のセッションに関するconversation情報を表すインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @see Conversation
 * @author YOKOTA Takehiko
 */
public interface Conversations extends Serializable {
    /**
     * 現在のconversationを表すConversationオブジェクトを返します。
     * <p>conversationに参加していない場合はnullを返します。
     * </p>
     * 
     * @return 現在のconversationを表すConversationオブジェクト。
     */
    Conversation getCurrentConversation();

    /**
     * 現在のconversationの名前を返します。
     * <p>conversationに参加していない場合はnullを返します。
     * </p>
     * 
     * @return 現在のconversationの名前。
     */
    String getCurrentConversationName();

    /**
     * super-conversationを表すConversationオブジェクトを返します。
     * <p>現在のconversationがsub-conversationでない場合はnullを返します。
     * </p>
     * 
     * @return super-conversationを表すConversationオブジェクト。
     */
    Conversation getSuperConversation();

    /**
     * super-conversationの名前を返します。
     * <p>現在のconversationがsub-conversationでない場合はnullを返します。
     * </p>
     * 
     * @return super-conversationを表すConversationオブジェクト。
     */
    String getSuperConversationName();

    /**
     * 現在のconversationにバインドされている属性のうち、
     * 指定されている名前の属性の値を返します。
     * 
     * @param name 属性の名前。
     * @return 属性の値。存在しない場合はnullを返します。
     */
    Object getAttribute(String name);

    /**
     * 指定された名前と値を持つ属性を現在のconversationにバインドします。
     * 
     * @param name 属性の名前。
     * @param value 属性の値。nullを指定すると属性が削除されます。
     */
    void setAttribute(String name, Object value);

    /**
     * 現在のconversationにバインドされている属性のうち、
     * 指定されている名前の属性の値を返します。
     * <p>{@link #getAttribute(String)}とは異なり、
     * Hotdeployモードの時でも補正せずにそのままオブジェクトを返します。
     * 従って、HotdeployモードではClassCastExceptionがスローされることがあります。
     * 通常は{@link #getScopeAttribute(String)}を使って下さい。
     * </p>
     * 
     * @param name 属性の名前。
     * @return 属性の値。存在しない場合はnullを返します。
     * @since 1.0.5
     */
    Object getRawAttribute(String name);

    /**
     * conversationを開始します。
     * <p>現在のconversationの名前が指定された名前と一致しない場合は、
     * conversationを新規に開始します。
     * </p>
     * <p>現在のconversationの名前が指定された名前と一致する場合は、
     * alwaysBeginがfalseであればconversationを開始しません。
     * alwaysBeginがtrueであれば、
     * 現在のconversationの名前が指定された名前と一致する場合でも再度conversationを開始します。
     * （ただし直前のフェーズ名と新しいフェーズ名が一致した場合はconversationを開始しません。）
     * </p>
     * 
     * @param conversationName 開始するconversationの名前。
     * @param phase 開始するフェーズの名前。
     * @param condition 開始条件。
     */
    void begin(String conversationName, String phase, BeginCondition condition);

    /**
     * 現在のconversationに参加します。
     * <p>参加するには、conversation名が現在のconversationの名前と一致し、
     * 現在のフェーズ名がphaseとまたはfollowAfterで指定されたいずれかの名前と一致する必要があります。
     * そうでない場合は{@link IllegalTransitionRuntimeException}がスローされます。
     * </p>
     * <p>正常に現在のconversationに参加すると、フェーズ名が指定したものに更新されます。
     * </p>
     * 
     * @param conversationName 新しいconversation名。
     * @param phase 新しいフェーズ名。
     * @param followAfter 現在のフェーズ名の制約。
     * @param acceptBrowsersBackButton ブラウザの「戻る」ボタンを押された場合の遷移を許可するかどうか。
     */
    void join(String conversationName, String phase, String[] followAfter,
            boolean acceptBrowsersBackButton);

    /**
     * conversationを終了します。
     * <p>現在のconversationがsub-conversationである場合は
     * super-conversationに遷移した上で
     * sub-conversationの開始時に指定された戻り値オブジェクトを返します。
     * そうでない場合は現在のconversationを消去してnullを返します。
     * </p>
     * 
     * @return sub-conversationの開始時に指定された戻り値オブジェクト。
     */
    Object end();

    /**
     * 現在のconversationがsub-conversationかどうかを返します。
     * 
     * @return 現在のconversationがsub-conversationかどうか。
     */
    boolean isInSubConversation();

    /**
     * sub-conversationを開始します。
     * <p>sub-conversationはconversationの入れ子で、
     * 処理の中から一時的に別処理を実行するために用意されています。
     * </p>
     * <p>sub-convresationを開始する場合は終了時の戻り先を設定しておきます。
     * sub-convresationが終了すると、予め設定しておいて戻り先に処理が戻ります。
     * </p>
     * <p><b>[重要]</b> クラスタリング対応アプリケーションでは、
     * <code>reenterResponse</code>にはSerializableでないオブジェクトを設定しないようにして下さい。
     * </p>
     * 
     * @param reenterResponse 終了時の処理の戻り先を表すオブジェクト。
     */
    void beginSubConversation(Object reenterResponse);

    /**
     * 現在のconversationにバインドされている全ての属性の名前のIteratorを返します。
     * 
     * @return Iteratorオブジェクト。
     */
    Iterator<String> getAttributeNames();
}
