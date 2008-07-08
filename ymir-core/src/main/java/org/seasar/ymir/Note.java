package org.seasar.ymir;

/**
 * 文を表すクラスです。
 * <p>このクラスは、主に画面で表示する文を表すクラスです。
 * </p>
 * このクラスは単に文を構築するための情報を持つだけです。
 * このクラスのオブジェクトの内容をどう使って文を組み立てて表示するかについては関与しませんが、
 * 通常、文を表すメッセージリソースのキーと文に埋め込むパラメータを保持させ、
 * 表示の際にこのクラスが持つ情報と表示用のロケールから実際の文を構築して表示するようにします。
 * 文そのものを持たせることもできます。
 * </p>
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @see NoteRenderer
 * @see Notes 
 * @author YOKOTA Takehiko
 */
public class Note {
    private String value_;

    private Object[] parameters_;

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param value 文を表す文字列。文そのものか、または文に対応するメッセージリソースのキーを指定します。
     */
    public Note(String value) {
        this(value, null);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param value 文に対応するメッセージリソースのキー。
     * @param parameter0 文に埋め込むパラメータ。
     */
    public Note(String value, Object parameter0) {
        this(value, new Object[] { parameter0 }, false);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param value 文に対応するメッセージリソースのキー。
     * @param parameter0 文に埋め込む1つ目のパラメータ。
     * @param parameter1 文に埋め込む2つ目のパラメータ。
     */
    public Note(String value, Object parameter0, Object parameter1) {
        this(value, new Object[] { parameter0, parameter1 }, false);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param value 文に対応するメッセージリソースのキー。
     * @param parameter0 文に埋め込む1つ目のパラメータ。
     * @param parameter1 文に埋め込む2つ目のパラメータ。
     */
    public Note(String value, Object parameter0, Object parameter1,
            Object parameter2) {
        this(value, new Object[] { parameter0, parameter1, parameter2 }, false);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * 
     * @param value 文に対応するメッセージリソースのキー。
     * @param parameter0 文に埋め込む1つ目のパラメータ。
     * @param parameter1 文に埋め込む2つ目のパラメータ。
     * @param parameter2 文に埋め込む3つ目のパラメータ。
     */
    public Note(String value, Object parameter0, Object parameter1,
            Object parameter2, Object parameter3) {
        this(value, new Object[] { parameter0, parameter1, parameter2,
            parameter3 }, false);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * <p>パラメータ列は内部で複製されます。</p>
     * 
     * @param value 文に対応するメッセージリソースのキー。
     * @param parameters 文に埋め込むパラメータ。nullを指定しても構いません。
     */
    public Note(String value, Object[] parameters) {
        this(value, parameters, true);
    }

    /**
     * このクラスのオブジェクトを構築します。
     * <p>このコンストラクタはインスタンスを不変にするために使用されます。</p>
     *
     * @param value 文に対応するメッセージリソースのキー。
     * @param parameters パラメータ列。nullを指定しても構いません。
     * @param clone パラメータ列を複製するかどうか。
     */
    private Note(String value, Object[] parameters, boolean clone) {
        value_ = value;
        if (parameters == null) {
            parameters_ = new Object[0];
        } else {
            if (clone) {
                Object[] objs = new Object[parameters.length];
                System.arraycopy(parameters, 0, objs, 0, parameters.length);
                parameters_ = objs;
            } else {
                parameters_ = parameters;
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("value=").append(value_).append(", parameters={");
        for (int i = 0; i < parameters_.length; i++) {
            sb.append(parameters_[i]);
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 文を表す文字列を返します。
     * 
     * @return 文を表す文字列。
     */
    public String getValue() {
        return value_;
    }

    /**
     * パラメータの配列を返します。
     * 
     * @return パラメータの配列。nullを返すことはありません。
     */
    public Object[] getParameters() {
        return parameters_;
    }
}
