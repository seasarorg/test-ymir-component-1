package org.seasar.ymir;

/**
 * フレームワークに関するエントリポイントを提供するクラスです。
 * <p><b>同期化：</b>
 * このクラスはスレッドセーフです。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public class YmirContext {
    private static Ymir ymir_;

    /**
     * シングルトンであるYmirオブジェクトをコンテキストに設定します。
     * 
     * @param ymir Ymirオブジェクト。
     */
    public static void setYmir(Ymir ymir) {
        ymir_ = ymir;
    }

    /**
     * シングルトンであるYmirオブジェクトを返します。
     * 
     * @return シングルトンであるYmirオブジェクト。
     */
    public static Ymir getYmir() {
        return ymir_;
    }

    /**
     * 現在処理中のアプリケーションが開発中であるとみなすかどうかを返します。
     * <p>このメソッドは{@link Ymir#isUnderDevelopment()}と同じです。
     * 
     * @return 現在処理中のアプリケーションが開発中であるとみなすかどうか。
     */
    public static boolean isUnderDevelopment() {
        return ymir_.isUnderDevelopment();
    }
}
