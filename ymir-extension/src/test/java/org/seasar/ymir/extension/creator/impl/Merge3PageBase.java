package org.seasar.ymir.extension.creator.impl;

public class Merge3PageBase extends Merge3PageBaseBase {

    public void setHoe(String hoe) {
        hoe_ = hoe;
    }

    public void setFuga(String fuga) {
        fuga_ = fuga;
    }

    public int times(int f1, int f2) {
        return f1 * f2;
    }

    public int div(int f1, int f2) {
        return f1 / f2;
    }

    public int inc(int f1, int f2) {
        return f1 + 1;
    }
}
