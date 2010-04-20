package org.seasar.ymir.zpt;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.ymir.Globals;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.annotation.handler.impl.AnnotationHandlerImpl;
import org.seasar.ymir.cache.impl.CacheManagerImpl;
import org.seasar.ymir.hotdeploy.fitter.impl.ListFitter;
import org.seasar.ymir.hotdeploy.impl.HotdeployManagerImpl;
import org.seasar.ymir.impl.ApplicationManagerImpl;
import org.seasar.ymir.impl.ContextURLResolverImpl;
import org.seasar.ymir.locale.impl.LocaleManagerImpl;
import org.seasar.ymir.message.impl.MessagesImpl;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.mock.MockYmir;
import org.seasar.ymir.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.mock.servlet.MockServletContext;
import org.seasar.ymir.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.session.impl.SessionManagerImpl;
import org.seasar.ymir.token.impl.TokenManagerImpl;

import net.skirnir.freyja.zpt.ZptTemplateContext;

abstract public class ZptTestCase extends TestCase {
    protected MockServletContext servletContext_ = new MockServletContextImpl(
            "/context");

    protected MockHttpServletRequest httpRequest_ = new MockHttpServletRequestImpl(
            servletContext_, HttpMethod.GET, "/index.html");

    protected S2Container container_;

    protected YmirVariableResolver varResolver_;

    protected ZptTemplateContext context_;

    protected MessagesImpl messages_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        container_ = new S2ContainerImpl();
        container_.register(new TokenManagerImpl());
        container_.register(ApplicationManagerImpl.class);
        container_.register(ListFitter.class);
        container_.register(HotdeployManagerImpl.class);
        container_.register(CacheManagerImpl.class);
        container_.register(AnnotationHandlerImpl.class);
        container_.register(SessionManagerImpl.class);
        LocaleManagerImpl localeManager = new LocaleManagerImpl() {
            @Override
            public Locale getLocale() {
                return ZptTestCase.this.getLocale();
            }

            @Override
            public TimeZone getTimeZone() {
                return ZptTestCase.this.getTimeZone();
            }
        };
        container_.register(localeManager);
        messages_ = new MessagesImpl();
        messages_.setLocaleManager(localeManager);
        container_.register(messages_, Globals.NAME_MESSAGES);
        container_.register(new ContextURLResolverImpl());

        MockYmir ymir = new MockYmir();
        MockApplication application = new MockApplication();
        application.setS2Container(container_);
        ymir.setApplication(application);
        YmirContext.setYmir(ymir);

        varResolver_ = new YmirVariableResolver(new MockRequest(),
                httpRequest_, container_);
        varResolver_.setVariable("true", Boolean.TRUE);
        varResolver_.setVariable("false", Boolean.FALSE);
        context_ = new ZptTemplateContext();
    }

    protected Locale getLocale() {
        return Locale.JAPANESE;
    }

    protected TimeZone getTimeZone() {
        return TimeZone.getTimeZone("GMT");
    }
}
