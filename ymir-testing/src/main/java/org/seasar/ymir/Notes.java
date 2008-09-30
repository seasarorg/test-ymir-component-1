package org.seasar.ymir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.ymir.util.StringUtils;

/**
 * 複数の文を束ねるためのクラスです。
 * <p>このクラスは{@link Note}オブジェクトを束ねるためのクラスです。
 * Noteオブジェクトをカテゴリ分けして保持したり、カテゴリ毎に取り出したりすることもできます。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 *
 * @see Note
 * @author YOKOTA Takehiko
 */
public class Notes {
    /**
     * 相関バリデータ関連のエラーのようにNoteを複数のカテゴリに関連づけたい場合にカテゴリ名を連結するための区切り文字です。
     */
    public static final String CATEGORY_DELIMITER = "+";

    private static final String CATEGORY_DELIMITER_PATTERN = "\\"
            + CATEGORY_DELIMITER;

    /**
     * 標準のカテゴリです。
     */
    public static final String GLOBAL_NOTE = "org.seasar.ymir.GLOBAL_NOTE";

    private List<Note> list_ = new ArrayList<Note>();

    private Map<String, List<Note>> map_ = new LinkedHashMap<String, List<Note>>();

    private boolean accessed_ = false;

    /**
     * このクラスのオブジェクトを構築します。
     */
    public Notes() {
    }

    /**
     * このクラスのオブジェクトを構築します。
     * <p>指定されたNotesオブジェクトと同じ内容を持つNotesオブジェクトを構築します。
     * </p>
     * 
     * @param notes 元となるNotesオブジェクト。nullを指定することもできます。 
     */
    public Notes(Notes notes) {
        if (notes != null) {
            boolean accessed = notes.isAccessed();
            try {
                add(notes);
            } finally {
                notes.setAccessed(accessed);
            }
        }
    }

    /**
     * 指定されたNotesオブジェクトの内容をこのオブジェクトに追加します。
     * 
     * @param notes Notesオブジェクト。nullを指定することもできます。 
     * @return このオブジェクト自身。
     */
    public Notes add(Notes notes) {
        if (notes == null) {
            return this;
        }

        Iterator<String> categories = notes.categories();
        while (categories.hasNext()) {
            String category = categories.next();

            Iterator<Note> itr = notes.get(category);
            while (itr.hasNext()) {
                Note note = itr.next();
                add(category, note);
            }
        }
        return this;
    }

    /**
     * 指定されたNoteオブジェクトをこのオブジェクトに追加します。
     * <p>{@link #GLOBAL_NOTE}カテゴリに追加します。
     * </p>
     * 
     * @param note Noteオブジェクト。 
     * @return このオブジェクト自身。
     */
    public Notes add(Note note) {
        return add(GLOBAL_NOTE, note);
    }

    /**
     * 指定されたNoteオブジェクトをこのオブジェクトに追加します。
     * <p>指定されたカテゴリに追加します。</p>
     * 
     * @param category カテゴリ。
     * 「+」で区切ることで{@link #add(Note, String...)}と同じく複数カテゴリを指定することができます。
     * @param note Noteオブジェクト。 
     * @return このオブジェクト自身。
     */
    public Notes add(String category, Note note) {
        return add(note, category.split(CATEGORY_DELIMITER_PATTERN, -1));
    }

    /**
     * 指定されたNoteオブジェクトをこのオブジェクトに追加します。
     * <p>指定されたカテゴリに追加します。</p>
     * <p>カテゴリを複数指定した場合は、指定されたNoteオブジェクトを複数のカテゴリに対して追加しますが、
     * 実体は1つです。
     * 例えばNotesが空の状態で<code>add(note, "a", "b")</code>とした場合、
     * <code>size()</code>も<code>size("a")</code>も<code>size("b")</code>
     * も全て1になります。
     * </p>
     * 
     * @param note Noteオブジェクト。
     * @param categories カテゴリ。 
     * @return このオブジェクト自身。
     * @since 0.9.6
     */
    public Notes add(Note note, String... categories) {
        list_.add(note);
        for (String category : StringUtils.unique(categories)) {
            List<Note> noteList = map_.get(category);
            if (noteList == null) {
                noteList = new ArrayList<Note>();
                map_.put(category, noteList);
            }
            noteList.add(note);
        }
        return this;
    }

    /**
     * このオブジェクトを空にします。
     * <p>アクセスフラグもOFFになります。</p>
     * 
     * @return このオブジェクト自身。
     */
    public Notes clear() {
        list_.clear();
        map_.clear();
        accessed_ = false;
        return this;
    }

    /**
     * このオブジェクトが持つ全てのNoteオブジェクトを取り出すためのIteratorを返します。
     * <p>アクセスフラグがONになります。</p>
     * 
     * @return このオブジェクトが持つ全てのNoteオブジェクトを取り出すためのIterator。
     */
    public Iterator<Note> get() {
        accessed_ = true;
        return Collections.unmodifiableCollection(list_).iterator();
    }

