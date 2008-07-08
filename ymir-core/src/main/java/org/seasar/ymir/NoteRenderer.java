package org.seasar.ymir;

/**
 * {@link Note}オブジェクトから文を構築するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @see Note
 * @author YOKOTA Takehiko
 */
public interface NoteRenderer {
    /**
     * 指定されたNoteオブジェクトからメッセージ文字列を構築します。
     * <p>{@link Note#getValue()}の値をテンプレートキーとみなして
     * {@link Messages#getMessage(String)}を使って現在のロケールに合う文のテンプレートを
     * Messagesオブジェクトから取り出し、
     * テンプレート中にある<code>{数字}</code>というプレースホルダを
     * 対応するパラメータの値で置き換えて文を構築します。
     * プレースホルダの数字は0オリジンです。
     * </p>
     * 
     * @param note Noteオブジェクト。
     * @param messages Messagesオブジェクト。
     * @return 構築したメッセージ文字列。
     * テンプレートキーに対応するテンプレートがmessagesに存在しない場合はnullを返します。
     */
    String render(Note note, Messages messages);

    /**
     * 指定されたテンプレートキーに対応するテンプレートとパラメータを使ってメッセージ文字列を構築します。
     * <p>テンプレートキーに対応するテンプレートをMessagesオブジェクトから取り出し、
     * テンプレート中にある<code>{数字}</code>というプレースホルダを
     * 対応するパラメータの値で置き換えて文を構築します。
     * プレースホルダの数字は0オリジンです。
     * </p>
     * 
     * @param templateKey テンプレートのキー。
     * @param parameters パラメータの配列。
     * @param messages Messagesオブジェクト。
     * @return 構築したメッセージ文字列。
     * テンプレートキーに対応するテンプレートがmessagesに存在しない場合はnullを返します。
     */
    String render(String templateKey, Object[] parameters, Messages messages);
}
