package org.seasar.ymir.history;

import java.util.List;
import java.util.regex.Pattern;

/**
 * アクセス履歴を記録するためのインタフェースです。
 * <p>例えば「直前に画面Aにアクセスがあった場合は処理を変える」というようなことをする場合に有用です。
 * </p>
 * <p>アクセス履歴はウィンドウID単位に記録されます。</p>
 * <p>画面遷移を制御したい場合はアクセス履歴を使うのではなく
 * Conversationの仕組みを使うことを検討して下さい。
 * </p>
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public interface HistoryManager {
    /**
     * 直近何回分のアクセス履歴を記録するかを指定します。
     * <p>負の値を指定した場合は全てのアクセス履歴を記録するようになりますが、
     * メモリを圧迫するため注意して下さい。
     * </p>
     * <p>このメソッドは{@code ymir-component+historyManager.dicon}を作成してその中で
     * 呼び出すようにすると良いでしょう。
     * </p>
     * 
     * @param recordCount 直近何回分のアクセス履歴を記録するか。
     */
    void setRecordCount(int recordCount);

    /**
     * アクセス履歴を記録しないパスの正規表現パターン文字列を追加します。
     * <p>マッチングはコンテキスト相対パスと行なわれます。
     * </p>
     * <p>このメソッドは{@code ymir-component+historyManager.dicon}を作成してその中で
     * 呼び出すようにすると良いでしょう。
     * </p>
     * 
     * @param ignorePathPatternString パスの正規表現パターン文字列。
     * nullを指定してはいけません。
     * @see #getIgnorePathPatterns()
     * @see #setIgnorePathPatternStrings(List)
     */
    void addIgnorePathPatternString(String ignorePathPatternString);

    /**
     * アクセス履歴を記録しないパスの正規表現パターン文字列を設定します。
     * <p>マッチングはコンテキスト相対パスと行なわれます。
     * </p>
     * <p>このメソッドは{@code ymir-component+historyManager.dicon}を作成してその中で
     * 呼び出すようにすると良いでしょう。
     * </p>
     * 
     * @param ignorePathPatternStrings パスの正規表現パターン文字列のList。
     * nullを指定してはいけません。またListにnullを含めてはいけません。
     * @see #getIgnorePathPatterns()
     * @see #addIgnorePathPatternString(String)
     */
    void setIgnorePathPatternStrings(List<String> ignorePathPatternStrings);

    /**
     * アクセス履歴を記録しないパスの正規表現パターンオブジェクトのListを返します。
     * 
     * @return パスの正規表現パターンオブジェクトのList。
     * nullが返されることはありません。
     */
    List<Pattern> getIgnorePathPatterns();

    /**
     * アクセス履歴の記録を開始します。
     * <p>自動記録モード（{@code app.properties}で{@code core.history.autoRecording}がtrue）の場合は
     * このメソッドを呼び出さなくても自動的に記録されます。
     * そうでない場合は、アクセス履歴の記録を開始するためにこのメソッドを明示的に呼び出す必要があります。
     * </p>
     */
    void startRecording();

    /**
     * アクセス履歴を記録中かどうかを返します。
     * 
     * @return アクセス履歴を記録中かどうか。
     */
    boolean isRecording();

    /**
     * アクセス履歴を保持するオブジェクトを返します。
     * <p>アクセス履歴が記録されていない場合は空のオブジェクトを返します。
     * </p>
     * 
     * @return アクセス履歴を保持するオブジェクト。nullが返されることはありません。
     */
    History getHistory();

    /**
     * アクセス履歴の記録を中断します。
     * <p>今まで記録したアクセス履歴は消去されます。
     * </p>
     * <p>自動記録モードの場合にこのメソッドを呼び出すと、
     * 今まで記録したアクセス履歴が消去されるだけで履歴の記録は継続して行なわれます。
     * </p>
     */
    void stopRecording();
}
