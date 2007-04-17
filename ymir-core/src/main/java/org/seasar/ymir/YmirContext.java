package org.seasar.ymir;

public class YmirContext {
    private static Ymir ymir_;

    public static void setYmir(Ymir ymir) {
        ymir_ = ymir;
    }

    public static Ymir getYmir() {
        return ymir_;
    }

    public static boolean isUnderDevelopment() {
        return ymir_.getApplication().isUnderDevelopment();
    }
}
