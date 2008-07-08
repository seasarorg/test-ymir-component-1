package org.seasar.ymir;

import java.util.Locale;

/**
 * ロケールを管理するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface LocaleManager {
    /**
     * 現在のセッションに関連付けられているロケールを返します。
     * <p>セッションにロケールが関連付けられていない場合はリクエストからロケールを判定して返します。
     * リクエストからロケールが判定できない場合はシステムのロケールを返します。
     * </p>
     * 
     * @return ロケール。nullが返ることはありません。
     */
    Locale getLocale();

    /**
     * 現在のセッションにロケールを関連付けます。
     * <p>このメソッドを呼び出すと、現在処理中のリクエストに関する{@link Request}オブジェクト
     * が持つロケールも差し替えられます。
     * </p>
     * 
     * @param locale 関連付けるロケール。
     */
    void setLocale(Locale locale);

    /**
     * 現在のセッションからロケールを除去します。
     * <p>現在処理中のリクエストに関する{@link Request}オブジェクトが持つロケールは変更されません。
     * </p>
     */
    void removeLocale();
}
