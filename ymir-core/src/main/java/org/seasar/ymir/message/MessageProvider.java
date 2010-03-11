package org.seasar.ymir.message;

import java.util.Locale;

/**
 * @since 1.0.7
 */
public interface MessageProvider {
    /**
     * 指定された名前の、指定されたロケールに対応するメッセージを返します。
     * 
     * @param name メッセージの名前。
     * @param locale ロケール。
     * @return メッセージ。見つからなかった場合はnullを返します。
     */
    String getMessageValue(String name, Locale locale);

    /**
     * 指定された名前に対応するメッセージを返します。
     * 
     * @param name メッセージの名前。
     * @return メッセージ。見つからなかった場合はnullを返します。
     */
    String getMessageValue(String name);
}
