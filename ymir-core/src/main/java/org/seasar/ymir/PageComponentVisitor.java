package org.seasar.ymir;

import java.io.ObjectInputStream.GetField;

/**
 * PageComponentをトラバースするためのVisitorを実装するベースとなる抽象クラスです。
 * <p><b>同期化：</b>
 * この抽象クラスのサブクラスはスレッドセーフである必要はありません。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
abstract public class PageComponentVisitor<R> implements
        Visitor<R, PageComponent> {
    public final R visit(PageComponent pageComponent, Object... parameters) {
        PageComponent[] descendants = pageComponent.getDescendants();
        for (int i = 0; i < descendants.length; i++) {
            R processed = process(descendants[i], parameters);
            if (isFinalResult(processed)) {
                return processed;
            }
        }

        return getFinalResult();
    }

    /**
     * 指定されたPageComponentを処理します。
     * 
     * @param pageComponent 処理対象のPageComponent。
     * @param parameters パラメータ。
     * @return 処理結果。
     * 処理結果が{@link #isFinalResult(R)}をtrueにするような値である場合、
     * この処理結果を最終的な処理結果とみなしてトラバースを終了します。
     */
    abstract public R process(PageComponent pageComponent, Object... parameters);

    /**
     * 指定された処理結果が最終的な処理結果かどうかを返します。
     * 
     * @param result 処理結果。nullが指定されることもあります。
     * @return 処理結果が最終的な処理結果かどうか。
     * @since 1.0.4
     */
    public boolean isFinalResult(R result) {
        return result != null;
    }

    /**
     * 最終的な処理結果を生成して返します。
     * <p>このメソッドは、どのPageComponentの処理結果についても
     * {@link #isFinalResult(R)}がfalseである場合に返される値を生成するためのものです。
     * </p>
     * 
     * @return 最終的な処理結果。
     * @since 1.0.4
     */
    public R getFinalResult() {
        return null;
    }
}
