package org.seasar.ymir.batch;

/**
 * {@link Batch}インタフェースの実装の基底クラスとすることのできる抽象クラスです。
 * 
 * @author yokota
 * @since 1.0.6
 */
abstract public class AbstractBatch implements Batch {
    public boolean init(String[] args) throws Exception {
        return true;
    }

    public void destroy() throws Exception {
    }
}
