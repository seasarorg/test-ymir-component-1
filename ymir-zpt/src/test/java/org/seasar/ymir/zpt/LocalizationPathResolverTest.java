package org.seasar.ymir.zpt;

import java.util.Locale;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.ymir.Request;
import org.seasar.ymir.Ymir;
import org.seasar.ymir.impl.LocaleManagerImpl;
import org.seasar.ymir.message.impl.MessagesImpl;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.mock.MockYmir;

import net.skirnir.freyja.VariableResolver;
import net.skirnir.freyja.impl.TemplateContextImpl;
import net.skirnir.freyja.impl.VariableResolverImpl;
import net.skirnir.freyja.webapp.ServletVariableResolver;

public class LocalizationPathResolverTest extends TestCase {
    private LocalizationPathResolver target_ = new LocalizationPathResolver();

    public void test1_Messagesの場合はページ名つきキーも検索されること() throws Exception {
        VariableResolver varResolver = new VariableResolverImpl();
        final Locale locale = Locale.JAPANESE;
        varResolver.setVariable(ServletVariableResolver.VAR_LOCALE, locale);
        final MockRequest request = new MockRequest();
        request.enterDispatch(new MockDispatch()
                .setPageComponentName("indexPage"));
        varResolver.setVariable(YmirVariableResolver.NAME_YMIRREQUEST, request);
        final S2Container container = new S2ContainerImpl() {
            @Override
            public Object getComponent(Object componentKey) {
                if (Request.class.equals(componentKey)) {
                    return request;
                } else {
                    return super.getComponent(componentKey);
                }
            }
        };
        MessagesImpl messages = new MessagesImpl() {
            private Ymir ymir_ = new MockYmir()
                    .setApplication(new MockApplication()
                            .setS2Container(container));

            @Override
            protected Ymir getYmir() {
                return ymir_;
            }
        };
        messages.setLocaleManager(new LocaleManagerImpl() {
            @Override
            public Locale getLocale() {
                return locale;
            }
        });
        messages.addPath(getClass().getName().replace('.', '/').concat(
                "_test1.xproperties"));
        messages.init();

        assertEquals("indexPage_ja", target_.resolve(new TemplateContextImpl(),
                varResolver, messages, "%a.b"));
    }

    public void testAccept() throws Exception {
        assertTrue(target_.accept(new TemplateContextImpl(),
                new VariableResolverImpl(), new MessagesImpl(), "%a.b"));
    }
}
