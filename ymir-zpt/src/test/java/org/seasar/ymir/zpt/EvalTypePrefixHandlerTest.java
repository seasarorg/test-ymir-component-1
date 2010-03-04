package org.seasar.ymir.zpt;

import junit.framework.TestCase;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.ymir.Globals;
import org.seasar.ymir.HttpMethod;
import org.seasar.ymir.RequestProcessor;
import org.seasar.ymir.YmirContext;
import org.seasar.ymir.message.Note;
import org.seasar.ymir.message.Notes;
import org.seasar.ymir.message.impl.MessagesImpl;
import org.seasar.ymir.mock.MockApplication;
import org.seasar.ymir.mock.MockRequest;
import org.seasar.ymir.mock.MockYmir;
import org.seasar.ymir.mock.servlet.MockHttpServletRequest;
import org.seasar.ymir.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.ymir.mock.servlet.MockServletContext;
import org.seasar.ymir.mock.servlet.MockServletContextImpl;
import org.seasar.ymir.token.impl.TokenManagerImpl;

import net.skirnir.freyja.zpt.ZptTemplateContext;

public class EvalTypePrefixHandlerTest extends TestCase {
    private EvalTypePrefixHandler target_ = new EvalTypePrefixHandler();

    private MockServletContext servletContext_ = new MockServletContextImpl(
            "/context");

    private MockHttpServletRequest httpRequest = new MockHttpServletRequestImpl(
            servletContext_, HttpMethod.GET, "/index.html");

    private YmirVariableResolver varResolver_;

    private ZptTemplateContext context_;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        S2Container container = new S2ContainerImpl();
        container.register(new TokenManagerImpl());
        container.register(MessagesImpl.class, Globals.NAME_MESSAGES);

        MockYmir ymir = new MockYmir();
        MockApplication application = new MockApplication();
        application.setS2Container(container);
        ymir.setApplication(application);
        YmirContext.setYmir(ymir);
        target_.setTalesExpressionEvaluator(new YmirTalesExpressionEvaluator());

        varResolver_ = new YmirVariableResolver(new MockRequest(), httpRequest,
                container);
        context_ = new ZptTemplateContext();
    }

    public void test_handle() throws Exception {
        Notes notes = new Notes();
        notes.add("category", new Note("key"));
        httpRequest.setAttribute(RequestProcessor.ATTR_NOTES, notes);
        varResolver_.setVariable("categoryname", "category");

        assertEquals("key", ((Note[]) target_.handle(context_, varResolver_,
                "notes/notes($categoryname)"))[0].getValue());
    }
}
