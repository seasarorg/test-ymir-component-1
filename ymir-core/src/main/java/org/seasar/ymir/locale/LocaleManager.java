package org.seasar.ymir.locale;

import java.util.Locale;
import java.util.TimeZone;

import org.seasar.ymir.Globals;
import org.seasar.ymir.Request;

/**
 * ロケールとタイムゾーンを管理するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface LocaleManager {
    /**
     * 現在のコンテキストに関連付けられているロケールオブジェクトを保持する属性の名前です。
     */
    String ATTR_LOCALE = Globals.IDPREFIX + "locale.locale";

    /**
     * 現在のコンテキストに関連付けられているロケールオブジェクトを保持する属性の名前です。
     */
    String ATTR_TIMEZONE = Globals.IDPREFIX + "locale.timeZone";

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
     * 
     * @param locale 関連付けるロケール。
     */
    void setLocale(Locale locale);

    /**
     * 現在のセッションからロケールを除去します。
     */
    void removeLocale();

    /**
     * 現在のセッションに関連付けられているタイムゾーンを返します。
     * <p>セッションにタイムゾーンが関連付けられていない場合はシステムのタイムゾーンを返します。
     * </p>
     * 
     * @return タイムゾーン。nullが返ることはありません。
     * @since 1.0.3
     */
    TimeZone getTimeZone();

    /**
     * 現在のセッションにタイムゾーンを関連付けます。
     * 
     * @param timeZone 関連付けるタイムゾーン。
     * @since 1.0.3
     */
    void setTimeZone(TimeZone timeZone);

    /**
     * 現在のセッションからタイムゾーンを除去します。
     * 
     * @since 1.0.3
     */
    void removeTimeZone();
}
