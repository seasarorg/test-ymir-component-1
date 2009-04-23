package org.seasar.ymir.extension.creator;

import java.util.Set;

public interface Desc<T extends Desc<?>> {
    DescPool getDescPool();

    <D extends Desc<?>> D getParent();

    void setParent(Desc<?> parent);

    /**
     * 指定されたオブジェクトにこのオブジェクトの内容を転記します。
     * 
     * @param desc 転記先のオブジェクト。
     * nullを指定してはいけません。
     * @return 引数で渡されたオブジェクト。
     */
    T transcriptTo(T desc);

    /**
     * このDescが依存しているクラスのうち、パッケージ名を省略可能でないクラスのFQCNを返します。
     * 
     * @return クラスのFQCNの配列。
     * nullを返すことはありません。
     */
    String[] getImportClassNames();

    /**
     * このDescが依存しているクラスのFQCNを指定されたSetに追加します。
     * 
     * @param set クラス名を追加するためのSet。
     * nullを指定してはいけません。
     */
    void addDependingClassNamesTo(Set<String> set);
}
