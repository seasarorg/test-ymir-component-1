package org.seasar.ymir.extension.creator;

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
     * このDescが依存している全てのクラスのFQCNを返します。
     * 
     * @return このDescが依存している全てのクラスのFQCNの配列。
     * nullを返すことはありません。
     */
    String[] getDependingClassNames();
}
