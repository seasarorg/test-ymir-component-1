package org.seasar.ymir;

public interface NoteRenderer {
    /**
     * 指定されたNoteオブジェクトからメッセージ文字列を構築します。
     * 
     * @param note Noteオブジェクト。
     * @param messages Messagesオブジェクト。
     * @return 構築したメッセージ文字列。
     * Note#getValue()で取得されるキーに対応するテンプレートがmessagesに存在しない場合はnullを返します。
     */
    String render(Note note, Messages messages);

    /**
     * 指定されたテンプレートキーに対応するテンプレートとパラメータを使ってメッセージ文字列を構築します。
     * 
     * @param templateKey テンプレートのキー。
     * @param parameters パラメータの配列。
     * @param messages Messagesオブジェクト。
     * @return 構築したメッセージ文字列。
     * テンプレートキーに対応するテンプレートがmessagesに存在しない場合はnullを返します。
     */
    String render(String templateKey, Object[] parameters, Messages messages);
}
