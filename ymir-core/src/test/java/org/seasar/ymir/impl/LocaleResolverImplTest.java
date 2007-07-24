package org.seasar.ymir.impl;

import java.util.Locale;

import org.seasar.ymir.PageTestCase;
import org.seasar.ymir.Request;

import com.example.web.LocaleResolverImplPage;

public class LocaleResolverImplTest extends
        PageTestCase<LocaleResolverImplPage> {
    @Override
    protected Class<LocaleResolverImplPage> getPageClass() {
        return LocaleResolverImplPage.class;
    }

    public void test() throws Exception {
        Locale locale = new Locale("ja", "JP");
        setLocale(locale);
        Request request = prepareForPrecessing("/localeResolverImpl.html",
                Request.METHOD_GET);
        processRequest(request);
        LocaleResolverImplPage page = getPageComponent();

        assertSame(locale, page.getLocale());
    }
}