    /**
     * このオブジェクトが持つ全てのNoteオブジェクトを返します。
     * <p>アクセスフラグがONになります。</p>
     * 
     * @return このオブジェクトが持つ全てのNoteオブジェクト。空の場合は空の配列を返します。
     */
    public Note[] getNotes() {
        accessed_ = true;
        return list_.toArray(new Note[0]);
    }

    /**
     * このオブジェクトからNoteオブジェクトの取り出し操作が行なわれたかどうかを返します。
     * <p>このクラスでは、同じ文を二度以上描画しないといった制御が行なえるよう、
     * 一度Noteオブジェクトを取り出したかどうかが分かるようになっています。
     * このメソッドが返す値を使ってそのような制御をすることができます。
     * </p>
     * <p>このメソッドは、Getter系のメソッドが呼び出された際に内部的にONになるアクセスフラグの状態を返します。
     * </p>
     * 
     * @return このオブジェクトからNoteオブジェクトの取り出し操作が行なわれたかどうか。
     * @see #setAccessed(boolean)
     */
    public boolean isAccessed() {
        return accessed_;
    }

    /**
     * アクセスフラグを設定します。
     * 
     * @param accessed アクセスフラグの値。
     * @see #isAccessed() 
     */
    public void setAccessed(boolean accessed) {
        accessed_ = accessed;
    }

    /**
     * 指定されたカテゴリに属する全てのNoteオブジェクトを取り出すためのIteratorを返します。
     * <p>アクセスフラグがONになります。</p>
     * 
     * @param category カテゴリ名。
     * @return 指定されたカテゴリに属する全てのNoteオブジェクトを取り出すためのIterator。
     */
    public Iterator<Note> get(String category) {
        accessed_ = true;
        List<Note> noteList = map_.get(category);
        if (noteList == null) {
            noteList = new ArrayList<Note>();
        }
        return Collections.unmodifiableCollection(noteList).iterator();
    }

    /**
     * 指定されたカテゴリに属する全てのNoteオブジェクトを返します。
     * <p>アクセスフラグがONになります。</p>
     * 
     * @param category カテゴリ名。
     * @return 指定されたカテゴリに属する全てのNoteオブジェクト。空の場合は空の配列を返します。
     */
    public Note[] getNotes(String category) {
        accessed_ = true;
        List<Note> noteList = map_.get(category);
        if (noteList == null) {
            return new Note[0];
        } else {
            return noteList.toArray(new Note[0]);
        }
    }

    /**
     * このオブジェクトが空であるかどうかを返します。
     * 
     * @return このオブジェクトが空であるかどうか。
     */
    public boolean isEmpty() {
        return list_.isEmpty();
    }

    /**
     * このオブジェクトが持つ全てのカテゴリの名前を取り出すためのIteratorを返します。
     * <p>Noteが含まれないカテゴリの名前は返しません。
     * </p>
     * 
     * @return このオブジェクトが持つ全てのカテゴリの名前を取り出すためのIterator。
     */
    public Iterator<String> categories() {
        return Collections.unmodifiableSet(map_.keySet()).iterator();
    }

    /**
     * このオブジェクトが持つNoteオブジェクトの個数を返します。
     * 
     * @return このオブジェクトが持つNoteオブジェクトの個数。
     */
    public int size() {
        return list_.size();
    }

    /**
     * 指定されたカテゴリに属するNoteオブジェクトの個数を返します。
     * 
     * @param category カテゴリ名。
     * @return 指定されたカテゴリに属するNoteオブジェクトの個数。
     */
    public int size(String category) {
        List<Note> noteList = map_.get(category);
        if (noteList == null) {
            return 0;
        } else {
            return noteList.size();
        }
    }

    /**
     * 指定されたカテゴリが存在するかどうかを返します。
     * 
     * @param category カテゴリ名。
     * @return 指定されたカテゴリが存在するかどうか。
     */
    public boolean contains(String category) {
        return map_.containsKey(category);
    }

    /**
     * 指定された文字列を持つNoteオブジェクトが存在するかどうかを返します。
     * <p>指定された文字列を{@link Note#getValue()}の値として持つ
     * Noteオブジェクトが存在するかどうかを返します。
     * </p>
     * 
     * @param value 文字列。
     * @return 指定された文字列を持つNoteオブジェクトが存在するかどうか。
     */
    public boolean containsValue(String value) {
        int n = list_.size();
        for (int i = 0; i < n; i++) {
            Note note = list_.get(i);
            String v = note.getValue();
            if (v != null) {
                if (v.equals(value)) {
                    return true;
                }
            } else {
                if (value == null) {
                    return true;
                }
            }
        }

        return false;
    }
}
