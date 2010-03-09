package org.seasar.ymir.message;

import java.util.Locale;

public interface MessageProvider {
    /**
     * 指定されたロケールに対応するメッセージを返します。
     * 
     * @param name
     * @param locale ロケール。
     * @return
     */
    String getMessage(String name, Locale locale);
}
