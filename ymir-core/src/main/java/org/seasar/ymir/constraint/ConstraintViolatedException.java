package org.seasar.ymir.constraint;

import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.message.NotesHolder;

/**
 * 制約を満たしていない場合にスローされる例外クラスです。
 * <p><b>同期化：</b>
 * この抽象クラスのサブクラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
abstract public class ConstraintViolatedException extends Exception implements
        NotesHolder {
    private static final long serialVersionUID = -7148377969598636958L;

    private String path_;

    private Notes notes_;

    /**
     * このクラスのオブジェクトを構築します。
     */
    public ConstraintViolatedException() {
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param note エラーメッセージ文字列。
     */
    public ConstraintViolatedException(String note) {
        super(note);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param cause 元になった例外。
     */
    public ConstraintViolatedException(Throwable cause) {
        super(cause);

    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param message エラーメッセージ文字列。
     * @param cause 元になった例外。
     */
    public ConstraintViolatedException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     * このクラスのオブジェクトを構築します。
     *
     * @param notes エラーメッセージを表すNotesオブジェクト。
     */
    public ConstraintViolatedException(Notes notes) {
        notes_ = notes;
    }

    /**
     * リクエストパスを返します。
     * 
     * @return リクエストパス。
     */
    public String getPath() {
        return path_;
    }

    /**
     * リクエストパスを設定します。
     * 
     * @param path リクエストパス。
     * @return このオブジェクト自身。
     */
    public ConstraintViolatedException setPath(String path) {
        path_ = path;
        return this;
    }

    /**
     * エラーメッセージを表すNotesオブジェクトを持つかどうかを返します。
     * 
     * @return エラーメッセージを表すNotesオブジェクトを持つかどうか。
     */
    public boolean hasNotes() {
        return (notes_ != null);
    }

    /**
     * エラーメッセージを表すNotesオブジェクトを返します。
     * 
     * @return エラーメッセージを表すNotesオブジェクト。
     */
    public Notes getNotes() {
        return notes_;
    }

    /**
     * エラーメッセージを表すNotesオブジェクトを設定します。
     * 
     * @param notes エラーメッセージを表すNotesオブジェクト。
     * @return このオブジェクト自身。
     */
    public ConstraintViolatedException setNotes(Notes notes) {
        notes_ = notes;
        return this;
    }

    /**
     * エラーメッセージを表すNoteオブジェクトを追加します。
     * 
     * @param note エラーメッセージを表すNoteオブジェクト。
     * @return このオブジェクト自身。
     */
    public ConstraintViolatedException addNote(Note note) {
        if (notes_ == null) {
            notes_ = new Notes();
        }
        notes_.add(note);
        return this;
    }

    /**
     * エラーメッセージを追加します。
     * 
     * @param value エラーメッセージのテンプレートを表すメッセージリソースのキー。
     * @param parameters テンプレートに埋め込むパラメータ。
     * @return このオブジェクト自身。
     */
    public ConstraintViolatedException addNote(String value, Object[] parameters) {
        return addNote(new Note(value, parameters));
    }

    /**
     * エラーメッセージを追加します。
     *
     * @param category エラーメッセージのカテゴリ。
     * @param note エラーメッセージを表すNoteオブジェクト。
     * @return このオブジェクト自身。
     */
    public ConstraintViolatedException addNote(String category, Note note) {
        if (notes_ == null) {
            notes_ = new Notes();
        }
        notes_.add(category, note);
        return this;
    }

    /**
     * エラーメッセージを追加します。
     * 
     * @param category エラーメッセージのカテゴリ。
     * @param value エラーメッセージのテンプレートを表すメッセージリソースのキー。
     * @param parameters テンプレートに埋め込むパラメータ。
     * @return このオブジェクト自身。
     */
    public ConstraintViolatedException addNote(String category, String value,
            Object[] parameters) {
        return addNote(category, new Note(value, parameters));
    }
}
