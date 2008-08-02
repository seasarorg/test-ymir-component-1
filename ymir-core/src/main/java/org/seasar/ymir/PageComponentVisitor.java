package org.seasar.ymir;

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
    @SuppressWarnings("unchecked")
    public final R visit(PageComponent pageComponent) {
        PageComponent[] descendants = pageComponent.getDescendants();
        for (int i = 0; i < descendants.length; i++) {
            R processed = process(descendants[i]);
            if (processed != null) {
                return processed;
            }
        }

        return null;
    }

    /**
     * 指定されたPageComponentを処理します。
     * 
     * @param pageComponent 処理対象のPageComponent。
     * @return 処理結果。
     * 処理結果がnullでない場合、この処理結果を最終的な処理結果とみなしてトラバースを終了します。
     */
    abstract public R process(PageComponent pageComponent);
}
