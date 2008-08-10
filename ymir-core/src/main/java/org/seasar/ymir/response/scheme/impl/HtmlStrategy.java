package org.seasar.ymir.response.scheme.impl;

import org.seasar.ymir.Response;
import org.seasar.ymir.response.SelfContainedResponse;
import org.seasar.ymir.response.scheme.Strategy;

/**
 * このクラスは後方互換性のために存在します。通常は代わりにContentStrategyを利用すべきです。
 * 具体的には、「<code>html:XXXX</code>」と書く代わりに「<code>content:XXXX</code>」
 * もしくは「<code>content:text/html; charset=CCC:XXXX</code>」と書くべきです。
 * 
 * @author YOKOTA Takehiko
 */
public class HtmlStrategy implements Strategy {

    public static final String SCHEME = "html";

    public String getScheme() {

        return SCHEME;
    }

    public Response constructResponse(String path, Object component) {

        return new SelfContainedResponse(path);
    }
}
