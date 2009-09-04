package org.seasar.ymir.batch;

public interface Batch {
    boolean init(String[] args) throws Exception;

    int execute() throws Exception;

    void destroy() throws Exception;
}
