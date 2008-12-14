package com.example;

import org.t2framework.it.SeleniumTestCase;
import org.t2framework.it.annotation.Port;
import org.t2framework.it.annotation.Url;

/**
 * TODO yonexを使ったITのサンプルです。不要であれば削除して下さい。
 * TODO src/main/webapp/yonex-sample.htmlも削除して下さい。
 */
@Url("http://localhost")
@Port(8080)
// @Browser(value=BrowserType.IEXPLORE, path="C:\\Program Files\\Internet
// Explorer\\iexplore.exe")
public class SampleIT extends SeleniumTestCase {
    public void testIndex() throws Exception {
        // テスト対象のドメイン相対のURLを指定して下さい。
        // コンテキスト相対URLが「/index.html」である場合は「webapp/index.html」
        // （webappという文字列は固定です）と指定して下さい。
        selenium.open("webapp/yonex-sample.html");

        assertEquals("TITLE", selenium.getTitle());

        // テストのエビデンスとして画面のスクリーンショットを取得します。
        captureScreen("SampleIT_testIndex.png");
    }
}
