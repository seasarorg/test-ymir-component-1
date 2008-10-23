package org.seasar.ymir.impl;

public class ArticlePage {

    private int counter_;

    public void _prerender() {
        counter_++;
    }

    public int getCounter() {
        return counter_;
    }
}
