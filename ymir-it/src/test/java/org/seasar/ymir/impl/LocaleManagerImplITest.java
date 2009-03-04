package org.seasar.ymir.impl;

import java.util.Locale;

import org.seasar.ymir.testing.PageTestCase;

import com.example.web.LocaleManagerImplPage;

public class LocaleManagerImplITest extends PageTestCase<LocaleManagerImplPage> {
    @Override
    public void setUpEnvironment() {
        setLocale(new Locale("ja", "JP"));
    }

    public void test() throws Exception {
        process(LocaleManagerImplPage.class);

        assertSame(getLocale(), getPage().getLocale());
    }
}
