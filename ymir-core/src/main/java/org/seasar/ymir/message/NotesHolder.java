package org.seasar.ymir.message;

/**
 * Notesを持っているものを表すインタフェースです。
 * 
 * @since 1.0.7
 */
public interface NotesHolder {
    /**
     * Notesオブジェクトを返します。
     * 
     * @return Notesオブジェクト。
     * nullが返されることもあります。
     */
    Notes getNotes();
}
