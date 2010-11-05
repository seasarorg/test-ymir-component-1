package org.seasar.ymir.scope.impl;

import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.testing.PageTestCase;
import org.seasar.ymir.window.WindowManager;

import com.example.web.WindowScopeTestPage;

public class WindowScopeITest extends PageTestCase<WindowScopeTestPage> {
    public void test_WindowScopeに値をアウトジェクトできること() throws Exception {
        WindowManager windowManager = getComponent(WindowManager.class);

        process(WindowScopeTestPage.class, windowManager.getWindowIdKey(),
                "KEY");

        assertEquals("OUTJECTED_VALUE", windowManager.getScopeAttribute("KEY",
                "outjectedValue"));
    }

    public void test_リクエストパラメータでWindowIdを指定できること_GET() throws Exception {
        WindowManager windowManager = getComponent(WindowManager.class);

        process(WindowScopeTestPage.class, HttpMethod.GET, windowManager
                .getWindowIdKey(), "KEY", "button1");

        WindowScopeTestPage page = getPage();
        assertEquals("KEY", page.getWindowId());
    }

    public void test_リクエストパラメータでWindowIdを指定できること_POST() throws Exception {
        WindowManager windowManager = getComponent(WindowManager.class);

        process(WindowScopeTestPage.class, HttpMethod.POST, windowManager
                .getWindowIdKey(), "KEY", "button2");

        WindowScopeTestPage page = getPage();
        assertEquals("KEY", page.getWindowId());
    }
}
