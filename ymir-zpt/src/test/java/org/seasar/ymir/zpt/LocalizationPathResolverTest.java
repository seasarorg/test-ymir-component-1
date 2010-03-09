package org.seasar.ymir.zpt;

import org.seasar.ymir.message.impl.MessagesImpl;
import org.seasar.ymir.mock.MockDispatch;
import org.seasar.ymir.mock.MockRequest;

import net.skirnir.freyja.impl.TemplateContextImpl;
import net.skirnir.freyja.impl.VariableResolverImpl;

public class LocalizationPathResolverTest extends ZptTestCase {
    private LocalizationPathResolver target_ = new LocalizationPathResolver();

    public void test1_Messagesの場合はページ名つきキーも検索されること() throws Exception {
        MockRequest request = new MockRequest();
        request.enterDispatch(new MockDispatch()
                .setPageComponentName("indexPage"));
        container_.register(request);
        varResolver_
                .setVariable(YmirVariableResolver.NAME_YMIRREQUEST, request);
        messages_.addPath(getClass().getName().replace('.', '/').concat(
                "_test1.xproperties"));
        messages_.init();

        assertEquals("indexPage_ja", target_.resolve(context_, varResolver_,
                messages_, "%a.b"));
    }

    public void testAccept() throws Exception {
        assertTrue(target_.accept(new TemplateContextImpl(),
                new VariableResolverImpl(), new MessagesImpl(), "%a.b"));
    }
}
