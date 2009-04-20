package org.seasar.ymir.extension.creator;

public interface Desc<T extends Desc<?>> {
    DescPool getDescPool();

    /**
     * 指定されたオブジェクトにこのオブジェクトの内容を転記します。
     * 
     * @param desc 転記先のオブジェクト。
     * nullを指定してはいけません。
     * @return 引数で渡されたオブジェクト。
     */
    T transcriptTo(T desc);
}
