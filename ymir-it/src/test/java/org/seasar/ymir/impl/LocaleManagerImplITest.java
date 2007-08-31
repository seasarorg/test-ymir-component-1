package org.seasar.ymir.impl;

import java.util.Locale;

import org.seasar.ymir.Request;
import org.seasar.ymir.test.PageTestCase;

import com.example.web.LocaleManagerImplPage;

public class LocaleManagerImplITest extends PageTestCase<LocaleManagerImplPage> {
    @Override
    protected Class<LocaleManagerImplPage> getPageClass() {
        return LocaleManagerImplPage.class;
    }

    public void test() throws Exception {
        Locale locale = new Locale("ja", "JP");
        setLocale(locale);
        Request request = prepareForPrecessing("/localeManagerImpl.html",
                Request.METHOD_GET);
        processRequest(request);

        assertSame(locale, getPage().getLocale());
    }
}