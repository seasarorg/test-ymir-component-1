package org.seasar.ymir;

import java.net.URL;
import java.util.Map;

/**
 * 画面テンプレートからResponseオブジェクトを生成するためのインタフェースです。
 * <p><b>同期化：</b>
 * このインタフェースの実装クラスはスレッドセーフである必要があります。
 * </p>
 * 
 * @author YOKOTA Takehiko
 */
public interface ResponseCreator {
    /**
     * Responseオブジェクトを生成します。
     * 
     * @param templateName テンプレート名。
     * @param variableMap テンプレートに埋め込む変数を保持するMap。
     * @return 生成したResponseオブジェクト。
     */
    Response createResponse(String templateName, Map variableMap);

    /**
     * Responseオブジェクトを生成します。
     * 
     * @param templateURL テンプレートのURL。
     * @param variableMap テンプレートに埋め込む変数を保持するMap。
     * @return 生成したResponseオブジェクト。
     */
    Response createResponse(URL templateURL, Map variableMap);
}
